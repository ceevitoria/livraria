package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.model.CaixaRepository;
import com.cee.livraria.model.DevolucaoProdutosRepository;
import com.cee.livraria.model.ProdutoRepository;
import com.cee.livraria.model.VendaProdutosRepository;
import com.cee.livraria.model.ajuste.AjusteEstoqueRepository;
import com.cee.livraria.model.compras.NotaFiscalRepository;
import com.cee.livraria.model.conferencia.ConferenciaRepository;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

@QPlcDefault
@SPlcFacade
public class AppFacadeImpl extends PlcFacadeImpl implements IAppFacade {

	@Inject @QPlcDefault
	AppJpaDAO dao;
	
	@Inject
	private ProdutoDAO produtoDAO;
	
	@Inject
	private VendaProdutosRepository vendaProdutoRepository;
	
	@Inject
	private DevolucaoProdutosRepository devolucaoProdutoRepository;
	
	@Inject
	private ConferenciaRepository conferenciaRepository;

	@Inject
	private AjusteEstoqueRepository ajusteEstoqueRepository;

	@Inject
	private NotaFiscalRepository notaFiscalRepository;

	@Inject
	private CaixaRepository caixaRepository;
	
	@Inject
	private ProdutoRepository produtoRepository;
	
	@PlcTransactional(commit=false)
	@Override
	public Object findById(PlcBaseContextVO context, Class classe, Object id) throws PlcException {
		return dao.findById(context, classe, id);
	}

	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public PrecoTabela findPrecoTabela(PlcBaseContextVO context, Long idProduto) throws PlcException {
		return produtoDAO.obterPrecoTabela(context, idProduto);
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
	public List<Estoque> buscarProdutosEstoque(PlcBaseContextVO context, List<Produto> listaProdutos) throws PlcException {
		List<Estoque> estoqueLista = new ArrayList<Estoque>(listaProdutos.size());
		
		for (Produto produto: listaProdutos) {
			List<Estoque> itens = produtoDAO.findByFields(context, EstoqueEntity.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
			estoqueLista.addAll(itens);
		}
		
		return estoqueLista;
	}


	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarEntradaNotaFiscal(PlcBaseContextVO context, NotaFiscal notaFiscal) throws PlcException {
		return notaFiscalRepository.registrarEntradaNotaFiscal(context, notaFiscal);
	}
	
	
	@PlcTransactional(commit=false)
	@Override
	public Collection recuperarProdutos(PlcBaseContextVO context, Produto produtoArg, String orderByDinamico, int inicio, int total) throws PlcException {
		return produtoRepository.recuperaProdutos(context, produtoArg, orderByDinamico, inicio, total);
	}
	
	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		return devolucaoProdutoRepository.registrarDevolucaoProdutos(context, entityList);
	}
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public BigDecimal buscarDadosDevolucaoProdutos(PlcBaseContextVO context, List entityList) throws PlcException {
		return devolucaoProdutoRepository.buscarDadosDevolucaoProdutos(context, entityList);
	}
	
	
}
