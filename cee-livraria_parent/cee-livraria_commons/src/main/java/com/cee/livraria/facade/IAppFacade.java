package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.List;

import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.facade.IPlcFacade;

public interface IAppFacade extends IPlcFacade {
	
	/**
	 * Encontra a tabela de preco vigente para livro informado
	 * @param context
	 * @param idLivro
	 * @return PrecoTabela
	 */
	public PrecoTabela findPrecoTabela(PlcBaseContextVO context, Long idLivro) throws PlcException;

	/**
	 * Registra a venda de livros
	 * @param context Contexto da aplicacao
	 * @param entityList Relacao dos livros sendo vendidos
	 * @param pagtoList Relacao dos pagamentos para a venda sendo realizada
	 * @return RetornoConfig Retorna com informacoes sobre a venda realizada (@see RetornoConfig)
	 */
	public RetornoConfig registrarVendaLivros(PlcBaseContextVO context, List entityList, List pagtoList) throws PlcException;

	/**
	 * Busca os dados para a venda dos livros
	 * @param context
	 * @param entityList
	 * @return BigDecimal Valor total da venda 
	 */
	public BigDecimal buscarDadosVendaLivros(PlcBaseContextVO context, List entityList) throws PlcException;

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
	 * Busca os referidos livros do estoque
	 * @param context
	 * @param entityList relação de livros a serem recuperados do estoque
	 * @return itens do estoque relativos ao livros informados
	 */
	public List<Estoque> buscarLivrosEstoque(PlcBaseContextVO context, List<Livro> listaLivros) throws PlcException;
	
}
