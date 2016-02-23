package com.cee.livraria.entity.estoque;

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
@Table(name = "ESTOQUE")
@SequenceGenerator(name = "SE_ESTOQUE", sequenceName = "SE_ESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name = "EstoqueEntity.querySelByProduto", query = "from EstoqueEntity where produto = :produto"),
	@NamedQuery(name = "EstoqueEntity.querySelByProdutoAndLocalizacao", query = "from EstoqueEntity where produto = :produto and localizacao = :localizacao"),
	@NamedQuery(name = "EstoqueEntity.querySelLookup", query = "select id as id, produto as produto from EstoqueEntity where id = ? order by id asc")})
public class EstoqueEntity extends Estoque {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public EstoqueEntity() {
	}

	@Override
	public String toString() {
		return getProduto().getTitulo() + "-" + getQuantidade().toString();
	}

}
