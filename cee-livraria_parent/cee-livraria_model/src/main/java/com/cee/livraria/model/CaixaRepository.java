package com.cee.livraria.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.CaixaFormaPagto;
import com.cee.livraria.entity.caixa.CaixaFormaPagtoEntity;
import com.cee.livraria.entity.caixa.CaixaMovimento;
import com.cee.livraria.entity.caixa.CaixaMovimentoEntity;
import com.cee.livraria.entity.caixa.StatusCaixa;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.OperacaoCaixaConfig;
import com.cee.livraria.entity.config.OperacaoCaixaConfigEntity;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.persistence.jpa.caixa.CaixaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@SPlcRepository
@PlcAggregationDAOIoC(value=CaixaEntity.class)
public class CaixaRepository extends PlcBaseRepository {

	@Inject
	private CaixaDAO jpa;

	@Inject
	protected transient Logger log;

	private OperacaoCaixaConfig config;
	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();
	
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo, CaixaEntity caixa) throws PlcException {
		mensagens.clear();
		alertas.clear();

		carregaConfiguracao(context);
		recarregaSaldoCaixa(context, caixa);

		try {
			switch (tipo) {
			case AB:
				registrarAberturaCaixa(context, caixa);
				break;
			case SA:
				registrarSangriaCaixa(context, caixa);
				break;
			case SU:
				registrarSuprimentoCaixa(context, caixa);
				break;
			case FE:
				registrarFechamentoCaixa(context, caixa);
				break;
				
			default:
				throw new Exception("Tipo de Movimento de Caixa desconhecido: " + tipo);
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("CaixaRepository", "registrarOperacaoCaixa", e, log, tipo.getLabel());
		}
		
		return new RetornoConfig(config, alertas, mensagens);
	}
	
	private void registrarAberturaCaixa(PlcBaseContextVO context, CaixaEntity caixa) throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava aberto
		if (StatusCaixa.A.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.aberto}");
		}

		// Criar um movimento de abertura do caixa
		criaMovimentoCaixa(context, caixa, data, TipoMovimentoCaixa.AB);
		
		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(context, caixa, data, StatusCaixa.A);
		
		mensagens.add(0,"{msg.caixa.abertura.ok}");
	}

	private void registrarSangriaCaixa(PlcBaseContextVO context, CaixaEntity caixa) throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava aberto
		if (StatusCaixa.F.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
		// Tratar sangria com valor maior que saldo do caixa
		if (caixa.getValor().compareTo(caixa.getSaldo()) > 0) {
			
			// Se permite fazer ajuste no valor do saldo divergente
			if (config.getPermitirAjusteSaldoDivergente().equals(PlcYesNo.S)) {
				double valorInformado = caixa.getValor().doubleValue(); // Valor informado para a sangria do caixa
				double saldoExistente = caixa.getSaldo().doubleValue(); // Saldo real existente no caixa
				
				// Se a divergência estiver dentro do limite de tolerância pré-configurado
				if (config.getValorMaximoAjusteSaldoDivergente().doubleValue() >= Math.abs(saldoExistente-valorInformado)) {
					criaMovimentoAjuste(context, caixa, data, valorInformado, saldoExistente);
					alertas.add(String.format("Foi realizado ajuste automático de saldo no valor de R$ %,.02f devido uma divergência entre o valor sangria informado e o saldo atual!", Math.abs(saldoExistente-valorInformado)) );
				} else {
					throw new PlcException("{erro.caixa.sangria.divergencia.invalida}", new Object[]{config.getValorMaximoAjusteSaldoDivergente()});
				}
			} else {
				throw new PlcException("{erro.caixa.sangria.saldo.insuficiente}");
			}
		}
		
		// Criar um movimento de abertura do caixa
		criaMovimentoCaixa(context, caixa, data, TipoMovimentoCaixa.SA);
		
		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(context, caixa, data, StatusCaixa.A);
		
		mensagens.add(0,"{msg.caixa.sangria.ok}");
	}
	
	private void registrarSuprimentoCaixa(PlcBaseContextVO context, CaixaEntity caixa) throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava aberto
		if (StatusCaixa.F.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
		// Criar um movimento de abertura do caixa
		criaMovimentoCaixa(context, caixa, data, TipoMovimentoCaixa.SU);
		
		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(context, caixa, data, StatusCaixa.A);
		
		mensagens.add(0,"{msg.caixa.suprimento.ok}");
	}
	
	protected void registrarFechamentoCaixa(PlcBaseContextVO context, CaixaEntity caixa) throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava fechado
		if (StatusCaixa.F.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
		//Tratar fechamento com valor de sangria maior que saldo do caixa
		if (caixa.getSaldo() != null && caixa.getValor() != null) {
			double valorInformado = caixa.getValor().doubleValue(); // Valor informado para a sangria do caixa
			double saldoExistente = caixa.getSaldo().doubleValue(); // Saldo real existente no caixa
			
			// Se o saldo divergir deve-se validar conforme configuração
			if (valorInformado != saldoExistente) {
				
				// Se permite fazer o fechamento de caixa com saldo divergente
				if (config.getPermitirFechamentoSaldoDivergente().equals(PlcYesNo.S)) {
				
					// Se permite fazer ajuste no valor do saldo divergente
					if (config.getPermitirAjusteSaldoDivergente().equals(PlcYesNo.S)) {
						
						// Se a divergência estiver dentro do limite de tolerância pré-configurado
						if (config.getValorMaximoAjusteSaldoDivergente().doubleValue() >= Math.abs(saldoExistente-valorInformado)) {
							criaMovimentoAjuste(context, caixa, data, valorInformado, saldoExistente);
							alertas.add(String.format("Foi realizado ajuste automático de saldo no valor de R$ %,.02f devido uma divergência entre o valor sangria informado e o saldo atual!", Math.abs(saldoExistente-valorInformado)) );
						} else {
							throw new PlcException("{erro.caixa.fechamento.divergencia.invalida}", new Object[]{config.getValorMaximoAjusteSaldoDivergente()});
						}
					} else {
						throw new PlcException("{erro.caixa.fechamento.saldo.diverge.do.informado}");
					}
				} else {
					throw new PlcException("{erro.caixa.fechamento.saldo.diverge.do.informado}");
				}
			}
		} else {
			throw new PlcException("{erro.caixa.fechamento.valor.nao.informado}");
		}

		// Criar um movimento de fechamento do caixa
		criaMovimentoCaixa(context, caixa, data, TipoMovimentoCaixa.FE);
		
		// Atualiza o saldo e fecha o caixa 
		atualizaStatusSaldoCaixa(context, caixa, data, StatusCaixa.F);

		mensagens.add(0, "{msg.caixa.fechamento.ok}");
	}

	/**
	 * @param context
	 */
	private void carregaConfiguracao(PlcBaseContextVO context) {
		List listaConfig = (List)jpa.findAll(context, OperacaoCaixaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{erro.caixa.configuracao.inexistente}");
		}
		
		config = (OperacaoCaixaConfig)listaConfig.get(0);
	}

	private void recarregaSaldoCaixa(PlcBaseContextVO context, CaixaEntity caixa) {
		
		// Só precisa recarregar o sado do caixa se o usuário não for um gestor
		if (!context.getUserProfile().isUserInRole("Gestor")) {
			CaixaEntity caixaReal;
			
			caixaReal = (CaixaEntity) jpa.findById(context, CaixaEntity.class, caixa.getId());
			
			if (caixaReal == null) {
				throw new PlcException("{erro.caixa.inexistente}");
			}
			
			caixa.setSaldo(caixaReal.getSaldo());
		}
	}

	/**
	 * Atualiza o status e o saldo do caixa
	 * @param context
	 * @param caixa
	 * @param data
	 */
	private void atualizaStatusSaldoCaixa(PlcBaseContextVO context, CaixaEntity caixa, Date data, StatusCaixa status) {
		caixa.setStatus(status);
		
		if (caixa.getValor() != null && caixa.getValor().doubleValue() != 0.0) {
			caixa.setSaldo(caixa.getSaldo().add(caixa.getValor()));
		}

		caixa.setDataUltAlteracao(data);
		caixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		//atualiza o saldo do caixa no meio de persistencia
		jpa.update(context, caixa);
	}

	/**
	 * @param context
	 * @param caixa
	 * @param data
	 * @param tipo Tipo de movimento do caixa (AB-Abertura / FE-Fechamento / etc)
	 * @throws Exception 
	 * @throws PlcException 
	 */
	private void criaMovimentoCaixa(PlcBaseContextVO context, CaixaEntity caixa, Date data, TipoMovimentoCaixa tipo) throws PlcException, Exception {
		
		if (caixa.getValor() != null && caixa.getValor().doubleValue() > 0.00) {

			switch (tipo) {
			case AB:
				mensagens.add(String.format("Abertura com suprimento no valor de R$ %,.02f", caixa.getValor()));
				break;
			case SA:
				// Movimentos de sangria ou fechamento são negativos
				caixa.setValor(caixa.getValor().negate());
				mensagens.add(String.format("Sangria realizada no valor de R$ %,.02f", caixa.getValor()));
				break;
			case SU:
				mensagens.add(String.format("Suprimento realizado no valor de R$ %,.02f", caixa.getValor()));
				break;
			case FE:
				// Movimentos de sangria ou fechamento são negativos
				caixa.setValor(caixa.getValor().negate());
				mensagens.add(String.format("Fechamento com sangria no valor de R$ %,.02f", caixa.getValor()));
				break;
			}

			CaixaMovimento movCaixa = new CaixaMovimentoEntity();
			movCaixa.setData(data);
			movCaixa.setTipo(tipo);
			movCaixa.setValor(caixa.getValor());
			movCaixa.setObservacao(caixa.getObservacao());
			movCaixa.setSitHistoricoPlc("A");
			movCaixa.setCaixa(caixa);
			
			movCaixa.setDataUltAlteracao(data);
			movCaixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			
			//registra o novo movimento do caixa no meio de persistencia
			jpa.insert(context, movCaixa);
		} else {
			alertas.add("{alerta.caixa.operacao.realizada.valorzerado}");
		}
			
	}

	/**
	 * Criar um movimento de ajuste automatico do saldo do caixa
	 * @param context
	 * @param caixa
	 * @param data
	 * @param saldoInformado
	 * @param saldoExistente
	 */
	private void criaMovimentoAjuste(PlcBaseContextVO context, CaixaEntity caixa, Date data, double saldoInformado, double saldoExistente) {
		CaixaMovimento movAjusteCaixa = new CaixaMovimentoEntity();
		
		movAjusteCaixa.setData(data);
		movAjusteCaixa.setTipo(TipoMovimentoCaixa.AA);
		movAjusteCaixa.setValor(BigDecimal.valueOf(saldoInformado-saldoExistente));
		movAjusteCaixa.setObservacao("Movimento de ajuste automático do saldo conforme permitido na configuracao do sistema");
		movAjusteCaixa.setSitHistoricoPlc("A");
		movAjusteCaixa.setCaixa(caixa);
		movAjusteCaixa.setDataUltAlteracao(data);
		movAjusteCaixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		// Corrige o saldo conforme movimento de ajuste
		caixa.setSaldo(BigDecimal.valueOf(saldoInformado));
		
		//registra o novo movimento do caixa no meio de persistencia
		jpa.insert(context, movAjusteCaixa);
	}

	public PagamentoList obterPagamentosCaixa(PlcBaseContextVO context, Caixa caixa) {
		PagamentoList lista = new PagamentoList();
		
		@SuppressWarnings("unchecked")
		List<CaixaFormaPagto> l = (List<CaixaFormaPagto>)jpa.findByFields(context, CaixaFormaPagtoEntity.class, "querySelByCaixa", new String[]{"caixa"}, new Object[]{caixa});
		
		for (CaixaFormaPagto caixaFormaPagto : l) {
			Pagamento pagto = new Pagamento();
			
			pagto.setFormaPagto(caixaFormaPagto.getFormaPagto());
			pagto.setValor(caixaFormaPagto.getValor());
			
			lista.getItens().add(pagto);	
		}
		
		return lista;
	}
	
}
