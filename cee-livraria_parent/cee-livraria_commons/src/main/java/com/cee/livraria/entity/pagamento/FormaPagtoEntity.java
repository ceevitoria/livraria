package com.cee.livraria.entity.pagamento;

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
@Table(name = "FORMA_PAGTO")
@SequenceGenerator(name = "SE_FORMA_PAGTO", sequenceName = "SE_FORMA_PAGTO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="FormaPagtoEntity.queryMan", query="from FormaPagtoEntity"), 
	@NamedQuery(name="FormaPagtoEntity.querySel", query="select id as id, nome as nome from FormaPagtoEntity order by nome asc"), 
	@NamedQuery(name="FormaPagtoEntity.querySelLookup", query="select id as id, nome as nome from FormaPagtoEntity where id = ? order by id asc") })
public class FormaPagtoEntity extends FormaPagto {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public FormaPagtoEntity() {
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
