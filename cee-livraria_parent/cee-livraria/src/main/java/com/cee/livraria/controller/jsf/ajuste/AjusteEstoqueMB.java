package com.cee.livraria.controller.jsf.ajuste;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.AjusteEstoqueConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
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

@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.ajuste.AjusteEstoque.class, 
	components = { @com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(
		clazz = com.cee.livraria.entity.produto.RegraPesquisaProdutos.class, property = "regra", separate = true) }, 
		details = { @com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
				clazz = com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque.class, 
				collectionName = "itemAjusteEstoque", numNew = 4, onDemand = false, 
				navigation = @com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail(numberByPage = 30))
})

@PlcConfigForm(
	formPattern = FormPattern.Mdt, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/estoque.ajuste")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("ajusteEstoque")
@PlcHandleException
public class AjusteEstoqueMB extends AppMB {

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	private AjusteEstoqueConfig config; 
	
	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("ajusteEstoque")
	public AjusteEstoque createEntityPlc() {

		if (this.config == null) {
			carregaConfiguracao();
		}
		
		if (this.entityPlc == null) {
			this.entityPlc = new AjusteEstoque();
			this.newEntity();
			
			if ((((AjusteEstoque)this.entityPlc).getStatusAjuste()) == null) {
				((AjusteEstoque)this.entityPlc).setStatusAjuste(StatusAjuste.F);
			}
		}
		
		return (AjusteEstoque) this.entityPlc;
	}

	@Override
	public String create()  {
		String ret = super.create();
		
		((AjusteEstoque)this.entityPlc).setStatusAjuste(StatusAjuste.F);

		return ret;
	}

	private void carregaConfiguracao() {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		List listaConfig = (List)iocControleFacadeUtil.getFacade().findSimpleList(context, AjusteEstoqueConfig.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{ajusteEstoque.err.configuracao.inexistente}");
		}
		
		config = (AjusteEstoqueConfig)listaConfig.get(0);
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

		produtoArg.setTipoProduto(regra.getTipoProduto());
		produtoArg.setTitulo(regra.getTitulo());
		produtoArg.setCodigoBarras(regra.getCodigoBarras());
		produtoArg.setLocalizacao(regra.getLocalizacao());
		
		return produtoArg;
	}
	
	/**
	 * buscarItensPorRegraPrecificacao
	 */
	public String buscarItensPorRegra()  {
		String ret = null;
		
		try {
			ret = buscarItens();
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}
		
		return ret;
	}
	
	@SuppressWarnings("unchecked")
	private String buscarItens() {

		if (this.entityPlc!=null) {
			AjusteEstoque ajusteEstoque = (AjusteEstoque)this.entityPlc;
			
			List<ItemAjusteEstoque> listaItensExistentes = ajusteEstoque.getItemAjusteEstoque();
			
			// Remove os items vazios (sem livros)
			for (int i=listaItensExistentes.size()-1; i>=0; i--) {
				ItemAjusteEstoque itemExistente = (ItemAjusteEstoque)listaItensExistentes.get(i);

				if (itemExistente.getId() == null && itemExistente.getProduto() == null) {
					listaItensExistentes.remove(itemExistente);
				}
			}
				
			boolean ok = false;
			
			if (StatusAjuste.F.equals(ajusteEstoque.getStatusAjuste())) {
				ok = true;
			} else {
				msgUtil.msg("{ajusteEstoque.err.buscar.pendente}", PlcMessage.Cor.msgVermelhoPlc.name());
			}
			
			if (ok) {
				//Busca os produtos que satisfazem às regras de pesquisa e os adiciona na listaItens caso ainda não tenham sido adicionados anteriormente
				RegraPesquisaProdutos regra = ajusteEstoque.getRegra();
				
				Produto produtoArg = criaArgumentoPesquisaProduto(regra);

				PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

				Long contaProdutos = (Long)iocControleFacadeUtil.getFacade().findCount(context, produtoArg);
				
				if (contaProdutos.longValue() > 100) {
					msgUtil.msg("{ajusteEstoque.err.buscar.muitosItensExistentes}", new Object[] {contaProdutos}, PlcMessage.Cor.msgVermelhoPlc.name());
					ok = false;
				} else {
					Estoque estoque;
					produtoArg.setLocalizacao(regra.getLocalizacao());
					List<Produto> produtos = (List<Produto>)iocControleFacadeUtil.getFacade(IAppFacade.class).recuperarProdutos(context, produtoArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
					
					int totalExistente = 0;
					
					for (Produto produto : produtos) {
						boolean existe = false;
						
						for (ItemAjusteEstoque itemExistente : listaItensExistentes) {
							
							if (produto.getId().compareTo(itemExistente.getProduto().getId())==0) {
								existe = true;
								totalExistente++;
								break;
							}
						}
						
						if (!existe) {
							ItemAjusteEstoque itemAjusteEstoque = criaNovoItem(ajusteEstoque, produto);
							listaItensExistentes.add(itemAjusteEstoque);
						}
					}
					
					msgUtil.msg("{ajusteEstoque.ok.buscar}", new Object[] {produtos.size()-totalExistente}, PlcMessage.Cor.msgAzulPlc.name());
					msgUtil.msg("{ajusteEstoque.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
					
					plcControleConversacao.setAlertaAlteracaoPlc("S");
				}
			}
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	private ItemAjusteEstoque criaNovoItem(AjusteEstoque ajusteEstoque, Produto produto) {
		ItemAjusteEstoque item = new ItemAjusteEstoque();
		
		item.setAjusteEstoque(ajusteEstoque);
		item.setProduto(produto);
		item.setTipoProduto(produto.getTipoProduto());
		item.setQuantidadeEstoque(produto.getEstoque() != null ? produto.getEstoque().getQuantidade() : null);
		item.setLocalizacao(produto.getLocalizacao());
		item.setIndExcPlc("N");
		
		return item;
	}

	public String abrirAjusteEstoque() {
		String ret = null;			
		
		try {
			AjusteEstoque ajusteEstoque = (AjusteEstoque)this.entityPlc;
			ajusteEstoque.setStatusAjuste(StatusAjuste.A);
			ret = super.save();
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}

		return ret;		
	}

	public String concluirAjusteEstoque()  {
		AjusteEstoque ajusteEstoque = (AjusteEstoque)this.entityPlc;

		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		RetornoConfig ret = null;
		
		try {
			ret = iocControleFacadeUtil.getFacade(IAppFacade.class).concluirAjusteEstoqueLivros(context, ajusteEstoque);
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}
		
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
		
		if ("ajusteestoque".equalsIgnoreCase(nomeAction)) {

			if (config != null) {
				if (PlcYesNo.S.equals(config.getUtilizaLocalizacaoLivros())) {
					contextUtil.getRequest().setAttribute(AppConstants.TELA_AJUSTE_ESTOQUE.UTILIZA_lOCALIZACAO, PlcConstants.SIM);
				} else {
					contextUtil.getRequest().setAttribute(AppConstants.TELA_AJUSTE_ESTOQUE.UTILIZA_lOCALIZACAO, PlcConstants.NAO);
				}
			}
			
			if (this.entityPlc!=null) {
				AjusteEstoque ajusteEstoque = (AjusteEstoque)this.entityPlc;
				
				if (StatusAjuste.F.equals(ajusteEstoque.getStatusAjuste())) {
					contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.SIM);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_AJUSTE_ESTOQUE, PlcConstants.EXIBIR);
				} else {
					contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.NAO);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_AJUSTE_ESTOQUE, PlcConstants.NAO_EXIBIR);
				}

				if (StatusAjuste.A.equals(ajusteEstoque.getStatusAjuste())) {
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_CONCLUIR_AJUSTE_ESTOQUE, PlcConstants.EXIBIR);
					contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_ABRIR_AJUSTE_ESTOQUE, PlcConstants.NAO_EXIBIR);
				}

				if (StatusAjuste.C.equals(ajusteEstoque.getStatusAjuste())) {
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




