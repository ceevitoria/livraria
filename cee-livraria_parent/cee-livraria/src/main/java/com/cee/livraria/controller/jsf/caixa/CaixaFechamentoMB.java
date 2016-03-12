package com.cee.livraria.controller.jsf.caixa;

import java.util.Date;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.controller.jsf.FileUploadDownloadMB;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.pagamento.FormaPagProduto;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
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
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(entity = com.cee.livraria.entity.caixa.CaixaEntity.class)
@PlcConfigForm(
	formPattern = FormPattern.Apl, 
	formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/caixafechamento")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("caixafechamento")
@Named("caixaFechamentoMB")
@PlcHandleException
public class CaixaFechamentoMB extends AppMB {
	private static final long serialVersionUID = 2738743456450689167L;

	@Inject @QPlcDefault 
	protected CaixaOperacaoMB caixaOperacaoMB;
	
	protected PagamentoList pagamentoList;
	
	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	@Inject 
	private FileUploadDownloadMB fileUploadBean;
	
	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("caixafechamento")
	public CaixaEntity createEntityPlc() {

		if (this.entityPlc == null) {
			this.entityPlc = new CaixaEntity();
			this.newEntity();
		}

		return (CaixaEntity) this.entityPlc;
	}
	
	@SuppressWarnings("unchecked")
	@Produces  @Named("pagtoListaFechamento") 
	public PagamentoList criaListaPagamento() {
		
		if (this.pagamentoList==null) {
    		pagamentoList = new PagamentoList();

			List<Pagamento> itens = pagamentoList.getItens();
			
			Pagamento pagto = null;
			
			List<FormaPagProduto> formasPagProduto = caixaOperacaoMB.recuperarFormasPagamentoProduto();
			
			for (FormaPagProduto formaPagProduto : formasPagProduto) {
				
				if (PlcYesNo.S.equals(formaPagProduto.getIsGeraCaixa())) {
					pagto = new Pagamento();
//					pagto.setFormaPagto(formaPagProduto.getFormaPagto());
					itens.add(pagto);
				}
			}
		}

		return pagamentoList;
	}
	
	public void handleButtonsAccordingFormPattern() {
		caixaOperacaoMB.handleButtonsAccordingFormPattern(this.entityPlc);
	}
	
	/**
	 * Registra a operação de fechamento do caixa
	 * @throws Exception 
	 * @throws PlcException 
	 */
	public String registrarFechamentoCaixa() throws PlcException, Exception  {
		RetornoConfig ret = caixaOperacaoMB.registrarOperacaoCaixa(TipoMovimentoCaixa.FE, this.entityPlc, this.pagamentoList.getItens());
		
		Caixa caixa = (Caixa) ret.getObject();

		this.entityPlc = caixa;
		
		pagamentoList = new PagamentoList();

		List<Pagamento> itens = pagamentoList.getItens();
		
		Pagamento pagto = null;
		
		List<FormaPagProduto> formasPagProduto = caixaOperacaoMB.recuperarFormasPagamentoProduto();
		
		for (FormaPagProduto formaPagProduto : formasPagProduto) {
			
			if (PlcYesNo.S.equals(formaPagProduto.getIsGeraCaixa())) {
				pagto = new Pagamento();
				pagto.setFormaPagto(formaPagProduto.getFormaPagto());
				itens.add(pagto);
			}
		}

		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	public void gerarRelatorioFechamentoCaixa() {
		
		if (this.entityPlc != null) {
			PlcBaseContextVO context = getContext();
			
			byte[] relatorio = iocControleFacadeUtil.getFacade(IAppFacade.class).gerarRelatorioFechamentoCaixa(context, "RelatorioFechamentoCaixa");
			
			if (relatorio != null) {
				Date data = new Date();
				String nomeArquivo = String.format("RelatorioFechamentoCaixa-%tY%tm%td.xlsx", data, data, data);
				fileUploadBean.downloadAbrindoArquivo(relatorio, nomeArquivo, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
			} else {
				msgUtil.msg("Nenhum arquivo gerado!", PlcMessage.Cor.msgAmareloPlc.name());
			}
		}
	}
	
}
