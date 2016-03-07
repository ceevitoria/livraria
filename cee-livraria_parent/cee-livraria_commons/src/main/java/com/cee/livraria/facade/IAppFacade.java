package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;

public interface IAppFacade extends IPlcFacade {
	
	@SuppressWarnings("rawtypes")
	public Object findById(PlcBaseContextVO context, Class classe, Object id) throws PlcException;

	/**
	 * Encontra a tabela de preco vigente para produto informado
	 * @param context
	 * @param idProduto
	 * @return PrecoTabela
	 */
	public PrecoTabela findPrecoTabela(PlcBaseContextVO context, Long idProduto) throws PlcException;

	/**
	 * Registra a venda de produtos
	 * @param context Contexto da aplicacao
	 * @param entityList Relacao dos produtos sendo vendidos
	 * @param pagtoList Relacao dos pagamentos para a venda sendo realizada
	 * @return RetornoConfig Retorna com informacoes sobre a venda realizada (@see RetornoConfig)
	 */
	public RetornoConfig registrarVendaProdutos(PlcBaseContextVO context, List entityList, List pagtoList) throws PlcException;

	/**
	 * Busca os dados para a venda dos produtos
	 * @param context
	 * @param entityList
	 * @return BigDecimal Valor total da venda 
	 */
	public BigDecimal buscarDadosVendaProdutos(PlcBaseContextVO context, List entityList) throws PlcException;

	/**
	 * Registra um movimento do caixa 
	 * @param context Contexto da aplicacao
	 * @param CaixaEntity O caixa
	 * @param List itens Formas de pagamento relacionadas à operacao do caixa
	 * @return RetornoConfig Retorna com informacoes sobre a operacao do caixa (@see RetornoConfig)
	 */
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo, CaixaEntity caixa, List itens) throws PlcException;
	
	/**
	 * Recupera os pagamentos do caixa informado separados por forma de pagamento
	 * @param context
	 * @param caixa
	 * @return PagamentoList
	 */
	public PagamentoList obterPagamentosCaixa(PlcBaseContextVO context, Caixa caixa) throws PlcException;

	/**
	 * Efetua a conclusão de uma conferencia de livros
	 * @param context
	 * @param conferencia
	 * @return RetornoConfig com informacoes sobre a conclusao da conferencia (@see RetornoConfig)
	 */
	public RetornoConfig concluirConferenciaLivros(PlcBaseContextVO context, Conferencia conferencia) throws PlcException;
	
	/**
	 * Efetua a conclusão de um ajuste de estoque de livros
	 * @param context
	 * @param ajusteEstoque
	 * @return RetornoConfig com informacoes sobre a conclusao da conferencia (@see RetornoConfig)
	 */
	public RetornoConfig concluirAjusteEstoqueLivros(PlcBaseContextVO context, AjusteEstoque ajusteEstoque) throws PlcException;

	/**
	 * Busca os referidos produtos do estoque
	 * @param context
	 * @param entityList relação de produtos a serem recuperados do estoque
	 * @return itens do estoque relativos aos produtos informados
	 */
	public List<Estoque> buscarProdutosEstoque(PlcBaseContextVO context, List<Produto> listaProdutos) throws PlcException;
	
	/**
	 * Busca os referidos produtos que estão em determinada localizacao do estoque
	 * @param context
	 * @param entityList relação de produtos a serem recuperados do estoque
	 * @param localizacao A localização que se deseja encontrar os produtos informados
	 * @return itens do estoque relativos aos produtos informados
	 */
	public List<Estoque> buscarProdutosEstoquePorLocalizacao(PlcBaseContextVO context, List<Produto> listaProdutos, Localizacao localizacao) throws PlcException;
	
	/**
	 * Busca os referidos produtos que estão em determinada localizacao do estoque
	 * @param context
	 * @param produtoArg produto para ser usado como argumento a serem recuperados do estoque
	 * @param localizacao A localização que se deseja encontrar os produtos informados
	 * @return itens do estoque relativos aos produtos informados
	 */
	public List<Estoque> buscarProdutosEstoquePorLocalizacao(PlcBaseContextVO context, Produto produtoArg, Localizacao localizacao) throws PlcException;
	
	/**
	 * Registra a entrada de uma nota fiscal para o sistema
	 * @param context Contexto da aplicacao
	 * @param notaFiscal Nota fiscal com seus itens a ser registrada
	 * @return RetornoConfig Retorna com informacoes sobre entrada da nota fiscal (@see RetornoConfig)
	 */
	public RetornoConfig registrarEntradaNotaFiscal(PlcBaseContextVO context, NotaFiscal notaFiscal) throws PlcException;

	@SuppressWarnings("rawtypes")
	public Collection recuperarProdutos(PlcBaseContextVO context, Produto produtoArg, String orderByDinamico, int inicio, int total) throws PlcException;

	/**
	 * Registra a devolucao de produtos
	 * @param context Contexto da aplicacao
	 * @param entityList Relacao dos produtos sendo devolvidos
	 * @return RetornoConfig Retorna com informacoes sobre a venda realizada (@see RetornoConfig)
	 */
	public RetornoConfig registrarDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException;

	/**
	 * Busca os dados para a devolucao dos produtos
	 * @param context
	 * @param entityList
	 * @return BigDecimal Valor total da devolucao 
	 */
	public BigDecimal buscarDadosDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException;
	
	
	public byte[] gerarRelatorioFechamentoCaixa(PlcBaseContextVO context, String sheetName) throws PlcException;

	public Collection recuperaDadosFechamentoCaixa(PlcBaseContextVO context, String orderByDinamico, Integer inicio, Integer total) throws PlcException;
}
