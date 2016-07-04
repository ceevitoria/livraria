package com.cee.livraria.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.CaixaFormaPagto;
import com.cee.livraria.entity.caixa.CaixaFormaPagtoEntity;
import com.cee.livraria.entity.caixa.CaixaMovimento;
import com.cee.livraria.entity.caixa.CaixaMovimentoEntity;
import com.cee.livraria.entity.caixa.StatusCaixa;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ItemMovimento;
import com.cee.livraria.entity.estoque.ItemMovimentoEntity;
import com.cee.livraria.entity.estoque.ModoMovimento;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.estoque.apoio.DevolucaoProduto;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.persistence.jpa.devolucaoproduto.DevolucaoProdutoDAO;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;

@SPlcRepository
@PlcAggregationDAOIoC(value=DevolucaoProduto.class)
public class DevolucaoProdutosRepository {
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	protected transient Logger log;

//	private CompraVendaConfig config;
	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();
	
	@Inject
	private DevolucaoProdutoDAO dao;
	
	private PlcBaseContextVO context;
	
	/** 
	 * Registra de devolucao dos produtos se o caixa estiver aberto
	 * @param entityList
	 * @throws PlcException
	 */
	@SuppressWarnings("rawtypes")
	public RetornoConfig registrarDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		this.context = context;
		Date dataDevolucao = new Date();

		mensagens.clear();
		alertas.clear();
		
		try {
//			carregaConfiguracao(context);
			
			double valorDevolver = validarValorDevolucao(entityList);
			
			registraMovimento(entityList, dataDevolucao, valorDevolver);
			
			int quantidade = atualizaEstoque(entityList, dataDevolucao);
			
			atualizaCaixa(entityList, dataDevolucao, valorDevolver, quantidade);
			
			mensagens.add("{sucesso.devolucao.produtos}");
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("DevolucaoProdutosRepository", "registrarDevolucaoProdutos",	e, log, "");
		}
		
		return new RetornoConfig(null, null, alertas, mensagens);
	}

	@SuppressWarnings("rawtypes")
	private double validarValorDevolucao(List itensDevolucao) throws PlcException {
		
		// Calcula o valor todos dos itens a serem devolvidos
		double valorItens = 0.0;
		
		for (Object o : itensDevolucao) {
			DevolucaoProduto vp = (DevolucaoProduto)o;
			
			// Evita os itens em branco
			if (vp.getProduto() != null && vp.getProduto().getId() != null) {
				double valorItem = vp.getQuantidade().doubleValue() * vp.getValorUnitario().doubleValue();
				valorItem = Math.round(valorItem * 100.00) / 100.00;
				valorItens += valorItem;
			}
		}

		valorItens = Math.round(valorItens * 100.00) / 100.00;

		
//		if (config.getPermitirVendaPagtoDivergente().equals(PlcYesNo.N) && valorFormasPagto != valorItens) {
//			throw new PlcException("{erro.devolucaoProduto.valorPagto.diverge.valorTotal}", 
//				new Object[] {String.format("R$ %,.02f", new Object[]{valorItens}), 
//							  String.format("R$ %,.02f", new Object[]{valorFormasPagto})});
//			
//		} else if (config.getPermitirVendaPagtoDivergente().equals(PlcYesNo.S) && 
//				config.getValorMaximoAjustePagtoDivergente().doubleValue() < Math.abs(valorFormasPagto-valorItens)) {
//			throw new PlcException("{erro.devolucaoProduto.valorPagto.diverge.valorTotal}", 
//					new Object[] {String.format("R$ %,.02f", new Object[]{valorItens}), 
//								  String.format("R$ %,.02f", new Object[]{valorFormasPagto})});
//		}
		
		return valorItens; 
	}
	
