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
import com.cee.livraria.entity.pagamento.FormaPagLivro;
import com.cee.livraria.entity.pagamento.FormaPagLivroEntity;
import com.cee.livraria.entity.pagamento.FormaPagto;
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
	private List<FormaPagLivro> formasPagLivro;
	private CaixaEntity caixa;
	private PlcBaseContextVO context;
	private List itens;
	
	private double valorGeraCaixa = 0.0;
	
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo, CaixaEntity caixa, List itens) throws PlcException {
		mensagens.clear();
		alertas.clear();
		
		this.context = context;
		this.caixa = caixa;
		this.itens = itens;

		carregaConfiguracao();
		recarregaSaldoCaixa();
		inicializaFormasPagLivro();
		inicializaValorCaixa();

		try {
			switch (tipo) {
			case AB:
				registrarAberturaCaixa();
				break;
			case SA:
				registrarSangriaCaixa();
				break;
			case SU:
				registrarSuprimentoCaixa();
				break;
			case FE:
				registrarFechamentoCaixa();
				break;
				
			default:
				throw new Exception("Tipo de Movimento de Caixa desconhecido: " + tipo);
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("CaixaRepository", "registrarOperacaoCaixa", e, log, tipo.getLabel());
		}
		
		return new RetornoConfig(this.caixa, this.config, this.alertas, this.mensagens);
	}
	
	private void inicializaFormasPagLivro() throws PlcException {
		formasPagLivro = (List<FormaPagLivro>)jpa.findAll(context, FormaPagLivroEntity.class, null);
		
		if (formasPagLivro == null || formasPagLivro.size() == 0) {
			throw new PlcException("erro.caixa.formasPagLivro.nao.cadastradas");
		}
	}

	private void inicializaValorCaixa() {
		FormaPagLivro formaPagLivro = null;
		double valorInformado = 0.0;
		valorGeraCaixa = 0.0;

		// Calculo o valor total informado nas formas de pagamento
		for (Object object : itens) {
			Pagamento pagamento = (Pagamento)object;
			
			if (pagamento.getFormaPagto() != null) {
				// Localiza a Forma de Pagamento Livro relacionada ao pagamento do caixa
				for (FormaPagLivro fpl : formasPagLivro) {
					
					if (fpl.getFormaPagto().getId().equals(pagamento.getFormaPagto().getId())) {
						formaPagLivro = fpl;
						break;
					}
				}
			}
			
			// Se a forma de pagamento do caixa gera caixa
			if (formaPagLivro != null && PlcYesNo.S.equals(formaPagLivro.getIsGeraCaixa()) && pagamento.getValor() != null) {
				valorInformado += pagamento.getValor().doubleValue(); 
			}
		}
		
		List<CaixaFormaPagto> formasPagtoCaixa = null;
		formasPagtoCaixa = (List<CaixaFormaPagto>)jpa.findByFields(context, CaixaFormaPagtoEntity.class, "querySelByCaixa", new String[]{"caixa"}, new Object[]{caixa});
		
		for (CaixaFormaPagto caixaFormaPagto: formasPagtoCaixa) {

			// Localiza a Forma de Pagamento Livro relacionada ao pagamento do caixa
			for (FormaPagLivro fpl : formasPagLivro) {
				
				if (fpl.getFormaPagto().getId().equals(caixaFormaPagto.getFormaPagto().getId())) {
					formaPagLivro = fpl;
					break;
				}
			}
			
			// Se a forma de pagamento do caixa gera caixa
			if (formaPagLivro != null && PlcYesNo.S.equals(formaPagLivro.getIsGeraCaixa()) && caixaFormaPagto.getValor() != null) {
				valorGeraCaixa += caixaFormaPagto.getValor().doubleValue();
			}
		}
		
		caixa.setValor(new BigDecimal(valorInformado));
	}

	private void registrarAberturaCaixa() throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava aberto
		if (StatusCaixa.A.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.aberto}");
		}

		// Criar um movimento de abertura do caixa
		criaMovimentoCaixa(data, TipoMovimentoCaixa.AB);
		
		// Atualiza o saldo de cada forma de pagamento
		atualizaSaldoFormasPagto(data, TipoMovimentoCaixa.AB);

		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(data, StatusCaixa.A, TipoMovimentoCaixa.AB);
		
		mensagens.add(0,"{msg.caixa.abertura.ok}");
	}

	private void registrarSangriaCaixa() throws PlcException, Exception {
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
		criaMovimentoCaixa(data, TipoMovimentoCaixa.SA);

		// Atualiza o saldo de cada forma de pagamento
		atualizaSaldoFormasPagto(data, TipoMovimentoCaixa.SA);

		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(data, StatusCaixa.A, TipoMovimentoCaixa.SA);
		
		mensagens.add(0,"{msg.caixa.sangria.ok}");
	}
	
	private void registrarSuprimentoCaixa() throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava aberto
		if (StatusCaixa.F.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
		// Criar um movimento de abertura do caixa
		criaMovimentoCaixa(data, TipoMovimentoCaixa.SU);
		
		// Atualiza o saldo de cada forma de pagamento
		atualizaSaldoFormasPagto(data, TipoMovimentoCaixa.SU);
		
		// Atualiza o saldo e abre o caixa 
		atualizaStatusSaldoCaixa(data, StatusCaixa.A, TipoMovimentoCaixa.SU);
		
		mensagens.add(0,"{msg.caixa.suprimento.ok}");
	}
	
	protected void registrarFechamentoCaixa() throws PlcException, Exception {
		Date data = new Date();
		
		// Verifica se o caixa já estava fechado
		if (StatusCaixa.F.equals(caixa.getStatus())) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
		//Tratar fechamento com valor de sangria maior que saldo do caixa
		if (caixa.getValor() != null  && caixa.getSaldo() != null) {
			
			double valorInformado = caixa.getValor().doubleValue(); // Valor informado para a sangria do caixa
//			double saldoExistente = caixa.getSaldo().doubleValue(); // Saldo real existente no caixa
			double saldoExistente = valorGeraCaixa; // Saldo real existente no caixa para os pagamentos que geram caixa
			
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
		criaMovimentoCaixa(data, TipoMovimentoCaixa.FE);
		
		// Atualiza o saldo de cada forma de pagamento
		atualizaSaldoFormasPagto(data, TipoMovimentoCaixa.FE);

		// Atualiza o saldo e fecha o caixa 
		atualizaStatusSaldoCaixa(data, StatusCaixa.F, TipoMovimentoCaixa.FE);

		mensagens.add(0, "{msg.caixa.fechamento.ok}");
	}

	/**
	 * Atualiza o caixa para cada tipo de pagamento realizado
	 * @param caixa
	 * @param data
	 * @param itens Lista dos valores por forma de pagamentos
	 */
	@SuppressWarnings("unchecked")
	private void atualizaSaldoFormasPagto(Date data, TipoMovimentoCaixa tipo) throws PlcException {
		List<CaixaFormaPagto> formasPagtoCaixa = null;
		formasPagtoCaixa = (List<CaixaFormaPagto>)jpa.findByFields(context, CaixaFormaPagtoEntity.class, "querySelByCaixa", new String[]{"caixa"}, new Object[]{caixa});
		
		for (CaixaFormaPagto caixaFormaPagto: formasPagtoCaixa) {
			caixaFormaPagto = (CaixaFormaPagto)jpa.findById(context, CaixaFormaPagtoEntity.class, caixaFormaPagto.getId());
			
			FormaPagLivro formaPagLivro = null;
			Pagamento pagto = null;
			
			// Localiza o pagamento relativo ao pagamento do caixa
			for (Object o : itens) {
				Pagamento p = (Pagamento)o;
				FormaPagto fp = p.getFormaPagto();
			
				if (fp != null) {
				
					if (caixaFormaPagto.getFormaPagto().getId().equals(fp.getId())) {
						pagto = p;
						break;
					}
				}
			}

			// Localiza a Forma de Pagamento Livro relacionada ao pagamento do caixa
			for (FormaPagLivro fpl : formasPagLivro) {
				
				if (fpl.getFormaPagto().getId().equals(caixaFormaPagto.getFormaPagto().getId())) {
					formaPagLivro = fpl;
					break;
				}
			}
			
			// Se localizar é por que o usuário informou esta forma de pagamento
			if (pagto != null) {
				double valorPagto = pagto.getValor().doubleValue();
				
				switch (tipo) {
				case AB:
					break;
				case SA:
					// Movimentos de sangria devemos trocar o sinal do atributo valor pois sao retiradas do caixa
					valorPagto = valorPagto * -1;
					break;
				case SU:
					break;
				case FE:
					// Movimentos de sangria devemos trocar o sinal do atributo valor pois sao retiradas do caixa
					valorPagto = valorPagto * -1;
					break;
				}
				
				double valorCaixaFormaPagto = caixaFormaPagto.getValor().doubleValue();
				
				valorCaixaFormaPagto = valorCaixaFormaPagto + valorPagto;
				
				if (valorCaixaFormaPagto < 0.0) {
					throw new PlcException("{erro.caixa.operacao.saldo.formaPagamento.negativo}");
				}

				caixaFormaPagto.setValor(new BigDecimal(valorCaixaFormaPagto));
			} else {
				// Se não localizar o pagamento e
				// o tipo de operação for Fechamento do Caixa e 
				// a forma de pag livro associada não gera caixa 
				// --> devo zerar esta forma de pagamento
				if (pagto == null && TipoMovimentoCaixa.FE.equals(tipo) && PlcYesNo.N.equals(formaPagLivro.getIsGeraCaixa())) {
					// TODO: Cuidar para que este caixa pagamento já tenha gerado crédito na conta apropriada do Plano de Contas da CEE
					caixaFormaPagto.setValor(new BigDecimal(0.0));
				}
			}

			caixaFormaPagto.setDataUltAlteracao(data);
			caixaFormaPagto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			
			try {
				jpa.update(context, caixaFormaPagto);
			} catch (PlcException plcE) {
				throw plcE;
			} catch (Exception e) {
				throw new PlcException("CaixaRepository", "atualizaSaldoFormasPagto", e, log, "jpa.update(context, caixaFormaPagto)");
			}
		}
	}

	
	/**
	 * @param context
	 */
	private void carregaConfiguracao() {
		List listaConfig = (List)jpa.findAll(context, OperacaoCaixaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{erro.caixa.configuracao.inexistente}");
		}
		
		config = (OperacaoCaixaConfig)listaConfig.get(0);
	}

	private void recarregaSaldoCaixa() {
		
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
	private void atualizaStatusSaldoCaixa(Date data, StatusCaixa status, TipoMovimentoCaixa tipo) throws PlcException {
		Caixa caixaAtual = (CaixaEntity) jpa.findById(context, CaixaEntity.class, caixa.getId());
		
		caixaAtual.setStatus(status);
		
		if (TipoMovimentoCaixa.FE.equals(tipo)) {
			caixaAtual.setSaldo(new BigDecimal(0.0));
		} else {
			
			if (caixa.getValor() != null && caixa.getValor().doubleValue() != 0.0) {
				caixaAtual.setSaldo(caixa.getSaldo().add(caixa.getValor()));
			}	
		}
		
		caixaAtual.setDataUltAlteracao(data);
		caixaAtual.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		//atualiza o saldo do caixa no meio de persistencia
		try {
			jpa.update(context, caixaAtual);
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("CaixaRepository", "atualizaStatusSaldoCaixa", e, log, "jpa.update(context, caixa)");
		}
		
		caixa.setStatus(caixaAtual.getStatus());
		caixa.setSaldo(caixaAtual.getSaldo());
		caixa.setObservacao(null);
	}

	/**
	 * @param context
	 * @param caixa
	 * @param data
	 * @param tipo Tipo de movimento do caixa (AB-Abertura / FE-Fechamento / etc)
	 * @throws Exception 
	 * @throws PlcException 
	 */
	private void criaMovimentoCaixa(Date data, TipoMovimentoCaixa tipo) throws PlcException, Exception {
		
		if (caixa.getValor() != null && caixa.getValor().doubleValue() > 0.00) {

			switch (tipo) {
			case AB:
				mensagens.add(String.format("Abertura com suprimento no valor de R$ %,.02f", caixa.getValor()));
				break;
			case SA:
				// Movimentos de sangria ou fechamento são negativos
				caixa.setValor(caixa.getValor().negate());
				mensagens.add(String.format("Sangria realizada no valor de R$ %,.02f", caixa.getValor().negate()));
				break;
			case SU:
				mensagens.add(String.format("Suprimento realizado no valor de R$ %,.02f", caixa.getValor()));
				break;
			case FE:
				// Movimentos de sangria ou fechamento são negativos
				caixa.setValor(caixa.getValor().negate());
				mensagens.add(String.format("Fechamento com sangria no valor de R$ %,.02f", caixa.getValor().negate()));
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
			try {
				jpa.insert(context, movCaixa);
			} catch (PlcException plcE) {
				throw plcE;
			} catch (Exception e) {
				throw new PlcException("CaixaRepository", "criaMovimentoCaixa", e, log, "jpa.insert(context, movCaixa)");
			}
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
