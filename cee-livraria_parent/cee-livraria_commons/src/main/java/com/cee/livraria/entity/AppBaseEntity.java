/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */  
package com.cee.livraria.entity;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

import com.powerlogic.jcompany.domain.PlcBaseMapEntity;
/**
 * @since jCompany 3.2 Ancestral para todas as classes da aplicação, 
 * com pre-mapeamentos para OID, versao e auditoria minima herdados.
 */
@MappedSuperclass
public abstract class AppBaseEntity extends PlcBaseMapEntity implements Serializable {
	private static final long serialVersionUID = 1L;
}
