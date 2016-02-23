package com.cee.livraria.model.conferencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.config.ConferenciaConfig;
import com.cee.livraria.entity.config.ConferenciaConfigEntity;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.config.TipoMensagemConferenciaConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.ItemConferencia;
import com.cee.livraria.entity.estoque.conferencia.ResultadoConferencia;
import com.cee.livraria.entity.estoque.conferencia.StatusConferencia;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.cee.livraria.persistence.jpa.conferencia.ConferenciaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.bindingtype.PlcUpdateBefore;

/**
 * Classe de Modelo gerada pelo assistente
 */
 
@SPlcRepository 
@PlcAggregationIoC(clazz=ConferenciaEntity.class)
public class ConferenciaRepository extends PlcBaseRepository {

	@Inject
	private ConferenciaDAO dao;

	@Inject
	protected transient Logger log;

	private ConferenciaConfig config;

	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();

	public void beforeUpdate(@Observes @PlcUpdateBefore PlcBaseContextVO context) throws PlcException {
		
		if (context.getUrl().equalsIgnoreCase("conferencia")) {
			Conferencia conferencia = (Conferencia)context.getEntityForExtension();
	
			if (conferencia != null && StatusConferencia.C.equals(conferencia.getStatus())) {
				List<ItemConferencia> itens = conferencia.getItemConferencia();
				
				if (itens.size() == 0) {
					throw new PlcException("{conferencia.err.sem.itens}");
				}
				
				for (ItemConferencia item: itens) {
					
					if (item.getLocalizacao() == null || item.getLocalizacao().getId() == null) {
						throw new PlcException("{conferencia.err.item.sem.localizacao}");
					}
	
					if (item.getQuantidadeConferida() == null ) {
						throw new PlcException("{conferencia.err.item.sem.quantidade}");
					}
				}
			}
		}
	}

