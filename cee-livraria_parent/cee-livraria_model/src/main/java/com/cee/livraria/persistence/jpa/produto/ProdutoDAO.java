package com.cee.livraria.persistence.jpa.produto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;

import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.TipoProduto;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(Produto.class)
@SPlcDataAccessObject
@PlcQueryService
public class ProdutoDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<Produto> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="tipoProduto", expression="obj.tipoProduto = :tipoProduto") TipoProduto tipoProduto,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal precoUltCompra
	);

	@PlcQuery("querySel")
	public native Long findCount (
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="tipoProduto", expression="obj.tipoProduto = :tipoProduto") TipoProduto tipoProduto,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal preco
	);
	
	public PrecoTabela obterPrecoTabela(PlcBaseContextVO context, Long idProduto) throws PlcException {
		String queryPrecoTabela = null;
		PrecoTabela preco = null;
		
		if(context!=null && annotationPersistenceUtil.getNamedQueryByName(Produto.class, "queryPrecoTabela") != null) {
			queryPrecoTabela = annotationPersistenceUtil.getNamedQueryByName(Produto.class, "queryPrecoTabela").query();
		}
		
		if (queryPrecoTabela != null) {
			List entities = null;

			try {
				entities = apiCreateQuery(context, Livro.class, queryPrecoTabela).setParameter("id", idProduto).getResultList();
			} catch (NoResultException nre) {
				entities = null;
			}
			
			preco = new PrecoTabela();

			if (entities == null || entities.isEmpty()) {
				preco.setNomeTabela("Nenhuma precificação!");
			} else {
				Produto produto = (Produto)entities.get(0);
				preco.setIdTabela(produto.getIdTabela());
				preco.setNomeTabela(produto.getNomeTabela());
				preco.setPrecoTabela(produto.getPrecoTabela());
			}
		}
		
		return preco;
	}
}
