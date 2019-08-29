package com.cee.livraria.controller.jsf.relatorio;

import java.util.Date;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.controller.jsf.FileUploadDownloadMB;
import com.cee.livraria.entity.relatorio.RelatorioEstoque;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigExport;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.relatorio.RelatorioEstoque.class
)

@PlcConfigForm(
	formPattern = FormPattern.Con, 
	selection = @PlcConfigSelection(apiQuerySel = "querySel", export=@PlcConfigExport(useExport=true)),
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/relatorio")
)

/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcHandleException
@PlcUriIoC("relatorioestoque")
@Named("relatorioEstoqueMB")
public class RelatorioEstoqueMB extends AppMB {
	private static final long serialVersionUID = -2223490700190936276L;

	@Inject
	@QPlcDefault
	protected PlcBeanPopulateUtil beanPopulateUtil;

	@Inject 
	private FileUploadDownloadMB fileUploadBean;
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("relatorioestoque")
	public RelatorioEstoque createEntityPlc() {
		
		if (this.entityPlc == null) {
			this.entityPlc = new RelatorioEstoque();
			this.newEntity();
			
		}
		
		return (RelatorioEstoque) this.entityPlc;
	}
	
	public void gerarRelatorioEstoque() {
		
		if (this.entityPlc != null) {
			Date dataAtual = new Date();
			PlcBaseContextVO context = getContext();
			
			RelatorioEstoque relatorioArg = (RelatorioEstoque) this.entityPlc;
			
			if (relatorioArg.getDataRelatorio() == null) {
				relatorioArg.setDataRelatorio(dataAtual);
			}
			
			byte[] relatorio = iocControleFacadeUtil.getFacade(IAppFacade.class).gerarRelatorioEstoque(context, relatorioArg, "RelatorioEstoque");
			
			if (relatorio != null) {
				
				String nomeArquivo = String.format("RelatorioEstoque-%tY%tm%td.xlsx", dataAtual, dataAtual, dataAtual, dataAtual);

				fileUploadBean.downloadAbrindoArquivo(relatorio, nomeArquivo, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				msgUtil.msg("Nenhum arquivo gerado!", PlcMessage.Cor.msgAmareloPlc.name());
			}
		}
	}
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA);
		
		if ("relatorioEstoque".equalsIgnoreCase(nomeAction)) {
			contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_GERAR_RELATORIO_ESTOQUE, PlcConstants.EXIBIR);
		}
		
		super.handleButtonsAccordingFormPattern();
	}
	
}
