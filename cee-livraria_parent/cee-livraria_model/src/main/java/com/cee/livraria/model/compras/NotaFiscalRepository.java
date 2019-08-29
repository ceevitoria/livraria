package com.cee.livraria.model.compras;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.compra.ContaPagar;
import com.cee.livraria.entity.compra.Fornecedor;
import com.cee.livraria.entity.compra.FornecedorProduto;
import com.cee.livraria.entity.compra.ItemNotaFiscal;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.compra.Parcelamento;
import com.cee.livraria.entity.compra.StatusContaPagar;
import com.cee.livraria.entity.compra.StatusNotaFiscal;
import com.cee.livraria.entity.config.AjusteEstoqueConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ItemMovimento;
import com.cee.livraria.entity.estoque.ItemMovimentoEntity;
import com.cee.livraria.entity.estoque.ModoMovimento;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.persistence.jpa.notafiscal.NotaFiscalDAO;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.bindingtype.PlcEditAfter;
import com.powerlogic.jcompany.model.bindingtype.PlcUpdateBefore;

/**
 * Classe de Modelo gerada pelo assistente
 */
 
@SPlcRepository 
@PlcAggregationIoC(clazz=NotaFiscal.class)
public class NotaFiscalRepository extends PlcBaseRepository {

	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private NotaFiscalDAO dao;

	@Inject
	protected transient Logger log;

	private AjusteEstoqueConfig config;

	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();

	public void antesAtualizar (@Observes @PlcUpdateBefore PlcBaseContextVO context) throws PlcException {
		
		if (context.getUrl().equalsIgnoreCase("notaFiscal")) {
			NotaFiscal notaFiscal = (NotaFiscal) context.getEntityForExtension();
			
			if (notaFiscal != null) {
				ajustaDataEmissaoContasPagar(context, notaFiscal);
			}
		}
	}
	
	public void depoisEditar (@Observes @PlcEditAfter PlcBaseContextVO context) throws PlcException {
		
		if (context.getUrl().equalsIgnoreCase("notaFiscal")) {
			NotaFiscal notaFiscal = (NotaFiscal) context.getEntityForExtension();
			
			if (notaFiscal != null) {
				
				for (Object o : notaFiscal.getItemNotaFiscal()) {
					ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
					
					if (itemNF.getProduto() != null && itemNF.getProduto().getId() != null && itemNF.getProduto().getTipoProduto() != null) {
						itemNF.setTipoProduto(itemNF.getProduto().getTipoProduto());
					}
				}
			}
		}
	}

	private void ajustaDataEmissaoContasPagar(PlcBaseContextVO context, NotaFiscal notaFiscal) {
		
		if (notaFiscal.getContaPagar() != null) {
			List<ContaPagar> contas = notaFiscal.getContaPagar();
			
			for (ContaPagar contaPagar : contas) {
				contaPagar.setDataEmissao(notaFiscal.getDataEmissao());
			}
		}
		
	}


