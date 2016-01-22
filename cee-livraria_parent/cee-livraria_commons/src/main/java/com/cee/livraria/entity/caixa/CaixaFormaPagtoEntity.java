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
@Table(name = "CAIXA_FORMAPAGTO")
@SequenceGenerator(name = "SE_CAIXAFORMAPAGTO", sequenceName = "SE_CAIXAFORMAPAGTO")
@Access(AccessType.FIELD)
@NamedQueries({
//	@NamedQuery(name="CaixaFormaPagtoEntity.querySel", query = "select id as id, data as data, valor as valor from CaixaFormaPagtoEntity where sistema = 'LIV'"),
	@NamedQuery(name="CaixaFormaPagtoEntity.querySel", query = "select obj.id as id, obj.data as data, obj.valor as valor, obj2.id as formaPagto_id, obj2.nome as formaPagto_nome from CaixaFormaPagtoEntity obj left outer join obj.formaPagto obj2 where obj.sistema = 'LIV'"),
	@NamedQuery(name="CaixaFormaPagtoEntity.querySelByCaixa", query = "select id as id, valor as valor, formaPagto as formaPagto from CaixaFormaPagtoEntity where sistema = 'LIV' and caixa = :caixa"),
	@NamedQuery(name="CaixaFormaPagtoEntity.querySelLookup", query = "select id as id, valor as valor from CaixaFormaPagtoEntity where id = ? order by id asc") })
public class CaixaFormaPagtoEntity extends CaixaFormaPagto {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public CaixaFormaPagtoEntity() {
	}

	@Override
	public String toString() {
		if (getValor() != null) { 
			return getValor().toPlainString();
		} else {
			return "N/A";
		}
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
