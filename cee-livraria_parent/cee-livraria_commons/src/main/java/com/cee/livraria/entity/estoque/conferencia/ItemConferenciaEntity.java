package com.cee.livraria.entity.estoque.conferencia;

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
@Table(name = "ITEM_CONFERENCIA")
@SequenceGenerator(name = "SE_ITEM_CONFERENCIA", sequenceName = "SE_ITEM_CONFERENCIA")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ItemConferenciaEntity.querySelLookup", query = "select id as id, livro as livro from ItemConferenciaEntity where id = ? order by id asc") })
public class ItemConferenciaEntity extends ItemConferencia {

	private transient String livroAuxLookup;

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public ItemConferenciaEntity() {
	}

	@Override
	public String toString() {

		if (getLivro() != null && getLivro().getTitulo() != null) {
			return getLivro().getTitulo();
		}

		return "";
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	public void setLivroAuxLookup(String livroAuxLookup) {
		this.livroAuxLookup = livroAuxLookup;
	}

}
