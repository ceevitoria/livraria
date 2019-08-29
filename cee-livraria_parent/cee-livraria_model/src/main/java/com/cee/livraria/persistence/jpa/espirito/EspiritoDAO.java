package com.cee.livraria.persistence.jpa.espirito;

import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.entity.EspiritoEntity;


import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(EspiritoEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class EspiritoDAO extends AppJpaDAO  {

	
}
