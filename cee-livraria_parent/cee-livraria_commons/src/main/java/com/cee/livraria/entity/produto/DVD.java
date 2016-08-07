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
 * Class DVD
 */
@SPlcEntity
@Entity
@Table(name = "DVD")
@SequenceGenerator(name = "SE_DVD", sequenceName = "SE_DVD")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="DVD.queryMan", query="from DVD"),
	@NamedQuery(name="DVD.querySel", query="select obj.id as id, obj.tipoProduto as tipoProduto, obj.codigoBarras as codigoBarras, obj.titulo as titulo, obj.artista as artista, obj.gravadora as gravadora, obj.palavrasChave as palavrasChave, obj.precoUltCompra as precoUltCompra, obj.precoVendaSugerido as precoVendaSugerido from DVD obj order by obj.titulo asc"),
	@NamedQuery(name="DVD.queryEdita", query="select obj from DVD obj where obj.id = ?"),
	@NamedQuery(name="DVD.querySelLookup", query="select id as id, codigoBarras as codigoBarras, titulo as titulo from DVD where id = ? order by id asc") })
@DiscriminatorValue("D")
@ForeignKey(name = "FK_DVD_PRODUTO")
public class DVD extends Produto {
	private static final long serialVersionUID = 7797394047043215945L;
	
	@Column(length=1, insertable=true, updatable=false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto = TipoProduto.D;
	
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
	public DVD() {
	}
	
//	@Override
//	public String toString() {
//		if (getTitulo() != null) {
//			return getCodigoBarras() + " - " + getTitulo();
//		} else {
//			return "DVD";
//		}
//	}
	
}
