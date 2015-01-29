package com.cee.livraria.controller.jsf.caixa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.apoio.CaixaConsulta;
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
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@PlcConfigAggregation(entity = com.cee.livraria.entity.caixa.apoio.CaixaConsulta.class)

@PlcConfigForm (
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), 
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/caixaconsulta")
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("caixaconsulta")
@PlcHandleException
public class CaixaConsultaMB extends AppMB  {

	private static final long serialVersionUID = 1L;
     		
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;
	
	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	protected CaixaConsulta caixaConsulta;
	protected PagamentoList pagamentoList;
	
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  
	@Named("caixaconsulta")
	public CaixaConsulta criaCaixaConsulta() {
        if (this.caixaConsulta==null) {
        	
    		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
    		
    		Caixa caixa = new CaixaEntity();
    		caixa.setSistema("LIV");

    		List<Caixa> caixaList = (List<Caixa>)iocControleFacadeUtil.getFacade(IAppFacade.class).findList(context, caixa, "", 0, 0);
    		
    		caixa = (Caixa)caixaList.get(0);
    		
    		caixaConsulta = new CaixaConsulta();
    		
    		caixaConsulta.setStatus(caixa.getStatus());
    		
    		if(contextUtil.getRequest().isUserInRole("Gestor")) {
    			caixaConsulta.setSaldo(caixa.getSaldo());
    		}
    		
        }
        
        return caixaConsulta;     	
	}
	
	@Produces  @Named("pagtoListaConsulta") 
	public PagamentoList criaListaPagamento() {
		if (this.pagamentoList==null) {

			
    		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
    		
    		Caixa caixa = new CaixaEntity();
    		caixa.setSistema("LIV");

    		if(contextUtil.getRequest().isUserInRole("Gestor")) {
    			List<Caixa> caixaList = (List<Caixa>)iocControleFacadeUtil.getFacade(IAppFacade.class).findList(context, caixa, "", 0, 0);
    			
    			caixa = (Caixa)caixaList.get(0);
    			
    			this.entityPlc = new CaixaConsulta();
    			
    			((CaixaConsulta)this.entityPlc).setStatus(caixa.getStatus());
    			((CaixaConsulta)this.entityPlc).setSaldo(caixa.getSaldo());
    			
    			pagamentoList = (PagamentoList)iocControleFacadeUtil.getFacade(IAppFacade.class).obterPagamentosCaixa(context, caixa);
    		} else {
        		pagamentoList = new PagamentoList();

    			List<Pagamento> itens = pagamentoList.getItens();
    			
    			Pagamento pagto = null;
    			
    			for (int i=0; i<2; i++) {
    				pagto = new Pagamento();
    				itens.add(pagto);
    			}
    		}
    		
		}

		return pagamentoList;
	}
	
	public String search()  {
		novosItens();

		return baseSearchMB.getDefaultNavigationFlow(); 
	}

	private void novosItens() {
		List itens = new ArrayList<Object>();
		
		Pagamento pagto = null;
		
		for (int i=0; i<4; i++) {
			pagto = new Pagamento();
			itens.add(pagto);
		}
		
		this.entityListPlc.setItensPlc(itens);
	}

	
    public void handleButtonsAccordingFormPattern() {
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
    }
}