package com.cee.livraria.controller.jsf.produto;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.produto.Produto.class
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
@PlcUriIoC("produto")
@PlcHandleException
public class ProdutoMB extends AppMB {
	
	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	
	private static final long serialVersionUID = 1L;
     		
	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("produto")
	public Produto createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new Produto();
              this.newEntity();
        }
        return (Produto)this.entityPlc;     	
	}
	
	/**
	 * Recupera um grafo da entidade principal e disponibiliza para o formulario
	 * de visão
	 */
	public String edit()  {
		String result = super.edit();

        if (this.entityPlc!=null) {
    		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

    		Produto produto = (Produto)entityPlc;
        	
        	PrecoTabela preco = iocControleFacadeUtil.getFacade(IAppFacade.class).findPrecoTabela(context, produto.getId());
        	
        	produto.setIdTabela(preco.getIdTabela());
        	produto.setPrecoTabela(preco.getPrecoTabela());
        	produto.setNomeTabela(preco.getNomeTabela());
        }
		
		return result;
	}
	
}
