package com.cee.livraria.persistence.jpa.notafiscal;

import java.util.Date;
import java.util.List;

import com.cee.livraria.entity.compra.Fornecedor;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.compra.StatusNotaFiscal;
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

@PlcAggregationDAOIoC(NotaFiscal.class)
@SPlcDataAccessObject
@PlcQueryService
public class NotaFiscalDAO extends AppJpaDAO  {

	@PlcQuery("querySel")
	public native List<NotaFiscal> findList(
			PlcBaseContextVO context,
			@PlcQueryOrderBy String dynamicOrderByPlc,
			@PlcQueryFirstLine Integer primeiraLinhaPlc, 
			@PlcQueryLineAmount Integer numeroLinhasPlc,		   
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="numero", expression="obj.numero like :numero || '%' ") String numero,
			@PlcQueryParameter(name="dataEmissao", expression="obj.dataEmissao >= :dataEmissao  ") Date dataEmissao,
			@PlcQueryParameter(name="dataEntrada", expression="obj.dataEntrada >= :dataEntrada  ") Date dataEntrada,
			@PlcQueryParameter(name="fornecedor", expression="obj1 = :fornecedor") Fornecedor fornecedor,
			@PlcQueryParameter(name="status", expression="obj.status = :status") StatusNotaFiscal status
	);

	@PlcQuery("querySel")
	public native Long findCount(
			PlcBaseContextVO context,
			
			@PlcQueryParameter(name="id", expression="obj.id = :id") Long id,
			@PlcQueryParameter(name="numero", expression="obj.numero like :numero || '%' ") String numero,
			@PlcQueryParameter(name="dataEmissao", expression="obj.dataEmissao >= :dataEmissao  ") Date dataEmissao,
			@PlcQueryParameter(name="dataEntrada", expression="obj.dataEntrada >= :dataEntrada  ") Date dataEntrada,
			@PlcQueryParameter(name="fornecedor", expression="obj1 = :fornecedor") Fornecedor fornecedor,
			@PlcQueryParameter(name="status", expression="obj.status = :status") StatusNotaFiscal status
	);
	
}
