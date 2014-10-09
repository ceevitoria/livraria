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
@Table(name = "OPERACAO_CAIXA_CONFIG")
@SequenceGenerator(name = "SE_OPERACAO_CAIXA_CONFIG", sequenceName = "SE_OPERACAO_CAIXA_CONFIG")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "OperacaoCaixaConfigEntity.querySelLookup", query = "select id as id, permitirFechamentoSaldoDivergente as permitirFechamentoSaldoDivergente from OperacaoCaixaConfigEntity where id = ? order by id asc") })
public class OperacaoCaixaConfigEntity extends OperacaoCaixaConfig {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public OperacaoCaixaConfigEntity() {
	}

	@Override
	public String toString() {
		return getPermitirFechamentoSaldoDivergente().toString();
	}

}
