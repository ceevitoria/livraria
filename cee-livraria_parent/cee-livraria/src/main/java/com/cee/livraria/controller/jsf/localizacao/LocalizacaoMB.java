package com.cee.livraria.controller.jsf.localizacao;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.cee.livraria.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.PlcEntityList;
import com.powerlogic.jcompany.config.collaboration.FormPattern;



import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigTabular;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
	
@PlcConfigAggregation(entity = com.cee.livraria.entity.LocalizacaoEntity.class)

@PlcConfigForm(
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/localizacao")
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("localizacao")	
@PlcHandleException
public class LocalizacaoMB extends AppMB  {
	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("localizacaoLista") 
	public PlcEntityList createEntityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	
	
}
