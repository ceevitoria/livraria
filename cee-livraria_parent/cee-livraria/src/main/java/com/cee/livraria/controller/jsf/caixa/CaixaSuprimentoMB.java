package com.cee.livraria.controller.jsf.caixa;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.pagamento.FormaPagto;
import com.cee.livraria.entity.pagamento.FormaPagtoEntity;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@PlcConfigAggregation(entity = com.cee.livraria.entity.caixa.CaixaEntity.class)
@PlcConfigForm(
	formPattern = FormPattern.Apl, 
	formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/caixasuprimento")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("caixasuprimento")
@PlcHandleException
public class CaixaSuprimentoMB extends AppMB {

	@Inject @QPlcDefault 
	protected CaixaOperacaoMB caixaOperacaoMB;
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	protected PagamentoList pagamentoList;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("caixasuprimento")
	public CaixaEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new CaixaEntity();
			this.newEntity();
		}

		return (CaixaEntity) this.entityPlc;
	}
	
	@Produces  @Named("pagtoListaSuprimento") 
	public PagamentoList criaListaPagamento() {
		
		if (this.pagamentoList==null) {
    		FormaPagto formaPagto =(FormaPagto) iocControleFacadeUtil.getFacade(IAppFacade.class).findById(getContext(), FormaPagtoEntity.class, 1L);
    		pagamentoList = new PagamentoList();
			List<Pagamento> itens = pagamentoList.getItens();
			Pagamento pagto = null;
			
			for (int i=0; i<2; i++) {
				pagto = new Pagamento();
				
				if (i==0) {
					pagto.setFormaPagto(formaPagto);
				}

				itens.add(pagto);
			}
		}

		return pagamentoList;
	}
	
	public void handleButtonsAccordingFormPattern() {
		caixaOperacaoMB.handleButtonsAccordingFormPattern(this.entityPlc);
	}
	
	/**
	 * Registra a operação de suprimento do caixa
	 * @throws Exception 
	 * @throws PlcException 
	 */
	public String registrarSuprimentoCaixa() throws PlcException, Exception  {
		RetornoConfig ret = null;
		
		try {
			ret = caixaOperacaoMB.registrarOperacaoCaixa(TipoMovimentoCaixa.SU, this.entityPlc, this.pagamentoList.getItens());
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}
		
		Caixa caixa = (Caixa) ret.getObject();

		this.entityPlc = caixa;

		pagamentoList = new PagamentoList();

		List<Pagamento> itens = pagamentoList.getItens();
		
		Pagamento pagto = null;
		
		for (int i=0; i<2; i++) {
			pagto = new Pagamento();
			itens.add(pagto);
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
}
