package com.cee.livraria.entity.caixa;

import java.math.BigDecimal;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import javax.persistence.Transient;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "CAIXA")
@SequenceGenerator(name = "SE_CAIXA", sequenceName = "SE_CAIXA")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="CaixaEntity.queryMan", query="from CaixaEntity where sistema = 'LIV'"),
	@NamedQuery(name="CaixaEntity.querySel", query="select id as id, status as status, saldo as saldo from CaixaEntity where sistema = 'LIV'"),
	@NamedQuery(name="CaixaEntity.querySelLookup", query="select id as id, saldo as saldo from CaixaEntity where id = ? and sistema = 'LIV' order by id asc") })
public class CaixaEntity extends Caixa {

	private static final long serialVersionUID = 1L;

	@Transient
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;
	
	@Transient
	@Size(max = 500)
	private String observacao;
	
	public BigDecimal getValor() {
		return valor;
	}
	
	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	
	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
	
	/*
	 * Construtor padrao
	 */
	public CaixaEntity() {
	}

	@Override
	public String toString() {
		if (getStatus() != null && getSaldo() != null) {
			return ("A".equals(getStatus().toString()) ? "[Aberto" : "[Fechado") + " - R$ " + getSaldo().toString() + "]";
		} else {
			return "Caixa";
		}
	}

}
