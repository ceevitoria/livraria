package com.cee.livraria.controller.jsf.compravendaconfig;

import javax.enterprise.inject.Produces;
import javax.inject.Named;


import com.cee.livraria.entity.config.CompraVendaConfigEntity;
import com.cee.livraria.controller.jsf.AppMB;

import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.config.collaboration.FormPattern;

import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;




import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;

@PlcConfigAggregation(
	entity= com.cee.livraria.entity.config.CompraVendaConfigEntity.class
)

@PlcConfigForm (
	formPattern=FormPattern.Apl,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/config/compravenda")
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("compravendaconfig")
@PlcHandleException
public class CompraVendaConfigMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("compravendaconfig")
	public CompraVendaConfigEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new CompraVendaConfigEntity();
              this.newEntity();
        }
        
        return (CompraVendaConfigEntity)this.entityPlc;     	
	}
		
}
