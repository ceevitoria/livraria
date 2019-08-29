package com.cee.livraria.entity;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "ESPIRITO")
@SequenceGenerator(name = "SE_ESPIRITO", sequenceName = "SE_ESPIRITO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="EspiritoEntity.queryMan", query="from EspiritoEntity"), 
	@NamedQuery(name="EspiritoEntity.querySel", query="select id as id, nome as nome from EspiritoEntity order by nome asc"), 
	@NamedQuery(name="EspiritoEntity.querySelLookup", query="select id as id, nome as nome from EspiritoEntity where id = ? order by nome asc") 
})
public class EspiritoEntity extends Espirito {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public EspiritoEntity() {
	}

	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
