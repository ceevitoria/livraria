package com.cee.livraria.controller.jsf.relatorio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.controller.jsf.FileUploadDownloadMB;
import com.cee.livraria.entity.estoque.apoio.VendaProduto;
import com.cee.livraria.entity.relatorio.RelatorioUltimasComprasPeriodo;
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
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.relatorio.RelatorioUltimasComprasPeriodo.class
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
@PlcUriIoC("relatorioultimascomprasperiodo")
@Named("relatorioUltimasComprasPeriodoMB")
public class RelatorioUltimasComprasPeriodoMB extends AppMB {
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
	@Named("relatorioUltimasComprasPeriodo")
	public RelatorioUltimasComprasPeriodo createEntityPlc() {
		
		if (this.entityPlc == null) {
			this.entityPlc = new RelatorioUltimasComprasPeriodo();
			this.newEntity();
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);

			cal.add(Calendar.MONTH, -2);
			
			RelatorioUltimasComprasPeriodo relatorioUltimasComprasPeriodo = (RelatorioUltimasComprasPeriodo)this.entityPlc;
			relatorioUltimasComprasPeriodo.setDataInicio(cal.getTime());
			
			cal.add(Calendar.MONTH, 2);
			
			relatorioUltimasComprasPeriodo.setDataFim(cal.getTime());
		}
		
		return (RelatorioUltimasComprasPeriodo) this.entityPlc;
	}
	
	public void gerarRelatorioUltimasComprasPeriodo() {
		
		if (this.entityPlc != null) {
			PlcBaseContextVO context = getContext();
			
			RelatorioUltimasComprasPeriodo relatorioArg = (RelatorioUltimasComprasPeriodo) this.entityPlc;
			
			if (relatorioArg.getDataFim() == null || relatorioArg.getDataInicio() == null) {
				throw new PlcException("{relatorio.ultimasComprasPeriodo.erro.peridoInvalido}");
			}
			
			if (relatorioArg.getDataFim().before(relatorioArg.getDataInicio())) {
				throw new PlcException("{relatorio.ultimasComprasPeriodo.erro.dataFimMenorDataInicio}");
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(relatorioArg.getDataFim());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.SECOND, -1); // Obtem o último horário do período final. Ex: 23:59:59
			relatorioArg.setDataFim(cal.getTime());
			
			byte[] relatorio = iocControleFacadeUtil.getFacade(IAppFacade.class).gerarRelatorioUltimasComprasPeriodo(context, relatorioArg, "RelatorioUltimasComprasPeriodo");
			
			if (relatorio != null) {
				String nomeArquivo = String.format("RelatorioUltimasComprasPeriodo-%tY%tm%td-%tY%tm%td.xlsx", relatorioArg.getDataInicio(), relatorioArg.getDataInicio(), relatorioArg.getDataInicio(), relatorioArg.getDataFim(), relatorioArg.getDataFim(), relatorioArg.getDataFim());

				fileUploadBean.downloadAbrindoArquivo(relatorio, nomeArquivo, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				msgUtil.msg("Nenhum arquivo gerado!", PlcMessage.Cor.msgAmareloPlc.name());
			}
		}
	}
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA);
		
		if ("relatorioUltimasComprasPeriodo".equalsIgnoreCase(nomeAction)) {
			contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_GERAR_RELATORIO_ULTIMAS_COMPRAS_PERIODO, PlcConstants.EXIBIR);
		}
		
		super.handleButtonsAccordingFormPattern();
	}

	
}
