package com.cee.livraria.persistence.jpa.relatorio;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.relatorio.RelatorioFechamentoCaixa;
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

@PlcAggregationDAOIoC(RelatorioFechamentoCaixa.class)
@SPlcDataAccessObject
@PlcQueryService
public class RelatorioFechamentoCaixaDAO extends AppJpaDAO {

	@PlcQuery("querySel")
	public native List<RelatorioFechamentoCaixa> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc,
			@PlcQueryLineAmount Integer numeroLinhasPlc,
			@PlcQueryParameter(name="data", expression="obj.data >= :data") Date data);

	@PlcQuery("querySel")
	public native Long findCount(PlcBaseContextVO context,
			@PlcQueryParameter(name="data", expression="obj.data >= :data") Date data);
	
	@PlcQuery("queryMovimentoAbertura")
	public native RelatorioFechamentoCaixa obterMovimentoAberturaCaixa(PlcBaseContextVO context);

}
