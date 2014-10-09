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
@Table(name = "LOCALIZACAO")
@SequenceGenerator(name = "SE_LOCALIZACAO", sequenceName = "SE_LOCALIZACAO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="LocalizacaoEntity.queryMan", query="from LocalizacaoEntity"), 
	@NamedQuery(name = "LocalizacaoEntity.querySelLookup", query = "select id as id, codigo as codigo from LocalizacaoEntity where id = ? order by id asc") })
public class LocalizacaoEntity extends Localizacao {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public LocalizacaoEntity() {
	}

	@Override
	public String toString() {
		return getCodigo();
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
