package com.cee.livraria.persistence.jpa.fornecedor;

import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.entity.compra.Fornecedor;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.cee.livraria.entity.Cidade;
import com.cee.livraria.entity.Uf;

import java.util.List;

import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(Fornecedor.class)
@SPlcDataAccessObject
@PlcQueryService
public class FornecedorDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<Fornecedor> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="razaoSocial", expression="obj.razaoSocial like '%' || :razaoSocial || '%' ") String razaoSocial,
			@PlcQueryParameter(name="cnpj", expression="obj.cnpj like :cnpj || '%' ") String cnpj,
			@PlcQueryParameter(name="endereco_cidade", expression="obj1 = :endereco_cidade") Cidade endereco_cidade,
			@PlcQueryParameter(name="endereco_uf", expression="obj2 = :endereco_uf") Uf endereco_uf
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="obj.nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="razaoSocial", expression="obj.razaoSocial like '%' || :razaoSocial || '%' ") String razaoSocial,
			@PlcQueryParameter(name="cnpj", expression="obj.cnpj like :cnpj || '%' ") String cnpj,
			@PlcQueryParameter(name="endereco_cidade", expression="obj1 = :endereco_cidade") Cidade endereco_cidade,
			@PlcQueryParameter(name="endereco_uf", expression="obj2 = :endereco_uf") Uf endereco_uf
	);
	
}
