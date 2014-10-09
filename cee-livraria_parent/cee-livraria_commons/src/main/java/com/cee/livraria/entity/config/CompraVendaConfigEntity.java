package com.cee.livraria.entity.config;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
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
@Table(name = "COMPRA_VENDA_CONFIG")
@SequenceGenerator(name = "SE_COMPRA_VENDA_CONFIG", sequenceName = "SE_COMPRA_VENDA_CONFIG")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "CompraVendaConfigEntity.querySelLookup", query = "select id as id, exibirMensagemSucessoVenda as exibirMensagemSucessoVenda from CompraVendaConfigEntity where id = ? order by id asc") })
public class CompraVendaConfigEntity extends CompraVendaConfig {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public CompraVendaConfigEntity() {
	}

	@Override
	public String toString() {
		return getExibirMensagemSucessoVenda().toString();
	}

}
