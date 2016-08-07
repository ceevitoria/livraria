package com.cee.livraria.persistence.jpa.produto;

import java.math.BigDecimal;
import java.util.List;

import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.produto.CD;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
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

@PlcAggregationDAOIoC(CD.class)
@SPlcDataAccessObject
@PlcQueryService
public class CDDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<CD> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="artista", expression="obj.artista like '%' || :artista|| '%' ") String artista,
		@PlcQueryParameter(name="gravadora", expression="obj.gravadora like '%' || :gravadora|| '%' ") String gravadora,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal precoUltCompra,
		@PlcQueryParameter(name="localizacao", expression="obj.localizacao = :localizacao") Localizacao localizacao
		
	);

	@PlcQuery("querySel")
	public native Long findCount (
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="codigoBarras", expression="obj.codigoBarras = :codigoBarras") String codigoBarras,
		@PlcQueryParameter(name="titulo", expression="obj.titulo like '%' || :titulo || '%' ") String titulo,
		@PlcQueryParameter(name="artista", expression="obj.artista like '%' || :artista|| '%' ") String artista,
		@PlcQueryParameter(name="gravadora", expression="obj.gravadora like '%' || :gravadora|| '%' ") String gravadora,
		@PlcQueryParameter(name="palavrasChave", expression="obj.palavrasChave like '%' || :palavrasChave || '%' ") String palavrasChave,
		@PlcQueryParameter(name="precoUltCompra", expression="obj.precoUltCompra = :precoUltCompra") BigDecimal preco,
		@PlcQueryParameter(name="localizacao", expression="obj.localizacao = :localizacao") Localizacao localizacao
	);
}
