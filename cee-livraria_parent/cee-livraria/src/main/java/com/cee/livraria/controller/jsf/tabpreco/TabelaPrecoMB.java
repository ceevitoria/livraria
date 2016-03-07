package com.cee.livraria.controller.jsf.tabpreco;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.produto.CD;
import com.cee.livraria.entity.produto.DVD;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.cee.livraria.entity.produto.TipoProduto;
import com.cee.livraria.entity.tabpreco.FontePrecificacao;
import com.cee.livraria.entity.tabpreco.ItemTabela;
import com.cee.livraria.entity.tabpreco.ItemTabelaEntity;
import com.cee.livraria.entity.tabpreco.TabelaPreco;
import com.cee.livraria.entity.tabpreco.TabelaPrecoEntity;
import com.cee.livraria.entity.tabpreco.TipoArredondamento;
import com.cee.livraria.entity.tabpreco.TipoPrecificacao;
import com.cee.livraria.entity.tabpreco.TipoVariacao;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(entity = com.cee.livraria.entity.tabpreco.TabelaPrecoEntity.class, 
	components = { @com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(clazz = 
		com.cee.livraria.entity.produto.RegraPesquisaProdutos.class, property = "regra", separate = true) }, details = { 
	@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.cee.livraria.entity.tabpreco.ItemTabelaEntity.class, 
			collectionName = "itemTabela", numNew = 0, onDemand = false, exclusionMode = ExclusionMode.LOGICAL, 
			navigation = @com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail(numberByPage = 30)) })

