package com.cee.livraria.controller.jsf.fornecedor;

import javax.enterprise.inject.Produces;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.compra.Fornecedor;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigNestedCombo;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.compra.Fornecedor.class,
	components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(
		clazz = com.cee.livraria.entity.Endereco.class, property="endereco", separate=true)}, 
		details={@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
			clazz = com.cee.livraria.entity.compra.FornecedorContato.class,
			collectionName = "fornecedorContato", numNew = 4, onDemand = false)
	}
)

@PlcConfigForm (
	nestedCombo=@PlcConfigNestedCombo(origemProp="endereco.uf", destinyProp="endereco.cidade"),
	formPattern=FormPattern.Mdt,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/compra/fornecedor")
)

/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("fornecedor")
@PlcHandleException
public class FornecedorMB extends AppMB  {
	private static final long serialVersionUID = 332219642315867770L;

	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("fornecedor")
	public Fornecedor createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new Fornecedor();
              this.newEntity();
        }
        return (Fornecedor)this.entityPlc;     	
	}
		
}
