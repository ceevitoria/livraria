package com.cee.livraria.persistence.jpa.conferencia;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.ResultadoConferencia;
import com.cee.livraria.entity.estoque.conferencia.StatusConferencia;
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

@PlcAggregationDAOIoC(ConferenciaEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class ConferenciaDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<ConferenciaEntity> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="data", expression="data >= :data ") Date data,
			@PlcQueryParameter(name="statusConferencia", expression="statusConferencia = :statusConferencia") StatusConferencia statusConferencia,
			@PlcQueryParameter(name="resultado", expression="resultado = :resultado") ResultadoConferencia resultado
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="id", expression="id = :id") Long id,
			@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
			@PlcQueryParameter(name="data", expression="data >= :data ") Date data,
			@PlcQueryParameter(name="statusConferencia", expression="statusConferencia = :statusConferencia") StatusConferencia statusConferencia,
			@PlcQueryParameter(name="resultado", expression="resultado = :resultado") ResultadoConferencia resultado
	);
	
}
