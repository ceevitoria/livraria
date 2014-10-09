package com.cee.livraria.controller.jsf.vendalivro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.CompraVendaConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.apoio.VendaLivro;
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
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
	
@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.apoio.VendaLivro.class)

@PlcConfigForm(
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), 
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/vender")
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("vendalivro")	
@PlcHandleException
public class VendaLivroMB extends AppMB  {

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private static final long serialVersionUID = 1L;

	private BigDecimal valorTotalGeral;

	@Produces @Named("valorTotalGeral")
	public BigDecimal getValorTotalGeral() {
		return valorTotalGeral;
	}

	public void setValorTotalGeral(BigDecimal valorTotalGeral) {
		this.valorTotalGeral = valorTotalGeral;
	}

	
	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("vendalivroLista") 
	public PlcEntityList createEntityListPlc() {
		criaNovaLista();
		return this.entityListPlc;
	}	

	public String search()  {
		novosItens();

		return baseSearchMB.getDefaultNavigationFlow(); 
	}

	private void novosItens() {
		List itens = new ArrayList<Object>();
		
		VendaLivro cl = null;
		
		for (int i=0; i<4; i++) {
			cl = new VendaLivro();
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
	
	public void handleButtonsAccordingFormPattern() {
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_EXCLUIR, PlcConstants.NAO_EXIBIR);
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_INCLUIR, PlcConstants.EXIBIR);
		
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_LIMPAR, PlcConstants.EXIBIR);
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_NOVO, PlcConstants.EXIBIR);
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_BUSCAR_VENDA, PlcConstants.EXIBIR);
		
		if (valorTotalGeral != null && valorTotalGeral.doubleValue() > 0.0) {
			contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_VENDA, PlcConstants.EXIBIR);
		}
	}
	
	public String limparVendaAnterior()  {
		List itens = entityListPlc.getItensPlc();

		limpaItens(itens);
	
		this.setValorTotalGeral(null);
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}

	/**
	 * 
	 */
	private void limpaItens(List itens) {

		for (Object o : itens) {
			VendaLivro vl = (VendaLivro)o;
			vl.setId(null);
			vl.setLivro(null);
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

	private void limpaItensSemLivros(List itens) {

		for (Object o : itens) {
			VendaLivro vl = (VendaLivro)o;
			
			if (vl.getLivro() == null || vl.getLivro().getCodigoBarras() == null || "".compareTo(vl.getLivro().getCodigoBarras().trim()) == 0) {
				vl.setId(null);
				vl.setLivro(null);
				vl.setNomeTabela(null);
				vl.setQuantidade(null);
				vl.setValorUnitario(null);
				vl.setValorTotal(null);
			}
		}
	}

	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String registrarVendaLivros()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		List itens = entityListPlc.getItensPlc();

		RetornoConfig ret = iocControleFacadeUtil.getFacade(IAppFacade.class).registrarVendaLivros(context, itens);
		
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
		
		CompraVendaConfig config = (CompraVendaConfig)ret.getConfig();
		
		if (config.getAutoLimparTelaParaNovaVenda().equals(PlcYesNo.S)) {
			limpaItens(itens);
		}
		
		this.setValorTotalGeral(null);
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buscarDadosVendaLivros()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		List itens = entityListPlc.getItensPlc();
		entityListPlc.setItensPlc(itens);

		valorTotalGeral = iocControleFacadeUtil.getFacade(IAppFacade.class).buscarDadosVendaLivros(context, itens);

		limpaItensSemLivros(itens);
		
//		msgUtil.msg("{vendaLivro.ok.buscar}", PlcMessage.Cor.msgAzulPlc.name());
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
}
