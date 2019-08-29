package com.cee.livraria.persistence.jpa.rest;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.NoResultException;

import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.TipoProduto;
import com.cee.livraria.entity.rest.ProdutoRest;
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

@PlcAggregationDAOIoC(ProdutoRest.class)
@SPlcDataAccessObject
@PlcQueryService
public class ProdutoRestDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<ProdutoRest> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras like :codigoBarras || '%' ") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="autor", expression="obj.autor like '%' || :autor || '%' ") String autor,
		@PlcQueryParameter(name="editora", expression="obj.editora like '%' || :editora || '%' ") String editora,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="tipoProduto", expression="obj.tipoProduto = :tipoProduto") String tipoProdutoId,
		@PlcQueryParameter(name="localizacaoCodigo", expression="obj.localizacaoCodigo like :localizacaoCodigo || '%' ") String localizacaoCodigo,
		@PlcQueryParameter(name="precoVenda", expression="obj.precoVenda = :precoVenda") BigDecimal precoVenda,
		@PlcQueryParameter(name="precoVendaMin", expression="obj.precoVenda >= :precoVendaMin") BigDecimal precoVendaMin,
		@PlcQueryParameter(name="precoVendaMax", expression="obj.precoVenda <= :precoVendaMax") BigDecimal precoVendaMax
		
	);

	@PlcQuery("querySel")
	public native Long findCount (
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras like :codigoBarras || '%' ") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="autor", expression="obj.autor like '%' || :autor || '%' ") String autor,
		@PlcQueryParameter(name="editora", expression="obj.editora like '%' || :editora || '%' ") String editora,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="tipoProduto", expression="obj.tipoProduto = :tipoProduto") String tipoProdutoId,
		@PlcQueryParameter(name="localizacaoCodigo", expression="obj.localizacaoCodigo like :localizacaoCodigo || '%' ") String localizacaoCodigo,
		@PlcQueryParameter(name="precoVenda", expression="obj.precoVenda = :precoVenda") BigDecimal precoVenda,
		@PlcQueryParameter(name="precoVendaMin", expression="obj.precoVenda >= :precoVendaMin") BigDecimal precoVendaMin,
		@PlcQueryParameter(name="precoVendaMax", expression="obj.precoVenda <= :precoVendaMax") BigDecimal precoVendaMax
	);
	
}
