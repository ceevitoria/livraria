package com.cee.livraria.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.bindingtype.PlcDeleteBefore;
import com.powerlogic.jcompany.model.bindingtype.PlcInsertAfter;

@SPlcRepository
@PlcAggregationDAOIoC(value=Produto.class)
public class ProdutoRepository extends PlcBaseRepository {

	@Inject
	private ProdutoDAO dao;

	public void antesExcluir (@Observes @PlcDeleteBefore PlcBaseContextVO context) throws PlcException {
		String url = context.getUrl();
		
		if ("produtoman.livroman.cdman.dvdman".contains(url)) {
			Produto produto = (Produto) context.getEntityForExtension();
			
			if (produto != null) {
				verificaExclusaoEstoque(context, produto);
			}
		}
	}
	
	public void aposIncluir (@Observes @PlcInsertAfter PlcBaseContextVO context) throws PlcException {
		String url = context.getUrl();
		
		if ("produtoman.livroman.cdman.dvdman".contains(url)) {
			Produto produto = (Produto) context.getEntityForExtension();
			
			if (produto != null) {
				realizaInclusaoEstoque(context, produto);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	private void realizaInclusaoEstoque(PlcBaseContextVO context, Produto produto) throws PlcException {
//		List<Estoque> estoqueList = dao.findByFields(context, Estoque.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
		
		if (produto != null) {
			Estoque estoque = dao.obterEstoqueProduto(context, produto);
		
			if (estoque == null) {
				estoque = new Estoque();
	
				estoque.setQuantidadeMinima(1);
				estoque.setQuantidade(0);
				estoque.setQuantidadeMaxima(20);
				estoque.setDataConferencia(null);
				
//				List<Localizacao> localizacaoList = dao.findByFields(context, Localizacao.class, "queryCaixaEntrada", new String[] {"codigo"}, new String[] {"Novos Produtos"});
//				
//				if (localizacaoList.size() == 0) {
//					throw new PlcException("{produto.erro.localizacao.entrada.inexistente}");
//				}
//				
//				Localizacao localizacao = localizacaoList.get(0);
//				estoque.setLocalizacao(localizacao);
				
				estoque.setDataUltAlteracao(new Date());
				estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				dao.insert(context, estoque);
				
				produto.setEstoque(estoque);
				dao.update(context, produto);
			}
		}
		
	}

	private void verificaExclusaoEstoque(PlcBaseContextVO context, Produto produto) throws PlcException {
//		List<Estoque> estoqueList = dao.findByFields(context, Estoque.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});

		if (produto != null) {
			Estoque estoque = dao.obterEstoqueProduto(context, produto);
		
			if (estoque != null) {
				estoque = produto.getEstoque();
				dao.delete(context, estoque);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public Collection recuperaProdutos(PlcBaseContextVO context, Produto arg, String orderByDinamico, int inicio, int total) throws PlcException {
		List<Produto> produtosRetorno = new ArrayList<Produto>();  
		
		// Recupera todos os produtos conforme filtro e paginação definida na tela de seleção
		List<Produto> produtosEncontrados = null;

//		produtosEncontrados = dao.findList(context, orderByDinamico, inicio, total, arg.getCodigoBarras(), arg.getTitulo(), arg.getPalavrasChave(), arg.getTipoProduto(), arg.getPrecoUltCompra(), arg.getLocalizacao());
		produtosEncontrados = (List<Produto>)dao.findList(context, arg, orderByDinamico, inicio, total);

		
		for (Produto produto: produtosEncontrados) {

			PrecoTabela precoTabela = dao.obterPrecoTabela(context, produto.getId());
			
			if (precoTabela != null && precoTabela.getIdTabela() != null) {
				produto.setPrecoTabela(precoTabela.getPrecoTabela());
				produto.setIdTabela(precoTabela.getIdTabela());
				produto.setNomeTabela(precoTabela.getNomeTabela());
				produto.setPrecoVendaSugerido(precoTabela.getPrecoTabela());
			}
			
			Estoque estoque = dao.obterEstoqueProduto(context, produto);
			produto.setEstoque(estoque);
			produto.setQuantidadeEstoque(estoque.getQuantidade());
			
			produtosRetorno.add(produto);
		}
		
		return produtosRetorno;
	}
	
}
