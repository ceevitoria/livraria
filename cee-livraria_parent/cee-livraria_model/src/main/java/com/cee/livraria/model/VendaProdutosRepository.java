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
import com.cee.livraria.entity.config.CompraVendaConfig;
import com.cee.livraria.entity.config.CompraVendaConfigEntity;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.config.TipoMensagemSucessoVendaConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ItemMovimento;
import com.cee.livraria.entity.estoque.ItemMovimentoEntity;
import com.cee.livraria.entity.estoque.ModoMovimento;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.estoque.apoio.VendaProduto;
import com.cee.livraria.entity.pagamento.FormaPagto;
import com.cee.livraria.entity.pagamento.Pagamento;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.persistence.jpa.livro.LivroDAO;
import com.cee.livraria.persistence.jpa.vendaproduto.VendaProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.domain.type.PlcYesNo;

public class VendaProdutosRepository {
	
	@Inject
	private LivroDAO livroDAO;

	@Inject
	protected transient Logger log;

	private CompraVendaConfig config;
	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();
	
	@Inject
	private VendaProdutoDAO jpa;
	
	private PlcBaseContextVO context;
	
	/** 
	 * Registra de venda dos produtos se o caixa estiver aberto
	 * @param entityList
	 * @throws PlcException
	 */
	public RetornoConfig registrarVendaProdutos(PlcBaseContextVO context, List entityList, List pagtoList) throws PlcException {
		this.context = context;
		Date dataVenda = new Date();

		mensagens.clear();
		alertas.clear();
		
		try {
			carregaConfiguracao(context);
			
			double valorPago = validarValorPagamento(entityList, pagtoList);
			
			registraMovimento(entityList, dataVenda, valorPago);
			
			int quantidade = atualizaEstoque(entityList, dataVenda);
			
			atualizaCaixa(entityList, pagtoList, dataVenda, valorPago, quantidade);
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("VendaProdutosRepository", "registrarVendaProdutos",	e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}

	@SuppressWarnings("rawtypes")
	private double validarValorPagamento(List itensVenda, List pagtoList) throws PlcException {
		
		// Calcula o valor todos dos itens a serem vendidos
		double valorItens = 0.0;
		
		for (Object o : itensVenda) {
			VendaProduto cl = (VendaProduto)o;
			
			// Evita os itens em branco
			if (cl.getLivro() != null && cl.getLivro().getId() != null) {
				double valorItem = cl.getQuantidade().doubleValue() * cl.getValorUnitario().doubleValue();
				valorItem = Math.round(valorItem * 100.00) / 100.00;
				valorItens += valorItem;
			}
		}

		valorItens = Math.round(valorItens * 100.00) / 100.00;

		// Calcula o valor total informado para todas as formas de pagamento  
		double valorFormasPagto = 0.0;
		
		for (Object o : pagtoList) {
			Pagamento pagto = (Pagamento)o;
			
			if (pagto != null && pagto.getValor() != null) {
				double valorFormaPagto = pagto.getValor().doubleValue();
				valorFormaPagto = Math.round(valorFormaPagto * 100.00) / 100.00;
				valorFormasPagto += valorFormaPagto;
			}
		}

		valorFormasPagto = Math.round(valorFormasPagto * 100.00) / 100.00;
		
		if (config.getPermitirVendaPagtoDivergente().equals(PlcYesNo.N) && valorFormasPagto != valorItens) {
			throw new PlcException("{erro.vendaProduto.valorPagto.diverge.valorTotal}", 
				new Object[] {String.format("R$ %,.02f", new Object[]{valorItens}), 
							  String.format("R$ %,.02f", new Object[]{valorFormasPagto})});
			
		} else if (config.getPermitirVendaPagtoDivergente().equals(PlcYesNo.S) && 
				config.getValorMaximoAjustePagtoDivergente().doubleValue() < Math.abs(valorFormasPagto-valorItens)) {
			throw new PlcException("{erro.vendaProduto.valorPagto.diverge.valorTotal}", 
					new Object[] {String.format("R$ %,.02f", new Object[]{valorItens}), 
								  String.format("R$ %,.02f", new Object[]{valorFormasPagto})});
		}
		
		return valorFormasPagto; 
	}
	
	/**
	 * @param context
	 */
	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
		List listaConfig = (List)jpa.findAll(context, CompraVendaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{erro.vendaProduto.configuracao.inexistente}");
		}
		
		config = (CompraVendaConfig)listaConfig.get(0);
	}

	/**
	 * Atualiza o saldo dos livros sendo vendidos no estoque
	 * OBS: Não faz crítica para saldo negativo.
	 * @param relacaoLivros Relação dos livros sendo vendidos
	 * @param dataVenda Data em que a venda foi realizada
	 * @return quantidade vendida de livros
	 * @throws PlcException
	 */
	private int atualizaEstoque(List relacaoLivros, Date dataVenda) throws PlcException {
		int qtEstoque = 0;
		int qtVendida = 0;
		
		for (Object o : relacaoLivros) {
			VendaProduto vl = (VendaProduto)o;

			if (vl.getLivro() != null) {
				@SuppressWarnings("unchecked")
				List<Estoque> lista = (List<Estoque>)jpa.findByFields(context, EstoqueEntity.class, "querySelByLivro", new String[]{"livro"}, new Object[]{vl.getLivro()});
				
				if (lista != null && lista.size() == 1) {
					Estoque estoque = (Estoque)lista.get(0);
					
					qtVendida += vl.getQuantidade();
					qtEstoque = estoque.getQuantidade() - vl.getQuantidade();
					
					if (config.getPermiteVendaParaEstoqueNegativo().equals(PlcYesNo.N) && qtEstoque < 0) {
						throw new PlcException("{erro.vendaProduto.estoque.negativo.atingido}", new Object[]{vl.getLivro().getCodigoBarras(), vl.getLivro().getTitulo()});
					}
					
					if (config.getAlertaEstoqueNegativoAtingido().equals(PlcYesNo.S) && qtEstoque < 0) {
						alertas.add(String.format("Quantidade negativa em estoque atingida para o livro '%s-%s'", new Object[]{vl.getLivro().getCodigoBarras(), vl.getLivro().getTitulo()}));
					} else if (config.getAlertaEstoqueMinimoAtingido().equals(PlcYesNo.S) && qtEstoque <= estoque.getQuantidadeMinima()) {
						alertas.add(String.format("Quantidade minina em estoque atingida para o livro '%s-%s'", new Object[]{vl.getLivro().getCodigoBarras(), vl.getLivro().getTitulo()}));
					}
					
					estoque.setQuantidade(qtEstoque);

					estoque.setDataUltAlteracao(dataVenda);
					estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

					jpa.update(context, estoque);
				} else {
					throw new PlcException("{erro.vendaProduto.estoque.livro.inexistente}", new Object[]{vl.getLivro().getCodigoBarras(), vl.getLivro().getTitulo()});
				}
			}
		}
		 
		return qtVendida;
	}

	/**
	 * Atualiza o saldo do caixa e cria um movimento de caixa para a venda atual.
	 * Valida se existe um registro para o caixa e se o mesmo encontra-se aberto.
	 * @param livros Relação dos livros sendo vendidos
	 * @param pagtos Relação dos pagamentos realizados
	 * @param dataVenda Data em que a venda foi realizada
	 * @param valor Valor total da venda
	 * @param quantidade Quantidade total de livros vendidos
	 */
	private void atualizaCaixa(List livros, List pagtos, Date dataVenda, double valor, int quantidade) throws PlcException {
		Caixa caixa = null;
		
		//recupera o caixa existente (só deve existir um único registro de caixa para o sistema "LIV")
		@SuppressWarnings("rawtypes")
		List lista = jpa.findAll(context, CaixaEntity.class, null);
		
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
		
		List<String> listaObs = montaObservacao(livros);

		configuraMensagens(listaObs, valor, quantidade);
		
		//cria um novo movimento de caixa
		CaixaMovimento movCaixa = criaMovimentoCaixa(dataVenda, valor, caixa);

		//atualiza o saldo do caixa
		atualizaSaldoCaixa(dataVenda, caixa, movCaixa);
		
		atualizaPagtosCaixa(dataVenda, caixa, pagtos);
	}

	/**
	 * Atualiza o caixa para cada tipo de pagamento realizado
	 * @param dataVenda
	 * @param caixa
	 * @param pagtos Lista dos pagamentos realizados
	 */
	@SuppressWarnings("unchecked")
	private void atualizaPagtosCaixa(Date dataVenda, Caixa caixa, List pagtos) throws PlcException {
		List<CaixaFormaPagto> formasPagtoCaixa = caixa.getCaixaFormaPagto();
		
		for (Object o : pagtos) {
			boolean achou = false;
			Pagamento pagto = (Pagamento)o;
			FormaPagto formaPagto = pagto.getFormaPagto();
			
			if (formaPagto != null) {
				
				for (CaixaFormaPagto caixaFormaPagto : formasPagtoCaixa) {
					caixaFormaPagto = (CaixaFormaPagto)jpa.findById(context, CaixaFormaPagtoEntity.class, caixaFormaPagto.getId());
					
					if (caixaFormaPagto.getFormaPagto().getId().equals(formaPagto.getId())) {
						caixaFormaPagto.setValor(caixaFormaPagto.getValor().add(pagto.getValor()));
						
						caixaFormaPagto.setDataUltAlteracao(dataVenda);
						caixaFormaPagto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
						
						jpa.update(context, caixaFormaPagto);
						
						achou = true;
					}
				}

				if (!achou) {
					CaixaFormaPagto caixaFormaPagto = new CaixaFormaPagtoEntity();
					caixaFormaPagto.setCaixa(caixa);
					caixaFormaPagto.setData(dataVenda);
					caixaFormaPagto.setFormaPagto(formaPagto);
					caixaFormaPagto.setValor(pagto.getValor());
					caixaFormaPagto.setSistema("LIV");
					caixaFormaPagto.setSitHistoricoPlc("A");

					caixaFormaPagto.setDataUltAlteracao(dataVenda);
					caixaFormaPagto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
					
					jpa.insert(context, caixaFormaPagto);
				}
			}
		}
	}

	/**
	 * Atualiza o caixa adicionando o valor do movimento (total vendido) ao saldo do caixa
	 * @param dataVenda
	 * @param caixa
	 * @param movCaixa
	 */
	private void atualizaSaldoCaixa(Date dataVenda, Caixa caixa,
			CaixaMovimento movCaixa) throws PlcException {
		caixa.setSaldo(caixa.getSaldo().add(movCaixa.getValor()));
		
		caixa.setDataUltAlteracao(dataVenda);
		caixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		//atualiza o saldo do caixa no meio de persistencia
		jpa.update(context, caixa);
	}

	/**
	 * Cria um novo movimento do caixa para a venda atual
	 * @param dataVenda
	 * @param valor
	 * @param caixa
	 * @return O movimento do caixa recem criado
	 */
	private CaixaMovimento criaMovimentoCaixa(Date dataVenda, double valor,	Caixa caixa) {
		CaixaMovimento movCaixa = new CaixaMovimentoEntity();
		movCaixa.setData(dataVenda);
		movCaixa.setTipo(TipoMovimentoCaixa.VD);
		movCaixa.setValor(BigDecimal.valueOf(valor));

		StringBuilder obs = new StringBuilder();

		for (String mensagem : mensagens) {
			obs.append(mensagem);
			obs.append("\n");
		}
		
		movCaixa.setObservacao(obs.toString());
		movCaixa.setSitHistoricoPlc("A");
		movCaixa.setCaixa(caixa);

		movCaixa.setDataUltAlteracao(dataVenda);
		movCaixa.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		//registra o novo movimento do caixa no meio de persistencia
		jpa.insert(context, movCaixa);
		
		return movCaixa;
	}

	/**
	 * Configura como as mensagens serão apresentadas para o usuário
	 * @param listaObs
	 * @param valor
	 * @param quantidade
	 */
	private void configuraMensagens(List<String> listaObs, double valor, int quantidade) {
		if (config.getExibirMensagemSucessoVenda().equals(PlcYesNo.S) ) {

			if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.B) ) {
				mensagens.add("Venda realizada com sucesso!");
			} else if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.V) ){
				mensagens.add(String.format("Venda de %02d livro(s) no valor total de R$ %,.02f realizada com sucesso!", new Object[]{quantidade, valor}));
			} else if (config.getTipoMensagemSucessoVenda() .equals(TipoMensagemSucessoVendaConfig.D) ){
				mensagens.add(String.format("Venda de %02d livro(s) no valor total de R$ %,.02f realizada com sucesso!", new Object[]{quantidade, valor}));
				mensagens.addAll(listaObs);
			}
		}
	}

	/**
	 * @param relacaoLivros
	 * @return
	 */
	private List<String> montaObservacao(List relacaoLivros) {
		List<String> listaObs = new ArrayList<String>();

		//monta o texto para a observação do movimento do caixa
		for (Object o : relacaoLivros) {
			VendaProduto cl = (VendaProduto)o;

			if (cl.getLivro() != null) {
				StringBuilder obs = new StringBuilder();
				obs.append("Livro: '");
				obs.append(cl.getLivro().getCodigoBarras());
				obs.append("-");
				obs.append(cl.getLivro().getTitulo());
				obs.append("' - Quant: '");
				obs.append(String.format("%02d", cl.getQuantidade()));
				obs.append("' - Valor Unit: '");
				obs.append(String.format("R$ %,.02f", cl.getValorUnitario()));
				obs.append("' - Valor Total: '");
				obs.append(String.format("R$ %,.02f", cl.getValorTotal()));
				obs.append("'");

				listaObs.add(obs.toString());
			}
		}
		return listaObs;
	}

	/**
	 * Registra o movimento de venda do tipo normal para os livros relacionados
	 * @param relacaoLivros Relação dos livros sendo vendidos
	 * @param dataVenda Data em que a venda foi realizada
	 * @param valorPago Valor efetivamente pago nesta venda (soma das formas de pagamento)
	 */
	private void registraMovimento(@SuppressWarnings("rawtypes") List relacaoLivros, Date dataVenda, double valorPago) throws PlcException {
//		double valorGeral = 0.0;
		Movimento mov = new MovimentoEntity();
		List<ItemMovimento> itens = new ArrayList<ItemMovimento>();
		
		for (Object o : relacaoLivros) {
			VendaProduto cl = (VendaProduto)o;
			
			// Evita os itens em branco
			if (cl.getLivro() != null && cl.getLivro().getId() != null) {
				ItemMovimento item = new ItemMovimentoEntity();

				double valorTotalItem = cl.getQuantidade().doubleValue() * cl.getValorUnitario().doubleValue();
				
				valorTotalItem = Math.round(valorTotalItem * 100.00) / 100.00;
				item.setValorTotal(BigDecimal.valueOf(valorTotalItem));
				
				Livro livro = (Livro)jpa.findById(context, Livro.class, cl.getLivro().getId());
				
				item.setLivro(livro);
				item.setQuantidade(cl.getQuantidade());
				item.setValorUnitario(cl.getValorUnitario());
				item.setValorTotal(cl.getValorTotal());
				item.setTipo(TipoMovimento.S);
				item.setMovimento(mov);
				
				item.setDataUltAlteracao(dataVenda);
				item.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				itens.add(item);
				
//				valorGeral += valorTotalItem;
			}
		}

//		valorGeral = Math.round(valorGeral * 100.00) / 100.00;
		
		mov.setData(dataVenda);
		mov.setTipo(TipoMovimento.S);
		mov.setModo(ModoMovimento.N);
		mov.setValor(BigDecimal.valueOf(valorPago));
		mov.setItemMovimento(itens);

		mov.setDataUltAlteracao(dataVenda);
		mov.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		jpa.insert(context, mov);

//		return valorGeral;
	}

	/**
	 * Calcula valor unitário e valor total de cada item da venda e retorna o valor geral  
	 * @param entityList Relação de itens sendo vendidos
	 * @return BigDecimal O valor total da venda
	 * @throws PlcException
	 */
	@SuppressWarnings("rawtypes")
	public BigDecimal buscarDadosVendaProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		this.context = context;
		double totalGeral = 0.0;
		double mult = 0.0;
		
		try {
			
			for (Object o : entityList) {
				VendaProduto cl = (VendaProduto)o;
				
				if (cl.getLivro() != null) {
					
					PrecoTabela preco = livroDAO.obterPrecoTabela(context, cl.getLivro().getId());
					
					cl.setNomeTabela(preco.getNomeTabela());
					
					if (preco.getIdTabela() != null) {
						cl.setValorUnitario(preco.getPrecoTabela());
					} else {
						Livro livro = (Livro)jpa.findById(context, Livro.class, cl.getLivro().getId());
						cl.setValorUnitario(livro.getPrecoVendaSugerido());
					}
					
					if (cl.getQuantidade() == null || cl.getQuantidade() == 0) {
						cl.setQuantidade(1);
					}
					
					if (cl.getValorUnitario() != null) { 
						mult = cl.getQuantidade().intValue() * cl.getValorUnitario().doubleValue();
						mult = Math.round(mult * 100.00) / 100.00;
						cl.setValorTotal(BigDecimal.valueOf(mult));
					}
					
					totalGeral += mult;
				}
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("VendaProdutosRepository",
					"buscarDadosvendaProdutos", e, log, "");
		}
		
		return BigDecimal.valueOf(totalGeral);
	}
	
}
