package com.cee.livraria.entity.config;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "CONFERENCIA_CONFIG")
@SequenceGenerator(name = "SE_CONFERENCIA_CONFIG", sequenceName = "SE_CONFERENCIA_CONFIG")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ConferenciaConfigEntity.querySelLookup", query = "select id as id, alertaTrocaLocalizacaoLivros as alertaTrocaLocalizacaoLivros from ConferenciaConfigEntity where id = ? order by id asc") })
public class ConferenciaConfigEntity extends ConferenciaConfig {

	private static final long serialVersionUID = 2L;

	/*
	 * Construtor padrao
	 */
	public ConferenciaConfigEntity() {
	}

	@Override
	public String toString() {
		return getAlertaTrocaLocalizacaoLivros().toString();
	}

}
