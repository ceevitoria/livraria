package com.cee.livraria.entity.caixa;

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
@Table(name = "CAIXA_MOVIMENTO")
@SequenceGenerator(name = "SE_CAIXA_MOVIMENTO", sequenceName = "SE_CAIXA_MOVIMENTO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "CaixaMovimentoEntity.querySelLookup", query = "select id as id, valor as valor from CaixaMovimentoEntity where id = ? order by id asc") })
public class CaixaMovimentoEntity extends CaixaMovimento {
	private static final long serialVersionUID = 4214193142279237948L;

	/*
	 * Construtor padrao
	 */
	public CaixaMovimentoEntity() {
	}

	@Override
	public String toString() {
		return getValor().toPlainString();
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
