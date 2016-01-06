package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.model.CaixaRepository;
import com.cee.livraria.model.VendaProdutosRepository;
import com.cee.livraria.model.ajuste.AjusteEstoqueRepository;
import com.cee.livraria.model.conferencia.ConferenciaRepository;
import com.cee.livraria.persistence.jpa.livro.LivroDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

@QPlcDefault
@SPlcFacade
public class AppFacadeImpl extends PlcFacadeImpl implements IAppFacade {

	@Inject
	private LivroDAO jpaDAO;
	
	@Inject
	private VendaProdutosRepository vendaProdutoRepository;
	
	@Inject
	private ConferenciaRepository conferenciaRepository;

	@Inject
	private AjusteEstoqueRepository ajusteEstoqueRepository;

	@Inject
	private CaixaRepository caixaRepository;
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public PrecoTabela findPrecoTabela(PlcBaseContextVO context, Long idLivro) throws PlcException {
		return jpaDAO.obterPrecoTabela(context, idLivro);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarVendaProdutos(PlcBaseContextVO context, List entityList, List pagtoList) throws PlcException {
		return vendaProdutoRepository.registrarVendaProdutos(context, entityList, pagtoList);
	}
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public BigDecimal buscarDadosVendaProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		return vendaProdutoRepository.buscarDadosVendaProdutos(context, entityList);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo,  CaixaEntity caixa, List itens) throws PlcException {
		return caixaRepository.registrarOperacaoCaixa(context, tipo, caixa, itens);
	}
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public PagamentoList obterPagamentosCaixa(PlcBaseContextVO context, Caixa caixa) throws PlcException {
		return caixaRepository.obterPagamentosCaixa(context, caixa);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig concluirConferenciaLivros(PlcBaseContextVO context, Conferencia conferencia) throws PlcException {
		return conferenciaRepository.concluirConferencia(context, conferencia);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig concluirAjusteEstoqueLivros(PlcBaseContextVO context, AjusteEstoque ajusteEstoque) throws PlcException {
		return ajusteEstoqueRepository.concluirAjusteEstoque(context, ajusteEstoque);
	}

	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public List<Estoque> buscarLivrosEstoque(PlcBaseContextVO context,	List<Livro> listaLivros) throws PlcException {
		List<Estoque> estoqueLista = new ArrayList<Estoque>(listaLivros.size());
		
		for (Livro livro: listaLivros) {
			List<Estoque> itens = jpaDAO.findByFields(context, EstoqueEntity.class, "querySelByLivro", new String[] {"livro"}, new Object[] {livro});
			estoqueLista.addAll(itens);
		}
		
		return estoqueLista;
	}
	
}