//	/**
//	 * @param context
//	 */
//	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
//		List listaConfig = (List)dao.findAll(context, CompraVendaConfigEntity.class, null);
//		
//		if (listaConfig == null || listaConfig.size() != 1) {
//			throw new PlcException("{erro.devolucaoProduto.configuracao.inexistente}");
//		}
//		
//		config = (CompraVendaConfig)listaConfig.get(0);
//	}

	/**
	 * Atualiza o saldo dos produtos sendo devolvidos no estoque
	 * OBS: Não faz crítica para saldo negativo.
	 * @param relacaoProdutos Relação dos produtos sendo devolvidos
	 * @param dataDevolucao Data em que a devolucao foi realizada
	 * @return quantidade devolvida de produtos
	 * @throws PlcException
	 */
	private int atualizaEstoque(List relacaoProdutos, Date dataDevolucao) throws PlcException {
		int qtEstoque = 0;
		int qtDevolvida = 0;
		
		for (Object o : relacaoProdutos) {
			DevolucaoProduto devolucao = (DevolucaoProduto)o;

			if (devolucao.getProduto() != null) {
				@SuppressWarnings("unchecked")
				List<Estoque> lista = (List<Estoque>)dao.findByFields(context, EstoqueEntity.class, "querySelByProduto", new String[]{"produto"}, new Object[]{devolucao.getProduto()});
				
				if (lista != null && lista.size() == 1) {
					Estoque estoque = (Estoque)lista.get(0);
					
					qtDevolvida += devolucao.getQuantidade();
					qtEstoque = estoque.getQuantidade() + devolucao.getQuantidade();
					
					estoque.setQuantidade(qtEstoque);

					estoque.setDataUltAlteracao(dataDevolucao);
					estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

					dao.update(context, estoque);
				} else {
					throw new PlcException("{erro.devolucao.estoque.produto.inexistente}", new Object[]{devolucao.getProduto().getCodigoBarras(), devolucao.getProduto().getTitulo()});
				}
			}
		}
		 
		return qtDevolvida;
	}

	/**
	 * Atualiza o saldo do caixa e cria um movimento de caixa para a devolucao atual.
	 * Valida se existe um registro para o caixa e se o mesmo encontra-se aberto.
	 * @param produtos Relação dos produtos sendo devolvidos
	 * @param dataDevolucao Data em que a venda foi realizada
	 * @param valor Valor total da venda
	 * @param quantidade Quantidade total de livros vendidos
	 */
	private void atualizaCaixa(List produtos, Date dataDevolucao, double valor, int quantidade) throws PlcException {
		Caixa caixa = null;
		
		//recupera o caixa existente (só deve existir um único registro de caixa para o sistema "LIV")
		@SuppressWarnings("rawtypes")
		List lista = dao.findAll(context, CaixaEntity.class, null);
		
		for (Object object : lista) {
			
			if ("LIV".equals(((Caixa)object).getSistema())) {
				caixa = (Caixa)object;
			}
		}
		
		if (caixa == null) {
			throw new PlcException("{erro.caixa.inexistente}");
		}
		
		if (caixa.getStatus() != StatusCaixa.A) {
			throw new PlcException("{erro.caixa.fechado}");
		}
		
//		List<String> listaObs = montaObservacao(produtos);

//		configuraMensagens(listaObs, valor, quantidade);
		
		//cria um novo movimento de caixa
		CaixaMovimento movCaixa = criaMovimentoCaixa(dataDevolucao, valor, caixa);

		atualizaFormaPagtoCaixa(dataDevolucao, caixa, valor);
		
		//atualiza o saldo do caixa
		atualizaSaldoCaixa(dataDevolucao, caixa, movCaixa);
	}

	/**
	 * Atualiza o caixa para cada tipo de pagamento realizado
	 * @param dataVenda
	 * @param caixa
	 * @param pagtos Lista dos pagamentos realizados
	 */
	private void atualizaFormaPagtoCaixa(Date dataVenda, Caixa caixa, double valor) throws PlcException {
		CaixaFormaPagto caixaFormaPagto = null;
		CaixaFormaPagto cfpArg = new CaixaFormaPagtoEntity();
		cfpArg.setCaixa(caixa);
		cfpArg.setDataAbertura(caixa.getDataUltAbertura());
		
		List<CaixaFormaPagto> formasPagtoCaixa = dao.findList(context, cfpArg, "", 0, 10);
		
		for (CaixaFormaPagto cfp : formasPagtoCaixa) {
			
			if (cfp.getFormaPagto().getId().compareTo(1L) == 0 ) {
				caixaFormaPagto = cfp;
				break;
			}
		}
		
		if (caixaFormaPagto != null && caixaFormaPagto.getValor().doubleValue() >= valor) {
			caixaFormaPagto.setValor(caixaFormaPagto.getValor().subtract(new BigDecimal(Double.toString(valor))));
			
			caixaFormaPagto.setDataUltAlteracao(dataVenda);
			caixaFormaPagto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			
			dao.update(context, caixaFormaPagto);
		} else {
			throw new PlcException("{erro.devolucao.saldo.insuficiente.caixa}");
		}
	}

	/**
	 * Atualiza o caixa retirando o valor do movimento (total devolvido) do saldo do caixa
	 * @param dataDevolucao
	 * @param caixa
	 * @param movCaixa
	 */
	private void atualizaSaldoCaixa(Date dataVenda, Caixa caixa, CaixaMovimento movCaixa) throws PlcException {
		caixa.setSaldo(caixa.getSaldo().add(movCaixa.getValor()));
		
		caixa.setDataUltAlteracao(dataVenda);
		caixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		//atualiza o saldo do caixa no meio de persistencia
		dao.update(context, caixa);
	}

	/**
	 * Cria um novo movimento do caixa para a devolucao atual
	 * @param dataDevolucao
	 * @param valor
	 * @param caixa
	 * @return O movimento do caixa recem criado
	 */
	private CaixaMovimento criaMovimentoCaixa(Date dataDevolucao, double valor,	Caixa caixa) {
		CaixaMovimento movCaixa = new CaixaMovimentoEntity();
		movCaixa.setData(dataDevolucao);
		movCaixa.setTipo(TipoMovimentoCaixa.DE);
		movCaixa.setValor(BigDecimal.valueOf(valor * -1));

		movCaixa.setObservacao("Devolução de Produto");
		movCaixa.setSitHistoricoPlc("A");
		movCaixa.setCaixa(caixa);

		movCaixa.setDataUltAlteracao(dataDevolucao);
		movCaixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		//registra o novo movimento do caixa no meio de persistencia
		dao.insert(context, movCaixa);
		
		return movCaixa;
	}

