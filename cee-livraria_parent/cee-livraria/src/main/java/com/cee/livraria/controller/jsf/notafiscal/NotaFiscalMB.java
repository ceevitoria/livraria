package com.cee.livraria.controller.jsf.notafiscal;

import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.commons.AppConstants;
import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.compra.Fornecedor;
import com.cee.livraria.entity.compra.FornecedorProduto;
import com.cee.livraria.entity.compra.ItemNotaFiscal;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.compra.StatusNotaFiscal;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.produto.Produto;
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
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;
import com.powerlogic.jcompany.domain.validation.PlcMessage.Cor;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.compra.NotaFiscal.class,
	
	details = { 
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
			clazz = com.cee.livraria.entity.compra.ItemNotaFiscal.class,
			collectionName = "itemNotaFiscal", numNew = 4, onDemand = false),
		
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
			clazz = com.cee.livraria.entity.compra.ContaPagar.class,
			collectionName = "contaPagar", numNew = 4,onDemand = false)
	}
)
	
@PlcConfigForm (
	formPattern=FormPattern.Mdt,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/compra/notafiscal")
)

/**
 * Classe de Controle gerada pelo assistente
 */
 
@SPlcMB
@PlcUriIoC("notafiscal")
@Named("notaFiscalMB")
@PlcHandleException
public class NotaFiscalMB extends AppMB  {
	private static final long serialVersionUID = -7884011930986931317L;

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private Integer indiceItem;
	private String codigoItem;
	private Integer indiceFornecedor;
	private String nomeFornecedor;
	
	private BigDecimal valorTotalNota = new BigDecimal("0.00");
	
	public Integer getIndiceItem() {
		return indiceItem;
	}

	public void setIndiceItem(Integer indiceItem) {
		this.indiceItem = indiceItem;
	}

	public String getCodigoItem() {
		return codigoItem;
	}

	public void setCodigoItem(String codigoItem) {
		this.codigoItem = codigoItem;
	}

	public BigDecimal getValorTotalNota() {
		return valorTotalNota;
	}

	public void setValorTotalNota(BigDecimal valorTotalNota) {
		this.valorTotalNota = valorTotalNota;
	}
	
	public Integer getIndiceFornecedor() {
		return indiceFornecedor;
	}

