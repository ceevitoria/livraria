package com.cee.livraria.controller.jsf.config;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.config.ConferenciaConfigEntity;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
	entity= com.cee.livraria.entity.config.ConferenciaConfigEntity.class
)

@PlcConfigForm (
	formPattern=FormPattern.Apl,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/config/conferencia")
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("conferenciaconfig")
@PlcHandleException
public class ConferenciaConfigMB extends AppMB  {

	private static final long serialVersionUID = 1L;
	
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("conferenciaconfig")
	public ConferenciaConfigEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new ConferenciaConfigEntity();
              this.newEntity();
        }
        
        return (ConferenciaConfigEntity)this.entityPlc;     	
	}
		
}