	public RetornoConfig concluirConferencia(PlcBaseContextVO context, Conferencia conferencia) throws PlcException {
		Date dataConferencia = new Date();
		boolean autorizaGravacao = true;
		int totalItensQuantidadeDivergente=0;
		int totalItensLocalizacaoDivergente=0;
		int quantidadeProdutosSemEstoque=0;

		mensagens.clear();
		alertas.clear();
		
		try {
			carregaConfiguracao(context);
			List<ItemConferencia> itensConferencia = conferencia.getItemConferencia();
			
			for (ItemConferencia itemConferencia : itensConferencia) {
				Produto produto = itemConferencia.getProduto();
				List<Estoque> estoqueList = dao.findByFields(context, EstoqueEntity.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
				
				if (estoqueList == null || estoqueList.size() == 0) {
					quantidadeProdutosSemEstoque++;
					
					if (config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.D) ) {
						alertas.add(String.format("O produto '%s' não possui estoque associado.", 
							new Object[]{itemConferencia.getProduto().getTitulo()}));
					}
					
				} else if (estoqueList.size() == 1) {
					Estoque estoque = estoqueList.get(0);

					// Se foi configurado para utilizar a localização do livros na conferencia de estoque
					if (PlcYesNo.S.equals(config.getUtilizaLocalizacaoLivros())) {
						
						if (itemConferencia.getLocalizacao().getId().compareTo(estoque.getLocalizacao().getId())!=0) {
							
							if (PlcYesNo.S.equals(config.getAlertaTrocaLocalizacaoLivros())) {
								
								if (config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.D) ) {
									alertas.add(String.format("O produto '%s' teve sua localização trocada de '%s' para '%s'", 
											new Object[]{itemConferencia.getProduto().getTitulo(),
											estoque.getLocalizacao().getDescricao(),
											itemConferencia.getLocalizacao().getDescricao()}));
								}
							}
							
							if (PlcYesNo.N.equals(config.getPermiteTrocaLocalizacaoLivros())) {
								autorizaGravacao = false;
	
								if (config.getTipoMensagem().equals(TipoMensagemConferenciaConfig.D) ) {
									alertas.add("A troca da localização não está permitida nas configurações do sistema!");
								}
								
							} else if (PlcYesNo.S.equals(config.getAjusteAutomaticoLocalizacaoLivros())) {
								estoque.setLocalizacao(itemConferencia.getLocalizacao());
								estoque.setDataConferencia(dataConferencia);
								estoque.setDataUltAlteracao(dataConferencia);
								estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
								dao.update(context, estoque);
							}
							
							totalItensLocalizacaoDivergente++;
						}
					}
					
					// Grava no item da conferencia a quantidade que existe atualmente contabilizada no estoque
					itemConferencia.setQuantidadeEstoque(estoque.getQuantidade());
					
					if (itemConferencia.getQuantidadeConferida().compareTo(estoque.getQuantidade()) != 0) {

						if (config.getTipoMensagem().equals(TipoMensagemConferenciaConfig.D) ) {
							alertas.add(String.format("Quantidade divergente para o produto '%s'. Registrado: '%d'. Informado: '%d'", 
									new Object[]{itemConferencia.getProduto().getTitulo(),
									estoque.getQuantidade(),
									itemConferencia.getQuantidadeConferida()}));
						}
						
						totalItensQuantidadeDivergente++;
					}
				}
			}

			if (!config.getTipoMensagem().equals(TipoMensagemConferenciaConfig.B) ) {

				if (PlcYesNo.S.equals(config.getUtilizaLocalizacaoLivros()) && PlcYesNo.N.equals(config.getAjusteAutomaticoLocalizacaoLivros())) {
					mensagens.add(String.format("Total de itens com divergência na localização: %d", new Object[]{totalItensLocalizacaoDivergente})); 
				}
				
				mensagens.add(String.format("Total de itens com divergência na quantidade: %d", new Object[]{totalItensQuantidadeDivergente}));
				
				if (quantidadeProdutosSemEstoque == 1) {
					alertas.add("Apenas um produto não possui estoque associado. Será criado automaticamente ao 'Ajustar Estoque'.");
				} else if (quantidadeProdutosSemEstoque > 1) {
					alertas.add(String.format("'%d' produtos não possuem estoque associado. Será criado automaticamente ao 'Ajustar Estoque'.", 
						new Object[]{quantidadeProdutosSemEstoque}));
				}
				
			}

			if (autorizaGravacao) {

				mensagens.add("     "); 
				if (totalItensQuantidadeDivergente+totalItensLocalizacaoDivergente+quantidadeProdutosSemEstoque == 0) {
					conferencia.setResultado(ResultadoConferencia.S);
					mensagens.add("Conferência gravada com sucesso sem divergência!"); 
				} else {
					conferencia.setResultado(ResultadoConferencia.D);
					mensagens.add("Conferência gravada com sucesso com divergência!"); 
				}
				
				conferencia.setStatus(StatusConferencia.C);
				conferencia.setDataUltAlteracao(dataConferencia);
				conferencia.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				dao.update(context, conferencia);

				
				if (ResultadoConferencia.D.equals(conferencia.getResultado())) {
					criarAjusteEstoqueParaDivergencia(context, conferencia, dataConferencia);
					mensagens.add("Foi cadastrado um novo 'Ajuste de Estoque' para tratar esta divergência!"); 
				}
				
			} else {
				alertas.add("Não está autorizado a conclusão da conferência com divergência de localização!"); 
				alertas.add("Solicite o Gestor para ajustar a configuração da conferência para aceitar a troca de localização!"); 
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("ConferenciaRepository", "concluirConferencia", e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}
	
	private void criarAjusteEstoqueParaDivergencia(PlcBaseContextVO context, Conferencia conferencia, Date dataConferencia) throws PlcException {
		AjusteEstoque ajusteEstoque = new AjusteEstoque();

		ajusteEstoque.setData(dataConferencia);
		ajusteEstoque.setNome("Ajuste para Conferência: '" + conferencia.getNome() + "'");
		ajusteEstoque.setDescricao("Ajuste criado automaticamente pois a conferência foi concluída com divergência.");
		ajusteEstoque.setRegra(new RegraPesquisaProdutos());
		ajusteEstoque.setStatus(StatusAjuste.A);
		ajusteEstoque.setConferencia(conferencia);

		dao.insert(context, ajusteEstoque);

		List<ItemConferencia> itensConferencia = conferencia.getItemConferencia();
		List<ItemAjusteEstoque> itensAjuste = new ArrayList<ItemAjusteEstoque>(itensConferencia.size());
		
		for (ItemConferencia itemConferencia : itensConferencia) {
			
			if (itemConferencia.getQuantidadeEstoque() == null || itemConferencia.getQuantidadeConferida().compareTo(itemConferencia.getQuantidadeEstoque())!=0) {
				ItemAjusteEstoque itemAjuste = new ItemAjusteEstoque();
				
				itemAjuste.setProduto(itemConferencia.getProduto());
				itemAjuste.setTipoProduto(itemConferencia.getTipoProduto());
				itemAjuste.setLocalizacao(itemConferencia.getLocalizacao());
				itemAjuste.setQuantidadeEstoque(itemConferencia.getQuantidadeEstoque() != null ? itemConferencia.getQuantidadeEstoque() : 0);
				itemAjuste.setQuantidadeInformada(itemConferencia.getQuantidadeConferida());
				itemAjuste.setAjusteEstoque(ajusteEstoque);
				
				itemAjuste.setDataUltAlteracao(dataConferencia);
				itemAjuste.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				dao.insert(context, itemAjuste);
				
				itensAjuste.add(itemAjuste);
			}
		}
		
		ajusteEstoque.setItemAjusteEstoque(itensAjuste);

		dao.update(context, ajusteEstoque);
	}

	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
		List listaConfig = (List)dao.findAll(context, ConferenciaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{conferencia.err.configuracao.inexistente}");
		}
		
		config = (ConferenciaConfig)listaConfig.get(0);
	}
	
}
