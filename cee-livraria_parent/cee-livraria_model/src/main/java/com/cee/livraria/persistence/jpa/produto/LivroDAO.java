package com.cee.livraria.persistence.jpa.produto;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;

import com.cee.livraria.entity.Autor;
import com.cee.livraria.entity.Colecao;
import com.cee.livraria.entity.Editora;
import com.cee.livraria.entity.Espirito;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Produto;
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

@PlcAggregationDAOIoC(Livro.class)
@SPlcDataAccessObject
@PlcQueryService
public class LivroDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<Livro> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="isbn", expression="obj.isbn = :isbn") String isbn,
		@PlcQueryParameter(name="edicao", expression="obj.edicao = :edicao") Integer edicao,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="espirito", expression="obj1 = :espirito") Espirito espirito,
		@PlcQueryParameter(name="autor", expression="obj2 = :autor") Autor autor,
		@PlcQueryParameter(name="editora", expression="obj3 = :editora") Editora editora,
		@PlcQueryParameter(name="colecao", expression="obj4 = :colecao") Colecao colecao,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal precoUltCompra
	);

	@PlcQuery("querySel")
	public native Long findCount (
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="isbn", expression="obj.isbn = :isbn") String isbn,
		@PlcQueryParameter(name="edicao", expression="obj.edicao = :edicao") Integer edicao,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="espirito", expression="obj1 = :espirito") Espirito espirito,
		@PlcQueryParameter(name="autor", expression="obj2 = :autor") Autor autor,
		@PlcQueryParameter(name="editora", expression="obj3 = :editora") Editora editora,
		@PlcQueryParameter(name="colecao", expression="obj4 = :colecao") Colecao colecao,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal preco
	);
	
	public PrecoTabela obterPrecoTabela(PlcBaseContextVO context, Long idProduto) throws PlcException {
		String queryPrecoTabela = null;
		PrecoTabela preco = null;
		
		if (context!=null && annotationPersistenceUtil.getNamedQueryByName(Produto.class, "queryPrecoTabela") != null) {
			queryPrecoTabela = annotationPersistenceUtil.getNamedQueryByName(Produto.class, "queryPrecoTabela").query();
		}
		
		if (queryPrecoTabela != null) {
			List entities = null;

			try {
				entities = apiCreateQuery(context, Produto.class, queryPrecoTabela).setParameter("id", idProduto).getResultList();
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
