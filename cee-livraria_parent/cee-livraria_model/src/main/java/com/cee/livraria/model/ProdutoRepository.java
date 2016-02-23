package com.cee.livraria.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

@SPlcRepository
@PlcAggregationDAOIoC(value=Produto.class)
public class ProdutoRepository extends PlcBaseRepository {

	@Inject
	private ProdutoDAO dao;

	public Collection recuperaProdutos(PlcBaseContextVO context, Produto arg, String orderByDinamico, int inicio, int total) throws PlcException {

		// Recupera todos os projetos conforme filtro e paginação definida na tela de seleção
		List<Produto> produtos = dao.findList(context, orderByDinamico, inicio, total,
				arg.getCodigoBarras(), arg.getTitulo(), arg.getPalavrasChave(), arg.getTipoProduto(), arg.getPrecoUltCompra());

		
		List<Estoque> estoqueLista = new ArrayList<Estoque>(produtos.size());
		
		for (Produto produto: produtos) {
			List<Estoque> estoquesProduto = dao.findByFields(context, EstoqueEntity.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
			
			int quantidade = 0;
			
			for (Estoque estoque : estoquesProduto) {
				quantidade += estoque.getQuantidade();
			}
			
			produto.setQuantidadeEstoque(quantidade);
			
			PrecoTabela precoTabela = dao.obterPrecoTabela(context, produto.getId());
			
			if (precoTabela != null && precoTabela.getIdTabela() != null) {
				produto.setPrecoTabela(precoTabela.getPrecoTabela());
				produto.setIdTabela(precoTabela.getIdTabela());
				produto.setNomeTabela(precoTabela.getNomeTabela());
				produto.setPrecoVendaSugerido(precoTabela.getPrecoTabela());
			}
		}
		
		return produtos;
	}
	
}
