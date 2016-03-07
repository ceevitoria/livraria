package com.cee.livraria.controller.jsf.conferencia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.ConferenciaConfig;
import com.cee.livraria.entity.config.ConferenciaConfigEntity;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.ItemConferencia;
import com.cee.livraria.entity.estoque.conferencia.ItemConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.StatusConferencia;
import com.cee.livraria.entity.produto.CD;
import com.cee.livraria.entity.produto.DVD;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.cee.livraria.entity.produto.TipoProduto;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity.class, 
	components = { @com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(
		clazz = com.cee.livraria.entity.produto.RegraPesquisaProdutos.class, property = "regra", separate = true) }, 
		details = { @com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
				clazz = com.cee.livraria.entity.estoque.conferencia.ItemConferenciaEntity.class, 
				collectionName = "itemConferencia", numNew = 4, onDemand = false, 
				navigation = @com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail(numberByPage = 30))
})

@PlcConfigForm(
	formPattern = FormPattern.Mdt, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/estoque.conferencia")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("conferencia")
@PlcHandleException
public class ConferenciaMB extends AppMB {

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	private ConferenciaConfig config; 
	
	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("conferencia")
	public ConferenciaEntity createEntityPlc() {

		if (this.config == null) {
			carregaConfiguracao();
		}
		
		if (this.entityPlc == null) {
			this.entityPlc = new ConferenciaEntity();
			this.newEntity();
		}
		
		return (ConferenciaEntity) this.entityPlc;
	}
	
	@Override
	public String create()  {
		String ret = super.create();
		
		((Conferencia)this.entityPlc).setStatusConferencia(StatusConferencia.F);

		return ret;
	}
	
	private void carregaConfiguracao() {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		List listaConfig = (List)iocControleFacadeUtil.getFacade().findSimpleList(context, ConferenciaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{conferencia.err.configuracao.inexistente}");
		}
		
		config = (ConferenciaConfig)listaConfig.get(0);
	}

	private Produto criaArgumentoPesquisaProduto(RegraPesquisaProdutos regra) throws PlcException {
		Produto produtoArg = null;

		if (regra.getTipoProduto() == null) {
			throw new PlcException("{erro.regra.informar.tipoProduto}");
		}
		
		if (TipoProduto.L.equals(regra.getTipoProduto())) {
			produtoArg = new Livro();
			((Livro) produtoArg).setAutor(regra.getAutor());
			((Livro) produtoArg).setColecao(regra.getColecao());
			((Livro) produtoArg).setEdicao(regra.getEdicao());
			((Livro) produtoArg).setEditora(regra.getEditora());
			((Livro) produtoArg).setEspirito(regra.getEspirito());
		} else if (TipoProduto.C.equals(regra.getTipoProduto())) {
			produtoArg = new CD();
			((CD) produtoArg).setArtista(regra.getArtista());
			((CD) produtoArg).setGravadora(regra.getGravadora());
		} else if (TipoProduto.D.equals(regra.getTipoProduto())) {
			produtoArg = new DVD();
			((DVD) produtoArg).setArtista(regra.getArtista());
			((DVD) produtoArg).setGravadora(regra.getGravadora());
		} else {
			produtoArg = new Produto();
		}

		produtoArg.setTitulo(regra.getTitulo());
		produtoArg.setCodigoBarras(regra.getCodigoBarras());
		
		return produtoArg;
	}

	/**
	 * buscarItensPorRegraPrecificacao
	 */
	public String buscarItensPorRegra()  {

		if (this.entityPlc!=null) {
			Conferencia conferencia = (Conferencia)this.entityPlc;
			List<ItemConferencia> listaItensExistentes = conferencia.getItemConferencia();
			PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
				
			boolean ok = false;
			
			if (StatusConferencia.F.equals(conferencia.getStatusConferencia())) {
				ok = true;
				
				// Remove os items vazios (sem livros)
				for (int i=listaItensExistentes.size()-1; i>=0; i--) {
					ItemConferencia itemExistente = (ItemConferencia)listaItensExistentes.get(i);
					
					if (itemExistente.getId() == null && itemExistente.getProduto() == null) {
						listaItensExistentes.remove(itemExistente);
					}
				}
			} else {
				msgUtil.msg("{conferencia.err.buscar.emformacao}", PlcMessage.Cor.msgVermelhoPlc.name());
			}
			
			if (ok) {
				//Busca os livros que satisfazem às regras de pesquisa e os adiciona na listaItens caso ainda não tenham sido adicionados anteriormente
				RegraPesquisaProdutos regra = conferencia.getRegra();
				
				Produto produtoArg = criaArgumentoPesquisaProduto(regra);

				Long contaProdutos = (Long)iocControleFacadeUtil.getFacade().findCount(context, produtoArg);
				
				if (contaProdutos.longValue() > 400) {
					msgUtil.msg("{conferencia.err.buscar.muitosItensExistentes}", new Object[] {contaProdutos}, PlcMessage.Cor.msgVermelhoPlc.name());
					ok = false;
				} else {
					List<Produto> produtos = (List<Produto>)iocControleFacadeUtil.getFacade().findList(context, produtoArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
					List<Estoque> estoques = null;
					
					if (regra.getLocalizacao() == null) {
						estoques = (List<Estoque>) iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoque(context, produtos);
					} else {
						estoques = (List<Estoque>) iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoquePorLocalizacao(context, produtos, regra.getLocalizacao());
					}

					Comparator<Estoque> comparator = new Comparator<Estoque>() {
						public int compare(Estoque o1, Estoque o2) {
							return o1.getProduto().getTitulo().compareTo(o2.getProduto().getTitulo());
						}
					};

					Collections.sort(estoques, comparator);
					
					int totalExistente = 0;
					
					for (Produto produto : produtos) {
						boolean existe = false;
						
						for (ItemConferencia itemExistente : listaItensExistentes) {
							
							if (produto.getId().compareTo(itemExistente.getProduto().getId())==0) {
								existe = true;
								totalExistente++;
								break;
							}
						}

						Estoque itemEstoque = null;
						
						for (Estoque estoque : estoques) {
							
							if (produto.getId().compareTo(estoque.getProduto().getId())==0) {
								itemEstoque = estoque;
								break;
							}
						}
						
						if ((!existe && regra.getLocalizacao() == null) || (!existe && regra.getLocalizacao() != null && itemEstoque != null)) {
							ItemConferencia itemConferencia = criaNovoItem(conferencia, produto, itemEstoque);
							listaItensExistentes.add(itemConferencia);
						}
					}
					
					msgUtil.msg("{conferencia.ok.buscar}", new Object[] {estoques.size()-totalExistente}, PlcMessage.Cor.msgAzulPlc.name());
					msgUtil.msg("{conferencia.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
					
					plcControleConversacao.setAlertaAlteracaoPlc("S");
				}
			}
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	private void carregaEstoqueProdutos(PlcBaseContextVO context , List<ItemConferencia> itensConferencia) {
		List<Produto> produtos = new ArrayList<Produto>(itensConferencia.size());
		
		for (ItemConferencia itemConferencia : itensConferencia) {
			produtos.add(itemConferencia.getProduto());
		}
		
		List<Estoque> estoques = (List<Estoque>)iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoque(context, produtos);
		
		for (Estoque itemEstoque : estoques) {
			
			for (ItemConferencia item : itensConferencia) {
				
				if (item.getProduto().getId().compareTo(itemEstoque.getProduto().getId()) == 0) {
					item.setQuantidadeEstoque(itemEstoque.getQuantidade());
					item.setLocalizacao(itemEstoque.getLocalizacao());
					break;
				}
			}
		}
	}

	private ItemConferencia criaNovoItem(Conferencia conferencia, Produto produto, Estoque itemEstoque) {
		ItemConferencia item = new ItemConferenciaEntity();
		
		item.setConferencia(conferencia);
		item.setProduto(produto);
		item.setTipoProduto(produto.getTipoProduto());
		item.setQuantidadeEstoque(itemEstoque != null ? itemEstoque.getQuantidade() : null);
		item.setLocalizacao(itemEstoque != null ? itemEstoque.getLocalizacao() : null);
		
		((ItemConferenciaEntity)item).setIndExcPlc("N");
		return item;
	}

	public String abrirConferencia()  {
		Conferencia conferencia = (Conferencia)this.entityPlc;

		conferencia.setStatusConferencia(StatusConferencia.A);
		
		return super.save(); 
	}
	
	public String concluirConferencia()  {
		Conferencia conferencia = (Conferencia)this.entityPlc;

		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		RetornoConfig ret = iocControleFacadeUtil.getFacade(IAppFacade.class).concluirConferenciaLivros(context, conferencia);
		
		if (ret.getAlertas().size() > 0) {
			
			for (String alerta : ret.getAlertas()) {
				msgUtil.msg(alerta, PlcMessage.Cor.msgAmareloPlc.name());
			}
		}
		
		if (ret.getMensagens().size() > 0) {
			
			for (String mensagem : ret.getMensagens()) {
				msgUtil.msg(mensagem, PlcMessage.Cor.msgVerdePlc.name());
			}
		}

		plcControleConversacao.setAlertaAlteracaoPlc("N");

		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA);
		
		if ("conferencia".equalsIgnoreCase(nomeAction)) {

			if (config != null) {
				if (PlcYesNo.S.equals(config.getUtilizaLocalizacaoLivros())) {
					contextUtil.getRequest().setAttribute(AppConstants.TELA_CONFEERENCIA.UTILIZA_lOCALIZACAO, PlcConstants.SIM);
				} else {
					contextUtil.getRequest().setAttribute(AppConstants.TELA_CONFEERENCIA.UTILIZA_lOCALIZACAO, PlcConstants.NAO);
				}
			}
			
			if (this.entityPlc!=null) {
				Conferencia conferencia = (Conferencia)this.entityPlc;
				
				if (StatusConferencia.F.equals(conferencia.getStatusConferencia())) {
					contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.SIM);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_CONFERENCIA, PlcConstants.EXIBIR);
				} else {
					contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.NAO);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_CONFERENCIA, PlcConstants.NAO_EXIBIR);
				}

				if (StatusConferencia.A.equals(conferencia.getStatusConferencia())) {
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_CONCLUIR_CONFERENCIA, PlcConstants.EXIBIR);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_CONFERENCIA, PlcConstants.NAO_EXIBIR);
				}

				if (StatusConferencia.C.equals(conferencia.getStatusConferencia())) {
					contextUtil.getRequest().setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
					contextUtil.getRequest().setAttribute( PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.NAO_EXIBIR);
				} else {
					contextUtil.getRequest().setAttribute( PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.EXIBIR);
					contextUtil.getRequest().setAttribute( PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.EXIBIR);
				}
			}
		}
		
		super.handleButtonsAccordingFormPattern();
	}

}

