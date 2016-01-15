package com.cee.livraria.persistence.jpa.parcelamento;

import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.entity.compra.Parcelamento;


import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(Parcelamento.class)
@SPlcDataAccessObject
@PlcQueryService
public class ParcelamentoDAO extends AppJpaDAO  {

	
}
