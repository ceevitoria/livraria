package com.cee.livraria.controller.jsf.parcelamento;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(entity = com.cee.livraria.entity.compra.Parcelamento.class)
@PlcConfigForm(

formPattern = FormPattern.Tab, tabular = @PlcConfigTabular(numNew = 4), formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/compra"))
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("parcelamento")
@PlcHandleException
public class ParcelamentoMB extends AppMB {

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("parcelamentoLista")
	public PlcEntityList createEntityListPlc() {
		if (this.entityListPlc == null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}

}
