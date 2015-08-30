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
@Table(name = "ESTOQUE")
@SequenceGenerator(name = "SE_ESTOQUE", sequenceName = "SE_ESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name = "EstoqueEntity.querySelByLivro", query = "from EstoqueEntity where livro = :livro"),
	@NamedQuery(name = "EstoqueEntity.querySelLookup", query = "select id as id, livro as livro from EstoqueEntity where id = ? order by id asc")})
public class EstoqueEntity extends Estoque {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public EstoqueEntity() {
	}

	@Override
	public String toString() {
		return getLivro().getTitulo() + "-" + getQuantidade().toString();
	}

//	@Override
//	public String toString() {
//		return getLivro().getTitulo() + "-" + getQuantidade().toString();
//	}
//	
}
