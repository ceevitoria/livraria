package com.cee.livraria.controller.jsf.caixa;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.controller.jsf.FileUploadDownloadMB;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.apoio.CaixaConsulta;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.relatorio.RelatorioFechamentoCaixa;
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
import com.powerlogic.jcompany.domain.validation.PlcMessage;

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

	@Inject 
	private FileUploadDownloadMB fileUploadBean;
	
	protected CaixaConsulta caixaConsulta;
	protected PagamentoList pagamentoList;
	protected PagamentoList movimentoList;
	
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
	
	@Produces  @Named("movimentoListaConsulta") 
	public PagamentoList criaListaMovimento() {
		
		if (this.movimentoList==null) {
			List<RelatorioFechamentoCaixa> itens = (List<RelatorioFechamentoCaixa>)iocControleFacadeUtil.getFacade(IAppFacade.class).recuperaDadosFechamentoCaixa(getContext(), null, null, null);
			
			movimentoList = new PagamentoList();
			movimentoList.setItens(itens);
		
		}

		return movimentoList;
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
		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_RELATORIO_FECHAMENTO_CAIXA, PlcConstants.EXIBIR);
   		contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_RELATORIO_FECHAMENTO_CAIXA, PlcConstants.EXIBIR);
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
    }
    
	public void gerarRelatorioFechamentoCaixa() {
		
//		if (this.entityPlc != null) {
			PlcBaseContextVO context = getContext();
			
			byte[] relatorio = iocControleFacadeUtil.getFacade(IAppFacade.class).gerarRelatorioFechamentoCaixa(context, "RelatorioFechamentoCaixa");
			
			if (relatorio != null) {
				fileUploadBean.downloadAbrindoArquivo(relatorio, "RelatorioFechamentoCaixa.xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				msgUtil.msg("Nenhum arquivo gerado!", PlcMessage.Cor.msgAmareloPlc.name());
			}
//		}
	}
    
}
