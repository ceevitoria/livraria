package com.cee.livraria.controller.jsf.vendaproduto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.CompraVendaConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.apoio.VendaProduto;
import com.cee.livraria.entity.pagamento.FormaPagto;
import com.cee.livraria.entity.pagamento.FormaPagtoEntity;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.pagamento.PagamentoList;
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
	
@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.apoio.VendaProduto.class)

@PlcConfigForm(
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), 
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/vender")
)


/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("vendaproduto")	
@PlcHandleException
public class VendaProdutoMB extends AppMB  {

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private static final long serialVersionUID = 1L;

	protected PagamentoList pagamentoList;
	
	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("pagamentoLista") 
	public PagamentoList createPagamentoList() {
		criaNovaListaPagamento();
		
		return this.pagamentoList;
	}	
	
	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("vendaprodutoLista") 
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
		
		VendaProduto cl = null;
		
		for (int i=0; i<4; i++) {
			cl = new VendaProduto();
			itens.add(cl);
		}
		
		this.entityListPlc.setItensPlc(itens);
	}

	private void criaNovaListaPagamento() {
		
		if (this.pagamentoList==null) {
			this.pagamentoList = new PagamentoList();
			
			List<Pagamento> itens = pagamentoList.getItens();
			
			Pagamento pagto = null;
			
			for (int i=0; i<2; i++) {
				pagto = new Pagamento();
				itens.add(pagto);
			}
			
			this.pagamentoList.setItens(itens);
		}
		
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
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_BUSCAR_PRODUTO, PlcConstants.EXIBIR);
		
		if (pagamentoList != null && pagamentoList.getItens() != null) {
			Pagamento pagto = (Pagamento)pagamentoList.getItens().get(0);
			
			if (pagto != null && pagto.getValor() != null  && pagto.getValor().doubleValue() > 0.0) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_VENDA, PlcConstants.EXIBIR);
			}
		}
	}
	
	private void limparOperacaoAnterior()  {
		limpaItens(entityListPlc.getItensPlc());
		limpaFormasPagto(pagamentoList.getItens());
	}

	private void limpaItens(List itens) {

		for (Object o : itens) {
			VendaProduto vl = (VendaProduto)o;
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

	private void limpaFormasPagto(List itens) {
		for (Object o : itens) {
			Pagamento vl = (Pagamento)o;
			vl.setValor(null);
			vl.setFormaPagto(null);
		}
	}

	private void limpaItensSemProdutos(List itens) {

		for (Object o : itens) {
			VendaProduto vl = (VendaProduto)o;
			
			if (vl.getProduto() == null || vl.getProduto().getCodigoBarras() == null || "".compareTo(vl.getProduto().getCodigoBarras().trim()) == 0) {
				vl.setId(null);
				vl.setProduto(null);
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
	public String registrarVendaProdutos()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		List itensPlc = entityListPlc.getItensPlc();
		List itensPagamento = pagamentoList.getItens();

		RetornoConfig ret = null;
		
		try {
			ret = iocControleFacadeUtil.getFacade(IAppFacade.class).registrarVendaProdutos(context, itensPlc, itensPagamento);
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
		
		CompraVendaConfig config = (CompraVendaConfig)ret.getConfig();
		
		if (config.getAutoLimparTelaParaNovaVenda().equals(PlcYesNo.S)) {
			limparOperacaoAnterior();
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	/**
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String buscarDadosProdutos()  {
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		List itens = entityListPlc.getItensPlc();

		BigDecimal valorTotal = iocControleFacadeUtil.getFacade(IAppFacade.class).buscarDadosVendaProdutos(context, itens);
		
		Collection formasPagto = iocControleFacadeUtil.getFacade(IAppFacade.class).findSimpleList(context, FormaPagtoEntity.class, "id");
		
		//Setar o primeiro item para o pagamento integral
		FormaPagto formaPagto = (FormaPagto)formasPagto.iterator().next();
		Pagamento pagto = (Pagamento)pagamentoList.getItens().get(0);
		BigDecimal valorPrimeiraParcela = pagto.getValor();
		
		if (valorPrimeiraParcela == null || pagto.getAutomatico() == null || pagto.getAutomatico().booleanValue() == true) {
			pagto.setValor(valorTotal);
			// pagto.setFormaPagto(formaPagto);
			pagto.setAutomatico(true);
		} else {
			pagto = (Pagamento)pagamentoList.getItens().get(1);
			BigDecimal valorSegundaParcela = valorTotal.subtract(valorPrimeiraParcela);
			formaPagto = (FormaPagto)formasPagto.iterator().next();
			pagto.setValor(valorSegundaParcela);
			// pagto.setFormaPagto(formaPagto);
			pagto.setAutomatico(true);
		}
		
		limpaItensSemProdutos(itens);
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
}
