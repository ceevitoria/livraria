package com.cee.livraria.entity.produto;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Class CD
 */
@SPlcEntity
@Entity
@Table(name = "CD")
@SequenceGenerator(name = "SE_CD", sequenceName = "SE_CD")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="CD.queryMan", query="from CD"),
	@NamedQuery(name="CD.querySel", query="select obj.id as id, obj.tipoProduto as tipoProduto, obj.codigoBarras as codigoBarras, obj.titulo as titulo, obj.artista as artista, obj.gravadora as gravadora, obj.palavrasChave as palavrasChave, obj.precoUltCompra as precoUltCompra from CD obj order by obj.titulo asc"),
	@NamedQuery(name="CD.queryEdita", query="select obj from CD obj where obj.id = ?"),
	@NamedQuery(name="CD.querySelLookup", query="select id as id, codigoBarras as codigoBarras, titulo as titulo from CD where id = ? order by id asc") })
@DiscriminatorValue("C")
@ForeignKey(name = "FK_CD_PRODUTO")
public class CD extends Produto {
	private static final long serialVersionUID = -3743563441057503441L;
	
	@Column(length=1, insertable=true, updatable=false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto = TipoProduto.C;

	@Size(max = 100)
	private String artista;

	@Size(max = 100)
	private String gravadora;

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getGravadora() {
		return gravadora;
	}

	public void setGravadora(String gravadora) {
		this.gravadora = gravadora;
	}
	
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}
	
	/*
	 * Construtor padrao
	 */
	public CD() {
	}
	
//	@Override
//	public String toString() {
//		if (getTitulo() != null) {
//			return getCodigoBarras() + " - " + getTitulo();
//		} else {
//			return "CD";
//		}
//	}
	
}
