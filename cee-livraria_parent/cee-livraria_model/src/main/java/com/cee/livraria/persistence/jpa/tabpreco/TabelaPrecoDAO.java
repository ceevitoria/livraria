package com.cee.livraria.persistence.jpa.tabpreco;

import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.entity.tabpreco.TabelaPrecoEntity;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryParameter;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import java.util.Date;

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

@PlcAggregationDAOIoC(TabelaPrecoEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class TabelaPrecoDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<TabelaPrecoEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="ativa", expression="ativa = :ativa") PlcYesNo ativa,
			@PlcQueryParameter(name="dataInicio", expression="dataInicio >= :dataInicio ") Date dataInicio,
			@PlcQueryParameter(name="dataFim", expression="dataFim <= :dataFim ") Date dataFim
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="ativa", expression="ativa = :ativa") PlcYesNo ativa,
			@PlcQueryParameter(name="dataInicio", expression="dataInicio >= :dataInicio ") Date dataInicio,
			@PlcQueryParameter(name="dataFim", expression="dataFim <= :dataFim ") Date dataFim
	);
	
}
