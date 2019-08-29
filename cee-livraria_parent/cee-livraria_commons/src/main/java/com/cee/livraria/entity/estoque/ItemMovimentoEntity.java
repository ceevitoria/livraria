package com.cee.livraria.entity.estoque;

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
@Table(name = "ITEM_MOVIMENTO")
@SequenceGenerator(name = "SE_ITEM_MOVIMENTO", sequenceName = "SE_ITEM_MOVIMENTO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ItemMovimentoEntity.querySelLookup", query = "select id as id, produto as produto from ItemMovimentoEntity where id = ? order by id asc") })
public class ItemMovimentoEntity extends ItemMovimento {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public ItemMovimentoEntity() {
	}

	@Override
	public String toString() {
		return getProduto().toString();
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
