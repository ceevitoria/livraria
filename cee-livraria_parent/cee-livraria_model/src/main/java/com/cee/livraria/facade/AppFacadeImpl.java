package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

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
import com.cee.livraria.entity.relatorio.RelatorioUltimasComprasPeriodo;
import com.cee.livraria.entity.relatorio.RelatorioVendaPeriodo;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.model.CaixaRepository;
import com.cee.livraria.model.DevolucaoProdutosRepository;
import com.cee.livraria.model.ProdutoRepository;
import com.cee.livraria.model.VendaProdutosRepository;
import com.cee.livraria.model.ajuste.AjusteEstoqueRepository;
import com.cee.livraria.model.compras.NotaFiscalRepository;
import com.cee.livraria.model.conferencia.ConferenciaRepository;
import com.cee.livraria.model.relatorio.RelatorioFechamentoCaixaRepository;
import com.cee.livraria.model.relatorio.RelatorioUltimasComprasPeriodoRepository;
import com.cee.livraria.model.relatorio.RelatorioVendaPeriodoRepository;
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
	
	@Inject
	private RelatorioFechamentoCaixaRepository relatorioFechamentoCaixaRepository;

	@Inject
	private RelatorioVendaPeriodoRepository relatorioVendaPeriodoRepository;
	
	@Inject
	private RelatorioUltimasComprasPeriodoRepository relatorioUltimasComprasPeriodoRepository;
	
	@PlcTransactional(commit=false)
	@Override
	public Object findById(PlcBaseContextVO context, Class classe, Object id) throws PlcException {
		return dao.findById(context, classe, id);
	}

	@PlcTransactional(commit=false)
	@Override
	public List findByFields(PlcBaseContextVO context, Class classe, String namedQuery, String[] parametros, Object[] valores) {
		return dao.findByFields(context, classe, namedQuery, parametros, valores);
	}
	
	@PlcTransactional(commit=false)
	public Long findCountProduto(PlcBaseContextVO context, Produto entidadeArg) throws PlcException {
		Long count = null;
		
		if (entidadeArg.getLocalizacao() == null) {
			count = produtoDAO.findCount(context, entidadeArg);
		} else {
			count = produtoDAO.findCount(context, entidadeArg.getCodigoBarras(), entidadeArg.getTitulo(), entidadeArg.getPalavrasChave(), entidadeArg.getTipoProduto(), entidadeArg.getPrecoVendaSugerido(), entidadeArg.getLocalizacao());
		}
		
		return count; 
	}

	@PlcTransactional(commit=false)
	@Override
	public Estoque obterEstoqueProduto(PlcBaseContextVO context, Produto produto) throws PlcException {
		return produtoDAO.obterEstoqueProduto(context, produto);
	}
	
	@PlcTransactional(commit=false)
	@Override
	public Localizacao obterLocalizacaoProduto(PlcBaseContextVO context, Produto produto) throws PlcException {
		return produtoDAO.obterLocalizacaoProduto(context, produto);
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

//	@PlcTransactional(commit=false)
//	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
//	@Override
//	public List<Estoque> buscarProdutosEstoque(PlcBaseContextVO context, List<Produto> listaProdutos) throws PlcException {
//		List<Estoque> estoqueLista = new ArrayList<Estoque>(listaProdutos.size());
//		
//		for (Produto produto: listaProdutos) {
//			List<Estoque> itens = produtoDAO.findByFields(context, Estoque.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
//			estoqueLista.addAll(itens);
//		}
//		
//		return estoqueLista;
//	}

//	@PlcTransactional(commit=false)
//	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
//	@Override
//	public List<Estoque> buscarProdutosEstoquePorLocalizacao(PlcBaseContextVO context, List<Produto> listaProdutos, Localizacao localizacao) throws PlcException {
//		List<Estoque> estoqueLista = new ArrayList<Estoque>(listaProdutos.size());
//		
//		for (Produto produto: listaProdutos) {
//			List<Estoque> itens = produtoDAO.findByFields(context, Estoque.class, "querySelByProdutoAndLocalizacao", new String[] {"produto", "localizacao"}, new Object[] {produto, localizacao});
//			estoqueLista.addAll(itens);
//		}
//		
//		return estoqueLista;
//	}

//	@PlcTransactional(commit=false)
//	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
//	@Override
//	public List<Estoque> buscarProdutosEstoquePorLocalizacao(PlcBaseContextVO context, Produto produtoArg, Localizacao localizacao) throws PlcException {
//		produtoArg.setLocalizacao(null);
//		
//		Estoque estoqueArg = new Estoque();
//		
//		estoqueArg.setProduto(produtoArg);
//		estoqueArg.setLocalizacao(localizacao);
//		
//		List<Estoque> itens = dao.findList(context, estoqueArg, null, 0, 0);
//		
//		return itens;
//	}

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
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public byte[] gerarRelatorioFechamentoCaixa(PlcBaseContextVO context, String sheetName) throws PlcException {
		return relatorioFechamentoCaixaRepository.gerarRelatorioFechamentoCaixa(context, sheetName);
	}

	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public Collection recuperaDadosFechamentoCaixa(PlcBaseContextVO context, String orderByDinamico, Integer inicio, Integer total) throws PlcException {
		return relatorioFechamentoCaixaRepository.recuperaDadosFechamentoCaixa(context, orderByDinamico, inicio, total);
	}

	/*
	@Override
	public Collection findList(PlcBaseContextVO context, Object entidadeArg, String orderByDinamico, int primeiraLinha, int maximoLinhas) {

		if (entidadeArg instanceof ProdutoRest) {
			maximoLinhas = 300;
		}
		
		return super.findList(context, entidadeArg, orderByDinamico, primeiraLinha, maximoLinhas);
	}
	*/

	@PlcTransactional(commit=false)
	@Override
	public byte[] gerarRelatorioVendaPeriodo(PlcBaseContextVO context, RelatorioVendaPeriodo relatorioArg, String sheetName) throws PlcException {
		return relatorioVendaPeriodoRepository.gerarRelatorioVendaPeriodo(context, relatorioArg, sheetName);
	}

	public Collection recuperaVendaPeriodo(PlcBaseContextVO context, RelatorioVendaPeriodo relatorioArg, String orderByDinamico, Integer inicio, Integer total) throws PlcException {
		return relatorioVendaPeriodoRepository.recuperaVendaPeriodo(context, relatorioArg, orderByDinamico, inicio, total);
	}
	
	@PlcTransactional(commit=false)
	@Override
	public byte[] gerarRelatorioUltimasComprasPeriodo(PlcBaseContextVO context, RelatorioUltimasComprasPeriodo relatorioArg, String sheetName) throws PlcException {
		return relatorioUltimasComprasPeriodoRepository.gerarRelatorioUltimasComprasPeriodo(context, relatorioArg, sheetName);
	}
	
	public Collection recuperaUltimasComprasPeriodo(PlcBaseContextVO context, RelatorioUltimasComprasPeriodo relatorioArg, String orderByDinamico, Integer inicio, Integer total) throws PlcException {
		return relatorioUltimasComprasPeriodoRepository.recuperaUltimasComprasPeriodo(context, relatorioArg, orderByDinamico, inicio, total);
	}
}
