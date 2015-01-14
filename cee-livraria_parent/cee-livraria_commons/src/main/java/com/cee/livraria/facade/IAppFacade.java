package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.List;

import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
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
	 * @return RetornoConfig Retorna com informacoes sobre a operacao do caixa (@see RetornoConfig)
	 */
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo, CaixaEntity caixa) throws PlcException;
	
}