	public void setIndiceFornecedor(Integer indiceFornecedor) {
		this.indiceFornecedor = indiceFornecedor;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	/**
	* Entidade da ação injetado pela CDI
	*/
	@Produces  @Named("notafiscal")
	public NotaFiscal createEntityPlc() {
		
        if (this.entityPlc==null) {
              this.entityPlc = new NotaFiscal();
              this.newEntity();
              ((NotaFiscal)this.entityPlc).setStatus(StatusNotaFiscal.A);
        }
        
        return (NotaFiscal)this.entityPlc;     	
	}

	@Override
	public String create() {
		String ret = super.create();
        ((NotaFiscal)this.entityPlc).setStatus(StatusNotaFiscal.A);
		return ret;
	}
	
	
	public void buscarProdutoFornecedor() {

		try {
			PlcBaseContextVO context = getContext();
			NotaFiscal notaFiscal = (NotaFiscal)this.entityPlc;
			Fornecedor fornecedor = notaFiscal.getFornecedor();
			
			if (fornecedor == null) {
				msgUtil.msg("{notafiscal.erro.nenhum.fornecedor.selecionado}", Cor.msgVermelhoPlc.toString());					
			} else {
				
				if (indiceItem != null && codigoItem != null) {
					FornecedorProduto entidadeArg =  new FornecedorProduto();
					entidadeArg.setFornecedor(fornecedor);
					entidadeArg.setCodigoProduto(codigoItem);
					
					List<FornecedorProduto> fornecedorProdutos = (List<FornecedorProduto>)iocControleFacadeUtil.getFacade(IAppFacade.class).findList(context, entidadeArg, null, 0, 1);
					
					if (fornecedorProdutos != null && fornecedorProdutos.size() > 0) {
						FornecedorProduto fornecedorProduto = fornecedorProdutos.get(0);
						
						if (fornecedorProduto != null && fornecedorProduto.getProduto() != null) {
							List<ItemNotaFiscal> itens = notaFiscal.getItemNotaFiscal();
							ItemNotaFiscal item = itens.get(indiceItem);
							
							Produto produto = fornecedorProduto.getProduto();
							
							item.setProduto(produto);
							item.setProdutoFornecedorExistente(true);
							
							if (item.getValorUnitario() == null) {
								item.setValorUnitario(produto.getPrecoUltCompra());
							}
							
							if (item.getQuantidade() == null) {
								item.setQuantidade(1);
							}
							
							if (item.getPercentualDesconto() == null) {
								item.setPercentualDesconto(fornecedor.getDescontoPadrao());
							}
							
							BigDecimal valorLiquido = item.getValorUnitario().multiply(new BigDecimal(item.getQuantidade())).multiply((new BigDecimal(100)).subtract(item.getPercentualDesconto()).divide(new BigDecimal(100)));
							item.setValorLiquido(valorLiquido);
						} else {
							List<ItemNotaFiscal> itens = notaFiscal.getItemNotaFiscal();
							ItemNotaFiscal item = itens.get(indiceItem);
							item.setProdutoFornecedorExistente(false);
						}
					}
				}
			}
			
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}
	}
	
	public void buscarParcelamentoFornecedor() {

		try {
			NotaFiscal notaFiscal = (NotaFiscal)this.entityPlc;
			Fornecedor fornecedor = notaFiscal.getFornecedor();
			
			if (fornecedor != null) {
				
				if (fornecedor.getParcelamentoPadrao() != null) {
					notaFiscal.setParcelamento(fornecedor.getParcelamentoPadrao());
				}
				
			} else {
				
				if (fornecedor == null && indiceFornecedor != null ) {
					List<Fornecedor> fornecedores = (List<Fornecedor>)dominioLookupUtil.getDominio("Fornecedor");
					
					fornecedor = fornecedores.get(indiceFornecedor);
					
					if (fornecedor.getParcelamentoPadrao() != null) {
						notaFiscal.setParcelamento(fornecedor.getParcelamentoPadrao());
					}
				}
			}
			
			
		} finally {
			contextUtil.getRequest().setAttribute("destravaTela", "S");
		}
	}

	public String registrarEntradaNotaFiscal() {
		
		save();
		
		PlcBaseContextVO context = getContext();
		
		NotaFiscal notaFiscal = (NotaFiscal)entityPlc;

		RetornoConfig ret = iocControleFacadeUtil.getFacade(IAppFacade.class).registrarEntradaNotaFiscal(context, notaFiscal);
		
		if (ret.getAlertas().size() > 0) {
			
			for (String alerta : ret.getAlertas()) {
				msgUtil.msg(alerta, PlcMessage.Cor.msgAmareloPlc.name());
			}
		}
		
		if (ret.getMensagens().size() > 0) {
			
			for (String mensagem : ret.getMensagens()) {
				msgUtil.msg(mensagem, PlcMessage.Cor.msgAzulPlc.name());
			}
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
		
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_SEM_BARRA);
		
		if ("notafiscal".equalsIgnoreCase(nomeAction)) {
			NotaFiscal notaFiscal = (NotaFiscal)entityPlc;
			
			// Se a nota fiscal ainda está aberta
			if (StatusNotaFiscal.A.equals(notaFiscal.getStatus())) {
				contextUtil.getRequest().setAttribute(AppConstants.ACAO.EXIBE_BT_REGISTRAR_ENTRADA_NOTA_FISCAL, PlcConstants.EXIBIR);
			}
		}
		
		super.handleButtonsAccordingFormPattern();
	}	
}
