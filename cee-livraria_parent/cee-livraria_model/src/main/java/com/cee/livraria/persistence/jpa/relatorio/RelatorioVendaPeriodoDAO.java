package com.cee.livraria.persistence.jpa.relatorio;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.relatorio.RelatorioVendaPeriodo;
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

@PlcAggregationDAOIoC(RelatorioVendaPeriodo.class)
@SPlcDataAccessObject
@PlcQueryService
public class RelatorioVendaPeriodoDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<RelatorioVendaPeriodo> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="dataInicio", expression="obj2.data >= :dataInicio") Date dataInicio,
		@PlcQueryParameter(name="dataFim", expression="obj2.data <= :dataFim") Date dataFim
	);

	@PlcQuery("querySel")
	public native Long findCount(
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="dataInicio", expression="obj2.data >= :dataInicio") Date dataInicio,
		@PlcQueryParameter(name="dataFim", expression="obj2.data < :dataFim") Date dataFim
	);
	
}
