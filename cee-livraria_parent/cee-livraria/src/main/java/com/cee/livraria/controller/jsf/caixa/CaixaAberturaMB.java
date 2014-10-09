package com.cee.livraria.controller.jsf.caixa;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(entity = com.cee.livraria.entity.caixa.CaixaEntity.class)
@PlcConfigForm(
	formPattern = FormPattern.Apl, 
	formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/caixaabertura")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("caixaabertura")
@PlcHandleException
public class CaixaAberturaMB extends AppMB {

	@Inject @QPlcDefault 
	protected CaixaOperacaoMB caixaOperacaoMB;
	
	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("caixaabertura")
	public CaixaEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new CaixaEntity();
			this.newEntity();
		}

		return (CaixaEntity) this.entityPlc;
	}
	
	public void handleButtonsAccordingFormPattern() {
		caixaOperacaoMB.handleButtonsAccordingFormPattern(this.entityPlc);
	}
	
	/**
	 * Registra a operação de abertura do caixa
	 * @throws Exception 
	 * @throws PlcException 
	 */
	public String registrarAberturaCaixa() throws PlcException, Exception  {
		caixaOperacaoMB.registrarOperacaoCaixa(TipoMovimentoCaixa.AB, this.entityPlc);
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
}
