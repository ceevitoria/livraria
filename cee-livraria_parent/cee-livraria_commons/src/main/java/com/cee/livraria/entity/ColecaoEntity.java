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
@Table(name = "COLECAO")
@SequenceGenerator(name = "SE_COLECAO", sequenceName = "SE_COLECAO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="ColecaoEntity.queryMan", query="from ColecaoEntity"), @NamedQuery(name = "ColecaoEntity.querySelLookup", query = "select id as id, nome as nome from ColecaoEntity where id = ? order by id asc") })
public class ColecaoEntity extends Colecao {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public ColecaoEntity() {
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
