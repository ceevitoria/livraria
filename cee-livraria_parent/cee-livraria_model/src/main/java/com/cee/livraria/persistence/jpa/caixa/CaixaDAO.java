package com.cee.livraria.persistence.jpa.caixa;

import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(CaixaEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class CaixaDAO extends AppJpaDAO  {

	
}
