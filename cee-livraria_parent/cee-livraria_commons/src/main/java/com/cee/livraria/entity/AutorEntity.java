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
@Table(name = "AUTOR")
@SequenceGenerator(name = "SE_AUTOR", sequenceName = "SE_AUTOR")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="AutorEntity.queryMan", query="from AutorEntity"), 
	@NamedQuery(name="AutorEntity.querySel", query="select id as id, nome as nome from AutorEntity order by nome asc"), 
	@NamedQuery(name="AutorEntity.querySelLookup", query="select id as id, nome as nome from AutorEntity where id = ? order by nome asc") 
})
public class AutorEntity extends Autor {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public AutorEntity() {
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