	public RetornoConfig registrarEntradaNotaFiscal(PlcBaseContextVO context, NotaFiscal notaFiscal) throws PlcException {
		Date dataRegistro = new Date();
		mensagens.clear();
		alertas.clear();
		
		try {
			if (StatusNotaFiscal.A.equals(notaFiscal.getStatus())) {
				validaValoresNota(context, notaFiscal, dataRegistro);
				registraMovimento(context, notaFiscal, dataRegistro);
				atualizaEstoque(context, notaFiscal, dataRegistro);
				registraContasPagar(context, notaFiscal, dataRegistro);
				cadastraProdutoFornecedor(context, notaFiscal, dataRegistro);
				ajustaPrecoUltimaCompraProdutos(context, notaFiscal, dataRegistro);
				
				mensagens.add("Registro de nota fiscal realizado com sucesso!"); 
				
				notaFiscal.setStatus(StatusNotaFiscal.R);
				notaFiscal.setDataUltAlteracao(dataRegistro);
				notaFiscal.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				dao.update(context, notaFiscal);
			} else {
				throw new PlcException("{compra.erro.notaFiscal.registrada}");
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("NotaFiscalRepository", "registrarEntradaNotaFiscal", e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}
	
	private void ajustaPrecoUltimaCompraProdutos(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		
		for (Object o : notaFiscal.getItemNotaFiscal()) {
			ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
			
			// Evita os itens em branco
			if (itemNF.getProduto() != null && itemNF.getProduto().getId() != null && itemNF.getValorUnitario() != null && itemNF.getPercentualDesconto() != null) {
				Produto produto = (Produto) dao.findById(context, Produto.class, itemNF.getProduto().getId());
				
				produto.setPrecoVendaSugerido(itemNF.getValorUnitario());
				
				double valorUnitario = itemNF.getValorUnitario().doubleValue();
				double percentualDesconto =  itemNF.getPercentualDesconto().doubleValue();
				double valorSugerido = valorUnitario - (valorUnitario * (percentualDesconto / 100.00));
				valorSugerido *= 100.00;
				valorSugerido = Math.round(valorSugerido);
				valorSugerido /= 100.00;
				
				produto.setPrecoUltCompra(new BigDecimal(Double.toString(valorSugerido)));
				
				produto.setDataUltAlteracao(dataRegistro);
				produto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				dao.update(context, produto);				
			}
		}
		
	}

	@SuppressWarnings("unchecked")
	private void validaValoresNota(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		BigDecimal valorTotalItens = new BigDecimal("0.00");

		List<ItemNotaFiscal> itensNotaFiscal = (List<ItemNotaFiscal>)dao.findByFields(context, ItemNotaFiscal.class, "recuperaItensNotaFiscal", new String[]{"notaFiscal"}, new Object[]{notaFiscal});
		
		for (Object o : itensNotaFiscal) {
			ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
			
			// Evita os itens em branco e os marcados para exclusão
			if (itemNF.getProduto() != null && "N".equals(itemNF.getIndExcPlc()) && itemNF.getProduto().getId() != null && itemNF.getValorLiquido() != null) {
				valorTotalItens = valorTotalItens.add(itemNF.getValorLiquido());
			}
		}
		
		if (valorTotalItens.compareTo(notaFiscal.getValorTotal()) != 0) {
			throw new PlcException("{compra.erro.notaFiscal.valorTotal.divergente}", new Object[] {String.format("R$ %10.2f", notaFiscal.getValorTotal()), String.format("R$ %10.2f", valorTotalItens)});
		}
		
		notaFiscal.setItemNotaFiscal(itensNotaFiscal);
	}

	@SuppressWarnings("unchecked")
	private void cadastraProdutoFornecedor(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		
		for (Object o : notaFiscal.getItemNotaFiscal()) {
			ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
			
			// Evita os itens em branco
			if (itemNF.getProduto() != null && itemNF.getProduto().getId() != null) {
				
				if (!itemNF.isProdutoFornecedorExistente()) {
					FornecedorProduto entidadeArg =  new FornecedorProduto();
					entidadeArg.setFornecedor(notaFiscal.getFornecedor());
					entidadeArg.setCodigoProduto(itemNF.getCodigoProduto());
					
					List<FornecedorProduto> fornecedorProdutos = (List<FornecedorProduto>)dao.findList(context, entidadeArg, null, 0, 1);
					
					if (fornecedorProdutos == null || fornecedorProdutos.size() == 0) {
						FornecedorProduto fp = new FornecedorProduto();
						
						fp.setFornecedor(notaFiscal.getFornecedor());
						fp.setCodigoProduto(itemNF.getCodigoProduto());
						fp.setProduto(itemNF.getProduto());
						fp.setDataUltAlteracao(dataRegistro);
						fp.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
						
						dao.insert(context, fp);
						
						itemNF.setProdutoFornecedorExistente(true);
					}
				}
			}
		}
	}

	private void registraContasPagar(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		boolean informouAlgumValor = false;
		List<ContaPagar> contasPagar = notaFiscal.getContaPagar();
		
		for (Object o : contasPagar) {
			ContaPagar contaPagar = (ContaPagar)o;
			
			if (contaPagar.getValor() != null) {
				informouAlgumValor = true;
				break;
			}
		}
			
		if (!informouAlgumValor) {
			contasPagar.clear();
			Fornecedor fornecedor = (Fornecedor) dao.findById(context, Fornecedor.class, notaFiscal.getFornecedor().getId());
			
			if (notaFiscal.getParcelamento() != null || fornecedor.getParcelamentoPadrao() != null) {
				Parcelamento parcelamento = notaFiscal.getParcelamento() != null ? notaFiscal.getParcelamento() : fornecedor.getParcelamentoPadrao();
				
				if (parcelamento == null || parcelamento.getNumeroParcelas() == null) {
					throw new PlcException("{compra.erro.estoque.parcelamento.invalido}");
				}
				
				Calendar calendar = Calendar.getInstance();
				double somaParcelas = 0.0;
				int numParcelas = parcelamento.getNumeroParcelas();
				
				for(int i=0; i < numParcelas; i++) {
					ContaPagar contaPagar = new ContaPagar();
					contaPagar.setDataEmissao(notaFiscal.getDataEmissao());
					contaPagar.setObservacao(String.format("Parcela %d/%d. Parcelamento '%s'", i+1, numParcelas, parcelamento.getNome()));
					contaPagar.setStatus(StatusContaPagar.A);
					
					calendar.setTime(notaFiscal.getDataEmissao());
					calendar.add(Calendar.MONTH, i);
					calendar.add(Calendar.DAY_OF_MONTH, parcelamento.getCarenciaInicial());
					contaPagar.setDataVencimento(calendar.getTime());

					double valorTotal = notaFiscal.getValorTotal().doubleValue();
					double valorParcela = valorTotal * 100.0 / numParcelas;
					valorParcela = Math.round(valorParcela);
					valorParcela = valorParcela / 100.00;
					
					if (i < (numParcelas - 1)) {
						somaParcelas += valorParcela;
					} else {
						valorParcela = valorTotal - somaParcelas;
						valorParcela = valorParcela * 100.00;
						valorParcela = Math.round(valorParcela);
						valorParcela = valorParcela / 100.00;
					}
					
					contaPagar.setValor(new BigDecimal(Double.toString(valorParcela)));
					
					contaPagar.setNotaFiscal(notaFiscal);
					contaPagar.setDataUltAlteracao(dataRegistro);
					contaPagar.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
					contasPagar.add(contaPagar);
				}
			}
		}
	}

	/**
	 * Registra o movimento de entrada de produtos no estoque
	 * @param notaFsical Nota Fiscal contendo a relação dos produtos sendo comprados
	 */
	private void registraMovimento(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		Movimento mov = new MovimentoEntity();
		List<ItemMovimento> itensMovimento = new ArrayList<ItemMovimento>();
		
		for (Object o : notaFiscal.getItemNotaFiscal()) {
			ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
			
			// Evita os itens em branco
			if (itemNF.getProduto() != null && itemNF.getProduto().getId() != null) {
				ItemMovimento itemMovimento = new ItemMovimentoEntity();
				Produto produto = (Produto)itemNF.getProduto();
				
				itemMovimento.setProduto(produto);
				itemMovimento.setQuantidade(itemNF.getQuantidade());
				itemMovimento.setValorUnitario(itemNF.getValorUnitario());
				itemMovimento.setValorTotal(itemNF.getValorLiquido());
				itemMovimento.setTipo(TipoMovimento.E);
				itemMovimento.setMovimento(mov);
				
				itemMovimento.setDataUltAlteracao(dataRegistro);
				itemMovimento.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				itensMovimento.add(itemMovimento);
			}
		}

		mov.setData(dataRegistro);
		mov.setTipo(TipoMovimento.E);
		mov.setModo(ModoMovimento.N);
		mov.setValor(notaFiscal.getValorTotal());
		mov.setItemMovimento(itensMovimento);

		mov.setDataUltAlteracao(dataRegistro);
		mov.setUsuarioUltAlteracao(context.getUserProfile().getLogin());

		dao.insert(context, mov);
	}
	
	private Estoque insereEstoque(PlcBaseContextVO context, ItemNotaFiscal item, Date data, Localizacao localizacaoPadrao)  throws PlcException {

		Estoque estoque = new Estoque();
		
		estoque.setQuantidadeMinima(1);
		estoque.setQuantidade(item.getQuantidade());
		estoque.setQuantidadeMaxima(20);
		estoque.setDataConferencia(data);
		
		estoque.setDataUltAlteracao(data);
		estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		dao.insert(context, estoque);
		
		return estoque;		
	}

	/**
	 * Atualiza o saldo dos produtos sendo comprados no estoque
	 * @param notaFiscal Nota Fiscal com os itens da nota sendo comprados
	 * @param dataRegistro Data em que operacao foi realizada
	 * @throws PlcException
	 */
	private void atualizaEstoque(PlcBaseContextVO context, NotaFiscal notaFiscal, Date dataRegistro) throws PlcException {
		@SuppressWarnings("unchecked")
		List<Localizacao> localizacaoList = dao.findByFields(context, Localizacao.class, "queryCaixaEntrada", new String[] {"codigo"}, new String[] {"Novos Produtos"});

		if (localizacaoList.size() == 0) {
			throw new PlcException("{produto.erro.localizacao.entrada.inexistente}");
		}
		
		for (Object o : notaFiscal.getItemNotaFiscal()) {
			ItemNotaFiscal itemNF = (ItemNotaFiscal)o;

			if (itemNF.getProduto() != null) {
				Produto produto = itemNF.getProduto();
				Estoque estoque = produtoDAO.obterEstoqueProduto(context, produto);
				
				if (estoque != null) {
					estoque.setQuantidade(estoque.getQuantidade() + itemNF.getQuantidade());
				} else {
					estoque = insereEstoque(context, itemNF, dataRegistro, localizacaoList.get(0));
					produto.setEstoque(estoque);
					dao.update(context, estoque);
				}
			}
		}
	}
	
}
