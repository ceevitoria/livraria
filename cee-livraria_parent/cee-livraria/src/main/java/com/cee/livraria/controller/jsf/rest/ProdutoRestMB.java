package com.cee.livraria.controller.jsf.rest;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.rest.ProdutoRest;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.rest.ProdutoRest.class
)

@PlcConfigForm (
	formPattern=FormPattern.Man,
	selection=@PlcConfigSelection(apiQuerySel="querySel"),
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/produto"),
	behavior = @com.powerlogic.jcompany.config.collaboration.PlcConfigBehavior(batchInput=false)
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("produtorest")
@PlcHandleException
public class ProdutoRestMB extends AppMB  {
	private static final long serialVersionUID = 1L;
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("produtorest")
	public ProdutoRest createEntityPlc() {
		
        if (this.entityPlc==null) {
              this.entityPlc = new ProdutoRest();
              this.newEntity();
        }
        
        return (ProdutoRest)this.entityPlc;     	
	}
	
}
