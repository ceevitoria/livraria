package com.cee.livraria.persistence.jpa.ajuste;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
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

@PlcAggregationDAOIoC(AjusteEstoque.class)
@SPlcDataAccessObject
@PlcQueryService
public class AjusteEstoqueDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<AjusteEstoque> findList(
		PlcBaseContextVO context,
		@PlcQueryOrderBy String dynamicOrderByPlc,
		@PlcQueryFirstLine Integer primeiraLinhaPlc, 
		@PlcQueryLineAmount Integer numeroLinhasPlc,		   
		
		@PlcQueryParameter(name="id", expression="id = :id") Long id,
		@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
		@PlcQueryParameter(name="data", expression="data >= :data ") Date data,
		@PlcQueryParameter(name="statusAjuste", expression="statusAjuste = :statusAjuste") StatusAjuste statusAjuste
	);

	@PlcQuery("querySel")
	public native Long findCount(
		PlcBaseContextVO context,
		
		@PlcQueryParameter(name="id", expression="id = :id") Long id,
		@PlcQueryParameter(name="nome", expression="nome like '%' || :nome || '%' ") String nome,
		@PlcQueryParameter(name="data", expression="data >= :data ") Date data,
		@PlcQueryParameter(name="statusAjuste", expression="statusAjuste = :statusAjuste") StatusAjuste statusAjuste
	);
	
}
