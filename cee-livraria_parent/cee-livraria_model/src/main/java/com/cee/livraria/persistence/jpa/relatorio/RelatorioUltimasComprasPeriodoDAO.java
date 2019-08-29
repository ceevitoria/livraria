package com.cee.livraria.persistence.jpa.relatorio;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.relatorio.RelatorioUltimasComprasPeriodo;
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

@PlcAggregationDAOIoC(RelatorioUltimasComprasPeriodo.class)
@SPlcDataAccessObject
@PlcQueryService
public class RelatorioUltimasComprasPeriodoDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<RelatorioUltimasComprasPeriodo> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="dataInicio", expression="obj2.dataEmissao >= :dataInicio") Date dataInicio,
		@PlcQueryParameter(name="dataFim", expression="obj2.dataEmissao <= :dataFim") Date dataFim
	);

	@PlcQuery("querySel")
	public native Long findCount(
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="dataInicio", expression="obj2.dataEmissao >= :dataInicio") Date dataInicio,
		@PlcQueryParameter(name="dataFim", expression="obj2.dataEmissao <= :dataFim") Date dataFim
	);
	
}
