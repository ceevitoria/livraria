package com.cee.livraria.controller.jsf.caixa;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.commons.AppUserProfileVO;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.StatusCaixa;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.controller.jsf.PlcBaseParentMB;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.util.PlcContextUtil;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.controller.util.PlcMsgUtil;
import com.powerlogic.jcompany.controller.util.PlcURLUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@QPlcDefault
public class CaixaOperacaoMB extends PlcBaseParentMB implements Serializable {

	private static final long serialVersionUID = 1L;

    @Inject @QPlcDefault 
    protected PlcContextUtil contextUtil;

	@Inject @QPlcDefault 
	protected PlcURLUtil urlUtil;

    @Inject @QPlcDefault 
    protected PlcMsgUtil msgUtil;
	
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;
	
	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

    public void handleButtonsAccordingFormPattern(Object entityPlc) {
		Caixa caixa = (Caixa) entityPlc;
		
		contextUtil.getRequest().setAttribute(PlcConstants.ACAO.EXIBE_BT_GRAVAR, PlcConstants.NAO_EXIBIR);
		
		String url = urlUtil.resolveCollaborationNameFromUrl(contextUtil.getRequest());
		
		if (url.contains("abertura")) {
			// Só exisbe o botão para registrar abertura do Caixa se ele estiver fechado
			if (caixa != null && StatusCaixa.F.equals(caixa.getStatus())) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_ABERTURA_CAIXA, PlcConstants.EXIBIR);
			} else {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_ABERTURA_CAIXA, PlcConstants.NAO_EXIBIR);
			}
		} else if (url.contains("sangria")) {
			
			if(!contextUtil.getRequest().isUserInRole("Gestor")) {
				caixa.setSaldo(null);
			}
			
			// Só exisbe o botão para registrar sangria do Caixa se ele estiver aberto
			if (caixa != null && StatusCaixa.A.equals(caixa.getStatus())) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_SANGRIA_CAIXA, PlcConstants.EXIBIR);
			} else {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_SANGRIA_CAIXA, PlcConstants.NAO_EXIBIR);
			}
		} else if (url.contains("suprimento")) {
			
			if(!contextUtil.getRequest().isUserInRole("Gestor")) {
				caixa.setSaldo(null);
			}
			
			// Só exisbe o botão para registrar suprimento do Caixa se ele estiver aberto
			if (caixa != null && StatusCaixa.A.equals(caixa.getStatus())) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_SUPRIMENTO_CAIXA, PlcConstants.EXIBIR);
			} else {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_SUPRIMENTO_CAIXA, PlcConstants.NAO_EXIBIR);
			}
		} else if (url.contains("fechamento")) {
			
			if(!contextUtil.getRequest().isUserInRole("Gestor")) {
				caixa.setSaldo(null);
			}
			
			// Só exisbe o botão para registrar fechamento do Caixa se ele estiver aberto
			if (caixa != null && StatusCaixa.A.equals(caixa.getStatus())) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_FECHAMENTO_CAIXA, PlcConstants.EXIBIR);
			} else {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_FECHAMENTO_CAIXA, PlcConstants.NAO_EXIBIR);
			}
		}
	}

	/**
	 * Registra uma operação no Caixa
	 * @throws Exception 
	 * @throws PlcException 
	 */
	public void registrarOperacaoCaixa(TipoMovimentoCaixa tipo, Object entityPlc) throws PlcException, Exception  {
		RetornoConfig ret = null;
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);
		
		CaixaEntity caixa = (CaixaEntity)entityPlc;

		ret = iocControleFacadeUtil.getFacade(IAppFacade.class).registrarOperacaoCaixa(context, tipo, caixa);
		
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
		
	}
	
}
