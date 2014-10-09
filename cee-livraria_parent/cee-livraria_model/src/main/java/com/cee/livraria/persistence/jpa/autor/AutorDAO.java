package com.cee.livraria.persistence.jpa.autor;

import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.cee.livraria.entity.AutorEntity;


import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(AutorEntity.class)
@SPlcDataAccessObject
@PlcQueryService
public class AutorDAO extends AppJpaDAO  {

	
}
