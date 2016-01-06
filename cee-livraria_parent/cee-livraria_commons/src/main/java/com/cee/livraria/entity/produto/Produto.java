package com.cee.livraria.entity.produto;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Estocavel;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="tipoProduto", length=1, discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("P")
@Access(AccessType.FIELD)
public abstract class Produto extends AppBaseEntity implements Estocavel {
	private static final long serialVersionUID = 7010726688930867174L;

	@NotNull
	@Column(length=1, insertable=false, updatable=false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;

	@NotNull
	@Size(max = 40)
	private String codigoBarras;

	@NotNull
	@Size(max = 200)
	private String titulo;

	@Size(max = 200)
	private String palavrasChave;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoUltCompra;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoVendaSugerido;
	
	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}
	
	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}
	
	public String getTitulo() {
		return titulo;
	}
	
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	
	public String getPalavrasChave() {
		return palavrasChave;
	}
	
	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}
	
	public BigDecimal getPrecoUltCompra() {
		return precoUltCompra;
	}
	
	public void setPrecoUltCompra(BigDecimal precoUltCompra) {
		this.precoUltCompra = precoUltCompra;
	}
	
	public BigDecimal getPrecoVendaSugerido() {
		return precoVendaSugerido;
	}
	
	public void setPrecoVendaSugerido(BigDecimal precoVendaSugerido) {
		this.precoVendaSugerido = precoVendaSugerido;
	}
	
}
