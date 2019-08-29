package com.cee.livraria.entity.estoque;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "MOVIMENTO")
@SequenceGenerator(name = "SE_MOVIMENTO", sequenceName = "SE_MOVIMENTO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "MovimentoEntity.querySelLookup", query = "select id as id, data as data from MovimentoEntity where id = ? order by id asc") })
public class MovimentoEntity extends Movimento {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public MovimentoEntity() {
	}

	@Override
	public String toString() {
		return getData().toString();
	}

}
