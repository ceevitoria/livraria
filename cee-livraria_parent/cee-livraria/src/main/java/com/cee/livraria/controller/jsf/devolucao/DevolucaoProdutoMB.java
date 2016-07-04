package com.cee.livraria.controller.jsf.devolucao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.apoio.DevolucaoProduto;
import com.cee.livraria.entity.estoque.apoio.VendaProduto;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
	
@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.apoio.DevolucaoProduto.class)

@PlcConfigForm(
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), 
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/devolucao")
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("devolucaoproduto")	
@Named("devolucaoProdutoMB")
@PlcHandleException
public class DevolucaoProdutoMB extends AppMB  {
	private static final long serialVersionUID = 1514409886499403257L;

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private BigDecimal valorTotal;

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}
	
	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("devolucaoprodutoLista") 
	public PlcEntityList createEntityListPlc() {
		criaNovaLista();
		return this.entityListPlc;
	}	

	public String search()  {
		novosItens();

		msgUtil.msg("Devalução somente em dinheiro", PlcMessage.Cor.msgAzulPlc.name());
		
		return baseSearchMB.getDefaultNavigationFlow(); 
	}

	private void novosItens() {
		List itens = new ArrayList<Object>();
		
		DevolucaoProduto cl = null;
		
		for (int i=0; i<4; i++) {
			cl = new DevolucaoProduto();
			itens.add(cl);
		}
		
		this.entityListPlc.setItensPlc(itens);
	}

	private void criaNovaLista() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
	}
	
	/**
	 * Limpa todos os argumentos da pesquisa.
	 */
	@Override
	public String clearArgs()  {
		return super.clearArgs();
	}
	
	public void handleButtonsAccordingFormPattern() {
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.NAO_EXIBIR);
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.EXIBIR);
		
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_LIMPAR, PlcConstants.EXIBIR);
		
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_NOVO, PlcConstants.EXIBIR);
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_BUSCAR_PRODUTO, PlcConstants.EXIBIR);
		
		if (valorTotal != null && valorTotal.doubleValue() > 0.01) {
			contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_DEVOLUCAO, PlcConstants.EXIBIR);
		}
		
	}
	
	public void limparOperacaoAnterior()  {
		limpaItens(entityListPlc.getItensPlc());
		valorTotal = new BigDecimal("0.00");
	}

	private void limpaItens(List itens) {

		for (Object o : itens) {
			DevolucaoProduto vl = (DevolucaoProduto)o;
			vl.setId(null);
			vl.setProduto(null);
			vl.setTipoProduto(null);
			vl.setNomeTabela(null);
			vl.setQuantidade(null);
			vl.setValorUnitario(null);
			vl.setValorTotal(null);
		}
		
		if (itens.size() > 4) {
			
			for(int i = itens.size()-1; i > 3; i--) {
				itens.remove(i);
			}
		}
	}

	private void limpaItensSemProdutos(List itens) {

		for (Object o : itens) {
			DevolucaoProduto dp = (DevolucaoProduto)o;
			
			if (dp.getProduto() == null || dp.getProduto().getCodigoBarras() == null || "".compareTo(dp.getProduto().getCodigoBarras().trim()) == 0) {
				dp.setId(null);
				dp.setProduto(null);
				dp.setNomeTabela(null);
				dp.setQuantidade(null);
				dp.setValorUnitario(null);
				dp.setValorTotal(null);
			}
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes"})
	public String registrarDevolucaoProdutos()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		List itensPlc = entityListPlc.getItensPlc();

		RetornoConfig ret = null;
		
		try {
			ret = iocControleFacadeUtil.getFacade(IAppFacade.class).registrarDevolucaoProdutos(context, itensPlc);
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
				msgUtil.msg(mensagem, PlcMessage.Cor.msgAzulPlc.name());
			}
		}
		
		limparOperacaoAnterior();
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buscarDadosProdutos()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		List itens = entityListPlc.getItensPlc();

		valorTotal = iocControleFacadeUtil.getFacade(IAppFacade.class).buscarDadosDevolucaoProdutos(context, itens);
		
		limpaItensSemProdutos(itens);
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}

}
