package com.cee.livraria.entity.estoque.conferencia;

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
@Table(name = "CONFERENCIA")
@SequenceGenerator(name = "SE_CONFERENCIA", sequenceName = "SE_CONFERENCIA")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="ConferenciaEntity.queryMan", query="from ConferenciaEntity"),
	@NamedQuery(name="ConferenciaEntity.querySel", query="select id as id, nome as nome, data as data, status as status from ConferenciaEntity order by nome asc"), 
	@NamedQuery(name="ConferenciaEntity.querySelLookup", query="select id as id, nome as nome from ConferenciaEntity where id = ? order by id asc") })
public class ConferenciaEntity extends Conferencia {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public ConferenciaEntity() {
	}

	@Override
	public String toString() {
		return getNome();
	}

}
