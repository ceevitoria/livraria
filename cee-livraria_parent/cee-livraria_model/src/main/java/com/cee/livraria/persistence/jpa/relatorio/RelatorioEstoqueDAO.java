package com.cee.livraria.persistence.jpa.relatorio;

import java.util.List;

import com.cee.livraria.entity.relatorio.RelatorioEstoque;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQuery;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryFirstLine;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryLineAmount;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryOrderBy;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;
/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(RelatorioEstoque.class)
@SPlcDataAccessObject
@PlcQueryService
public class RelatorioEstoqueDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<RelatorioEstoque> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc
	);

	@PlcQuery("querySel")
	public native Long findCount(
		PlcBaseContextVO context
	);
	
}
