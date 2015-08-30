package com.cee.livraria.model.ajuste;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.LivroEntity;
import com.cee.livraria.entity.config.AjusteEstoqueConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.config.TipoMensagemAjusteEstoqueConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ItemMovimento;
import com.cee.livraria.entity.estoque.ItemMovimentoEntity;
import com.cee.livraria.entity.estoque.ModoMovimento;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
import com.cee.livraria.entity.estoque.apoio.VendaLivro;
import com.cee.livraria.persistence.jpa.ajuste.AjusteEstoqueDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.model.PlcBaseRepository;

/**
 * Classe de Modelo gerada pelo assistente
 */
 
@SPlcRepository 
@PlcAggregationIoC(clazz=AjusteEstoque.class)
public class AjusteEstoqueRepository extends PlcBaseRepository {

	@Inject
	private AjusteEstoqueDAO dao;

	@Inject
	protected transient Logger log;

	private AjusteEstoqueConfig config;

	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();

	public RetornoConfig concluirAjusteEstoque(PlcBaseContextVO context, AjusteEstoque ajusteEstoque) throws PlcException {
		Date dataAjusteEstoque = new Date();
		mensagens.clear();
		alertas.clear();
		
		try {
			carregaConfiguracao(context);

			registraMovimento(context, ajusteEstoque, dataAjusteEstoque);
			atualizaEstoque(context, ajusteEstoque, dataAjusteEstoque);
			
			mensagens.add("Ajuste de Estoque realizado com sucesso!"); 
			
			ajusteEstoque.setStatus(StatusAjuste.C);
			ajusteEstoque.setDataUltAlteracao(dataAjusteEstoque);
			ajusteEstoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			dao.update(context, ajusteEstoque);
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("AjusteEstoqueRepository", "concluirAjusteEstoque", e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}
	
	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
		List listaConfig = (List)dao.findAll(context, AjusteEstoqueConfig.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{ajusteEstoque.err.configuracao.inexistente}");
		}
		
		config = (AjusteEstoqueConfig)listaConfig.get(0);
	}
	
	/**
	 * Registra o movimento de ajuste de estoque para os livros informados
	 * @param relacaoLivros Relação dos livros sendo vendidos
	 */
	private void registraMovimento(PlcBaseContextVO context, AjusteEstoque ajusteEstoque, Date dataMovimento) throws PlcException {
		Movimento mov = new MovimentoEntity();
		List<ItemMovimento> itens = new ArrayList<ItemMovimento>();
		double valorMovimento = 0.0;
		int qtdEntrada = 0;
		int qtdSaida = 0;
		
		List<ItemAjusteEstoque> itensAjusteEstoque = ajusteEstoque.getItemAjusteEstoque();
		
		for (ItemAjusteEstoque itemAjuste : itensAjusteEstoque) {
			
			// Evita os itens em branco
			if (itemAjuste.getLivro() != null && itemAjuste.getLivro().getId() != null) {
				
				if (itemAjuste.getQuantidadeInformada() == null) {
					throw new PlcException("ajusteEstoque.err.item.sem.quantidade");
				}
				
				int qtdInformada = itemAjuste.getQuantidadeInformada();
				int qtdExistente = itemAjuste.getQuantidadeEstoque();
				int qtdSaldo = 0;
				int sinal = 1;
				
				TipoMovimento tipoMovimento = null;
				
				if (qtdInformada > qtdExistente) {
					tipoMovimento = TipoMovimento.E;
					qtdSaldo = qtdInformada - qtdExistente;
					sinal = 1;
					qtdEntrada++;
				}

				if (qtdInformada < qtdExistente) {
					tipoMovimento = TipoMovimento.S;
					qtdSaldo = qtdExistente - qtdInformada;
					sinal = -1;
					qtdSaida++;
				}
				
				if (qtdSaldo != 0) {
					ItemMovimento itemMovimento = new ItemMovimentoEntity();
					
					Livro livro = (Livro)dao.findById(context, LivroEntity.class, itemAjuste.getLivro().getId());
					
					itemMovimento.setLivro(livro);
					itemMovimento.setTipo(tipoMovimento);
					itemMovimento.setQuantidade(qtdSaldo);
					itemMovimento.setValorUnitario(livro.getPrecoUltCompra());
					itemMovimento.setValorTotal(livro.getPrecoUltCompra().multiply(new BigDecimal(qtdSaldo)));
					itemMovimento.setMovimento(mov);
					
					itemMovimento.setDataUltAlteracao(dataMovimento);
					itemMovimento.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
					
					itens.add(itemMovimento);
					
					valorMovimento += (livro.getPrecoUltCompra().doubleValue() * qtdSaldo * sinal);
				}
			}
		}

		TipoMovimento tipoMovimento = null;
		
		if (qtdEntrada > 0 && qtdSaida > 0) {
			tipoMovimento = TipoMovimento.A;
		} else if (qtdEntrada > 0) {
			tipoMovimento = TipoMovimento.E;
		} else if (qtdSaida > 0) {
			tipoMovimento = TipoMovimento.S;
		}
		
		mov.setData(dataMovimento);
		mov.setTipo(tipoMovimento);
		mov.setModo(ModoMovimento.A);
		mov.setValor(BigDecimal.valueOf(valorMovimento));
		mov.setItemMovimento(itens);

		mov.setDataUltAlteracao(dataMovimento);
		mov.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		dao.insert(context, mov);
	}
	
	/**
	 * Atualiza o saldo dos livros sendo vendidos no estoque
	 * OBS: Não faz crítica para saldo negativo.
	 * @param relacaoLivros Relação dos livros sendo vendidos
	 * @param dataVenda Data em que a venda foi realizada
	 * @return quantidade vendida de livros
	 * @throws PlcException
	 */
	private void atualizaEstoque(PlcBaseContextVO context, AjusteEstoque ajusteEstoque, Date dataAjuste) throws PlcException {
		List<ItemAjusteEstoque> itensAjusteEstoque = ajusteEstoque.getItemAjusteEstoque();
		
		for (ItemAjusteEstoque itemAjuste : itensAjusteEstoque) {

			if (itemAjuste.getLivro() != null && itemAjuste.getLivro().getId() != null) {
				@SuppressWarnings("unchecked")
				List<Estoque> lista = (List<Estoque>)dao.findByFields(context, EstoqueEntity.class, "querySelByLivro", new String[]{"livro"}, new Object[]{itemAjuste.getLivro()});
				
				if (lista != null && lista.size() == 1) {
					int qtdInformada = itemAjuste.getQuantidadeInformada();
					int qtdExistente = itemAjuste.getQuantidadeEstoque();
					int qtdSaldo = 0;
					
					if (qtdInformada > qtdExistente) {
						qtdSaldo = qtdInformada - qtdExistente;
					}

					if (qtdInformada < qtdExistente) {
						qtdSaldo = qtdExistente - qtdInformada;
					}
					
					if (qtdSaldo != 0) {
						Estoque estoque = (Estoque)lista.get(0);
						
						estoque.setQuantidade(qtdInformada);
						
						if (PlcYesNo.S.equals(config.getUtilizaLocalizacaoLivros()) ) {
							
							if (PlcYesNo.S.equals(config.getAjusteAutomaticoLocalizacaoLivros())) {
								estoque.setLocalizacao(itemAjuste.getLocalizacao());
							} else {
								
								if (estoque.getLocalizacao().getId().compareTo(itemAjuste.getLocalizacao().getId()) != 0) {
									throw new PlcException("ajusteEstoque.err.item.localizacao.divergente", 
										new Object[] {estoque.getLivro().getTitulo()});
								}
							}
						}
						estoque.setDataConferencia(dataAjuste);

						estoque.setDataUltAlteracao(dataAjuste);
						estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

						dao.update(context, estoque);
					}
				}
			}
		}
	}
	
}
