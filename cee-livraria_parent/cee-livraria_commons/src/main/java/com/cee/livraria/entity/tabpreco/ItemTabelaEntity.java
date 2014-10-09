package com.cee.livraria.entity.tabpreco;

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
@Table(name = "ITEM_TABELA")
@SequenceGenerator(name = "SE_ITEM_TABELA", sequenceName = "SE_ITEM_TABELA")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ItemTabelaEntity.querySelLookup", query = "select id as id, livro as livro from ItemTabelaEntity where id = ? order by titulo asc") })
public class ItemTabelaEntity extends ItemTabela {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public ItemTabelaEntity() {
	}

	@Override
	public String toString() {
		return getLivro().toString();
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