@PlcConfigForm(formPattern = FormPattern.Mdt, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/tabpreco"), exclusionMode = ExclusionMode.LOGICAL)

/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("tabpreco")
@PlcHandleException
public class TabelaPrecoMB extends AppMB {

	@Inject
	@QPlcDefault
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject
	@Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;

	@Inject
	@QPlcDefault
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("tabpreco")
	public TabelaPrecoEntity createEntityPlc() {

		if (this.entityPlc == null) {
			this.entityPlc = new TabelaPrecoEntity();
			this.newEntity();
		}
		
		return (TabelaPrecoEntity) this.entityPlc;
	}

	/**
	 * buscarItensPorRegraPrecificacao
	 */
	public String buscarItensPorRegra() {

		if (this.entityPlc != null) {
			TabelaPreco tabPreco = (TabelaPreco) this.entityPlc;
			List<ItemTabela> listaItens = tabPreco.getItemTabela();
			PlcBaseContextVO context = contextMontaUtil	.createContextParam(plcControleConversacao);

			// Busca os livros que satisfazem às regras de pesquisa e os adiciona na listaItens
			RegraPesquisaProdutos regra = tabPreco.getRegra();
			Produto produtoArg = criaArgumentoPesquisaProduto(regra);

			Long contaProdutos = (Long) iocControleFacadeUtil.getFacade().findCount(context, produtoArg);

			if (contaProdutos.longValue() > 400) {
				msgUtil.msg("{tabpreco.err.buscar.muitosItensExistentes}", new Object[] { contaProdutos }, PlcMessage.Cor.msgVermelhoPlc.name());
			} else {
				Comparator<ItemTabela> comparator = new Comparator<ItemTabela>() {
					public int compare(ItemTabela o1, ItemTabela o2) {
						if (o1.getCodigoBarras().equals(o2.getCodigoBarras())) {
							return o1.getProduto().getId().compareTo(o2.getProduto().getId());
						}
						return o1.getCodigoBarras().compareTo(o2.getCodigoBarras());
					}
				};

				Collections.sort(listaItens, comparator); 

				List<Produto> produtos = (List<Produto>) iocControleFacadeUtil.getFacade().findList(context, produtoArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
				List<Estoque> estoques = null;
				
				if (regra.getLocalizacao() == null) {
					estoques = (List<Estoque>) iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoque(context, produtos);
				} else {
					estoques = (List<Estoque>) iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoquePorLocalizacao(context, produtos, regra.getLocalizacao());
				}
				
				for (Estoque itemEstoque : estoques) {
					ItemTabela itemTabela = criaNovoItem(tabPreco, itemEstoque);
					double preco = calcularAjustePrecoProduto(tabPreco, itemEstoque.getProduto());
					itemTabela.setPreco(new BigDecimal(preco));

					int i = Collections.binarySearch(listaItens, itemTabela, comparator);

					if (i < 0) {
						listaItens.add(itemTabela);
					} else {
						itemTabela = listaItens.get(i);
						itemTabela.setPreco(new BigDecimal(preco));
					}
				}

				msgUtil.msg("{tabpreco.ok.buscar}",	new Object[] {estoques.size()}, PlcMessage.Cor.msgAzulPlc.name());
				msgUtil.msg("{tabpreco.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());

				plcControleConversacao.setAlertaAlteracaoPlc("S");
			}
		}

		return baseEditMB.getDefaultNavigationFlow();
	}

	private Produto criaArgumentoPesquisaProduto(RegraPesquisaProdutos regra) throws PlcException {
		Produto produtoArg = null;

		if (regra.getTipoProduto() == null) {
			throw new PlcException("{erro.regra.informar.tipoProduto}");
		}
		
		if (TipoProduto.L.equals(regra.getTipoProduto())) {
			produtoArg = new Livro();
			((Livro) produtoArg).setAutor(regra.getAutor());
			((Livro) produtoArg).setColecao(regra.getColecao());
			((Livro) produtoArg).setEdicao(regra.getEdicao());
			((Livro) produtoArg).setEditora(regra.getEditora());
			((Livro) produtoArg).setEspirito(regra.getEspirito());
		} else if (TipoProduto.C.equals(regra.getTipoProduto())) {
			produtoArg = new CD();
			((CD) produtoArg).setArtista(regra.getArtista());
			((CD) produtoArg).setGravadora(regra.getGravadora());
		} else if (TipoProduto.D.equals(regra.getTipoProduto())) {
			produtoArg = new DVD();
			((DVD) produtoArg).setArtista(regra.getArtista());
			((DVD) produtoArg).setGravadora(regra.getGravadora());
		} else {
			produtoArg = new Produto();
		}

		produtoArg.setTitulo(regra.getTitulo());
		produtoArg.setCodigoBarras(regra.getCodigoBarras());
		
		return produtoArg;
	}

	private double calcularAjustePrecoProduto(TabelaPreco tabPreco, Produto produto) {
		double variacao = 0;
		double preco = 0;
		double fator = 0;

		variacao = tabPreco.getVariacao().doubleValue();

		// Recupera o preço segundo a fonte apropriada
		if (FontePrecificacao.PV.equals(tabPreco.getFontePrecificacao())) {

			if (produto.getPrecoVendaSugerido() != null) {
				preco = produto.getPrecoVendaSugerido().doubleValue();
			} else {
				preco = 0;
			}

		} else if (FontePrecificacao.PC.equals(tabPreco.getFontePrecificacao())) {

			if (produto.getPrecoUltCompra() != null) {
				preco = produto.getPrecoUltCompra().doubleValue();
			} else {
				preco = 0;
			}

		}

		if (tabPreco.getTipoPrecificacao().equals(TipoPrecificacao.A)) {

			if (tabPreco.getTipoVariacao().equals(TipoVariacao.V)) {
				preco += variacao;
			} else {
				preco *= (1 + (variacao / 100.0));
			}
		} else {

			if (tabPreco.getTipoVariacao().equals(TipoVariacao.V)) {
				preco -= variacao;
			} else {
				preco *= (1 - (variacao / 100.0));
			}
		}

		// Arredondamentos
		if (TipoArredondamento.DC.equals(tabPreco.getTipoArredondamento())) {
			fator = 10;
		} else if (TipoArredondamento.UN.equals(tabPreco.getTipoArredondamento())) {
			fator = 1;
		} else if (TipoArredondamento.DE.equals(tabPreco.getTipoArredondamento())) {
			fator = 0.1;
		}

		preco *= fator;
		preco = Math.round(preco);
		preco /= fator;
		return preco;
	}

	/**
	 * atualizarItensPorPrecificacao
	 */
	public String atualizarItensPorPrecificacao() {
		TabelaPreco tabPreco = (TabelaPreco) this.entityPlc;
		List<ItemTabela> listaItens = tabPreco.getItemTabela();
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		for (ItemTabela item : listaItens) {
			Produto produto = (Produto) iocControleFacadeUtil.getFacade().edit(context, Produto.class, item.getProduto().getId())[0];

			item.setTitulo(produto.getTitulo());
			item.setTipoProduto(produto.getTipoProduto());
			item.setCodigoBarras(produto.getCodigoBarras());
			item.setPrecoUltCompra(produto.getPrecoUltCompra());
			item.setPrecoVendaSugerido(produto.getPrecoVendaSugerido());

			double preco = calcularAjustePrecoProduto(tabPreco, produto);

			item.setPreco(new BigDecimal(preco));
		}

		msgUtil.msg("{tabpreco.ok.atualizar}", new Object[] { listaItens.size() }, PlcMessage.Cor.msgAzulPlc.name());
		msgUtil.msg("{tabpreco.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
		
		return baseEditMB.getDefaultNavigationFlow();
	}

	private ItemTabela criaNovoItem(TabelaPreco tabPreco, Estoque itemEstoque) {
		ItemTabela item = new ItemTabelaEntity();

		item.setTabelaPreco(tabPreco);
		item.setProduto(itemEstoque.getProduto());
		item.setTipoProduto(itemEstoque.getProduto().getTipoProduto());
		item.setTitulo(itemEstoque.getProduto().getTitulo());
		item.setCodigoBarras(itemEstoque.getProduto().getCodigoBarras());
		item.setPrecoUltCompra(itemEstoque.getProduto().getPrecoUltCompra());
		item.setPrecoVendaSugerido(itemEstoque.getProduto().getPrecoVendaSugerido());
		item.setLocalizacao(itemEstoque.getLocalizacao());

		return item;
	}

	/**
	 * Exclui grafo do Value Object principal (ie. Mestre, seus Detalhes e
	 * Sub-Detalhe eventuais). Mas somente faz isso se já tiver recuperado os
	 * detalhes
	 */
	@Override
	public String delete() {

		if (this.entityPlc != null) {
			TabelaPreco tabPreco = (TabelaPreco) this.entityPlc;
			List<ItemTabela> listaItens = tabPreco.getItemTabela();

			if (listaItens == null) {
				msgUtil.msg("{tabpreco.err.buscar.clicarAbaItens}",
						PlcMessage.Cor.msgAmareloPlc.name());
				return baseEditMB.getDefaultNavigationFlow();
			}
		}

		return baseDeleteMB.delete(entityPlc);
	}

	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA);

		if (nomeAction.contains("mdt")) {
			contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.SIM);
			contextUtil.getRequest().setAttribute("atualizarItensPorPrecificacao", PlcConstants.SIM);
		}

		super.handleButtonsAccordingFormPattern();
	}

}
