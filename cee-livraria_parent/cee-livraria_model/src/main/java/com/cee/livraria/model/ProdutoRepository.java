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
import com.powerlogic.jcompany.model.bindingtype.PlcInsertBefore;

@SPlcRepository
@PlcAggregationDAOIoC(value=Produto.class)
public class ProdutoRepository extends PlcBaseRepository {

	@Inject
	private ProdutoDAO dao;

	public void antesIncluir (@Observes @PlcInsertBefore PlcBaseContextVO context) throws PlcException {
		String url = context.getUrl();
		
		if ("produtoman.livroman.cdman.dvdman".contains(url)) {
			Produto produto = (Produto) context.getEntityForExtension();
			
			if (produto != null) {
				realizaInclusaoEstoque(context, produto);
			}
		}
	}
	
	private void realizaInclusaoEstoque(PlcBaseContextVO context, Produto produto) throws PlcException {
		
		if (produto != null) {
			Estoque estoque = produto.getEstoque();
		
			if (estoque == null) {
				estoque = new Estoque();
	
				estoque.setQuantidadeMinima(1);
				estoque.setQuantidade(0);
				estoque.setQuantidadeMaxima(20);
				estoque.setDataConferencia(null);
				
				estoque.setDataUltAlteracao(new Date());
				estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				
				dao.insert(context, estoque);
				
				produto.setEstoque(estoque);
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