//	/**
//	 * Configura como as mensagens serão apresentadas para o usuário
//	 * @param listaObs
//	 * @param valor
//	 * @param quantidade
//	 */
//	private void configuraMensagens(List<String> listaObs, double valor, int quantidade) {
//		if (config.getExibirMensagemSucessoVenda().equals(PlcYesNo.S) ) {
//
//			if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.B) ) {
//				mensagens.add("Venda realizada com sucesso!");
//			} else if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.V) ){
//				mensagens.add(String.format("Venda de %02d livro(s) no valor total de R$ %,.02f realizada com sucesso!", new Object[]{quantidade, valor}));
//			} else if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.D) ){
//				mensagens.add(String.format("Venda de %02d livro(s) no valor total de R$ %,.02f realizada com sucesso!", new Object[]{quantidade, valor}));
//				mensagens.addAll(listaObs);
//			}
//		}
//	}

//	/**
//	 * @param relacaoProdutos
//	 * @return
//	 */
//	private List<String> montaObservacao(List relacaoProdutos) {
//		List<String> listaObs = new ArrayList<String>();
//
//		//monta o texto para a observação do movimento do caixa
//		for (Object o : relacaoProdutos) {
//			DevolucaoProduto vp = (DevolucaoProduto)o;
//
//			if (vp.getProduto() != null) {
//				StringBuilder obs = new StringBuilder();
//				obs.append("Produto: '");
//				obs.append(vp.getProduto().getCodigoBarras());
//				obs.append("-");
//				obs.append(vp.getProduto().getTitulo());
//				obs.append("' - Quant: '");
//				obs.append(String.format("%02d", vp.getQuantidade()));
//				obs.append("' - Valor Unit: '");
//				obs.append(String.format("R$ %,.02f", vp.getValorUnitario()));
//				obs.append("' - Valor Total: '");
//				obs.append(String.format("R$ %,.02f", vp.getValorTotal()));
//				obs.append("'");
//
//				listaObs.add(obs.toString());
//			}
//		}
//		return listaObs;
//	}

	/**
	 * Registra o movimento de venda do tipo normal para os produtos relacionados
	 * @param relacaoProdutos Relação dos produtos sendo vendidos
	 * @param dataMovimento Data em que a venda foi realizada
	 * @param valorPago Valor efetivamente pago nesta venda (soma das formas de pagamento)
	 */
	private void registraMovimento(@SuppressWarnings("rawtypes") List relacaoProdutos, Date dataMovimento, double valorPago) throws PlcException {
		Movimento mov = new MovimentoEntity();
		List<ItemMovimento> itens = new ArrayList<ItemMovimento>();
		
		for (Object o : relacaoProdutos) {
			DevolucaoProduto vp = (DevolucaoProduto)o;
			
			// Evita os itens em branco
			if (vp.getProduto() != null && vp.getProduto().getId() != null) {
				ItemMovimento item = new ItemMovimentoEntity();

				double valorTotalItem = vp.getQuantidade().doubleValue() * vp.getValorUnitario().doubleValue();
				
				valorTotalItem = Math.round(valorTotalItem * 100.00) / 100.00;
				item.setValorTotal(BigDecimal.valueOf(valorTotalItem));
				
				Produto produto = (Produto)dao.findById(context, Produto.class, vp.getProduto().getId());
				
				item.setProduto(produto);
				item.setQuantidade(vp.getQuantidade());
				item.setValorUnitario(vp.getValorUnitario());
				item.setValorTotal(vp.getValorTotal());
				item.setTipo(TipoMovimento.S);
				item.setMovimento(mov);
				
				item.setDataUltAlteracao(dataMovimento);
				item.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				itens.add(item);
			}
		}

		mov.setData(dataMovimento);
		mov.setTipo(TipoMovimento.S);
		mov.setModo(ModoMovimento.N);
		mov.setValor(BigDecimal.valueOf(valorPago));
		mov.setItemMovimento(itens);

		mov.setDataUltAlteracao(dataMovimento);
		mov.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		dao.insert(context, mov);
	}

	/**
	 * Calcula valor unitário e valor total de cada item da venda e retorna o valor geral  
	 * @param entityList Relação de itens sendo vendidos
	 * @return BigDecimal O valor total da venda
	 * @throws PlcException
	 */
	@SuppressWarnings("rawtypes")
	public BigDecimal buscarDadosDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		this.context = context;
		double totalGeral = 0.0;
		double mult = 0.0;
		
		try {
			
			for (Object o : entityList) {
				DevolucaoProduto vp = (DevolucaoProduto)o;
				
				if (vp.getProduto() != null) {
					Produto produto = (Produto)dao.findById(context, Produto.class, vp.getProduto().getId());
					
					PrecoTabela preco = produtoDAO.obterPrecoTabela(context, vp.getProduto().getId());
					
					vp.setNomeTabela(preco.getNomeTabela());
					
					if (preco.getIdTabela() != null) {
						vp.setValorUnitario(preco.getPrecoTabela());
					} else {
						vp.setValorUnitario(produto.getPrecoVendaSugerido());
					}
					
					vp.setTipoProduto(produto.getTipoProduto());

					if (vp.getQuantidade() == null || vp.getQuantidade() == 0) {
						vp.setQuantidade(1);
					}
					
					if (vp.getValorUnitario() != null) { 
						mult = vp.getQuantidade().intValue() * vp.getValorUnitario().doubleValue();
						mult = Math.round(mult * 100.00) / 100.00;
						vp.setValorTotal(BigDecimal.valueOf(mult));
					}
					
					totalGeral += mult;
				}
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("DevolucaoProdutosRepository", "buscarDadosDevolucaoProdutos", e, log, "");
		}
		
		return BigDecimal.valueOf(totalGeral);
	}
	
}
