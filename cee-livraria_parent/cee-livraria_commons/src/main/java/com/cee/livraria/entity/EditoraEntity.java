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
@Table(name = "EDITORA")
@SequenceGenerator(name = "SE_EDITORA", sequenceName = "SE_EDITORA")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="EditoraEntity.queryMan", query="from EditoraEntity"), @NamedQuery(name = "EditoraEntity.querySelLookup", query = "select id as id, nome as nome from EditoraEntity where id = ? order by id asc") })
public class EditoraEntity extends Editora {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public EditoraEntity() {
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
