package com.cee.livraria.controller.jsf.autor;

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
	
@PlcConfigAggregation(entity = com.cee.livraria.entity.AutorEntity.class)

@PlcConfigForm(
	
	formPattern=FormPattern.Tab,
	tabular = @PlcConfigTabular(numNew = 4), formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/autor")
	)





/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("autor")	
@PlcHandleException
public class AutorMB extends AppMB  {

	private static final long serialVersionUID = 1L;

	

	/**
	 * Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("autorLista") 
	public PlcEntityList createEntityListPlc() {
		if (this.entityListPlc==null) {
			this.entityListPlc = new PlcEntityList();
			this.newObjectList();
		}
		return this.entityListPlc;
	}	
	
	
}
