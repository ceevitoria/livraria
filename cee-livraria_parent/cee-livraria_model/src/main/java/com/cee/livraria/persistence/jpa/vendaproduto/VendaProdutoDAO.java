package com.cee.livraria.persistence.jpa.vendaproduto;

import com.cee.livraria.entity.estoque.apoio.VendaProduto;
import com.cee.livraria.persistence.jpa.AppJpaDAO;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationDAOIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcDataAccessObject;
import com.powerlogic.jcompany.persistence.jpa.PlcQueryService;

/**
 * Classe de Persistência gerada pelo assistente
 */

@PlcAggregationDAOIoC(VendaProduto.class)
@SPlcDataAccessObject
@PlcQueryService
public class VendaProdutoDAO extends AppJpaDAO  {
	
}
