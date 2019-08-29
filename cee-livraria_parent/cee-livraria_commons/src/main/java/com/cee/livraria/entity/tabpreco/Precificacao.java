package com.cee.livraria.entity.tabpreco;

import java.io.Serializable;
import java.math.BigDecimal;

import com.cee.livraria.entity.AppBaseEntity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.EnumType;
import javax.persistence.Column;
import javax.persistence.Enumerated;
import javax.validation.constraints.Digits;
import javax.persistence.Access;
import javax.persistence.Embeddable;
import javax.persistence.AccessType;

@Embeddable
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "Precificacao.querySelLookup", query = "select quantidade as quantidade from Precificacao where id = ? order by quantidade asc") })
public class Precificacao implements Serializable {

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private TipoVariacao variacao;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private TipoPrecificacao tipo;

	@Digits(integer = 8, fraction = 2)
	private BigDecimal quantidade;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private TipoArredondamento arredondamento;

	public Precificacao() {
	}

	public TipoVariacao getVariacao() {
		return variacao;
	}

	public void setVariacao(TipoVariacao variacao) {
		this.variacao = variacao;
	}

	public TipoPrecificacao getTipo() {
		return tipo;
	}

	public void setTipo(TipoPrecificacao tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(BigDecimal quantidade) {
		this.quantidade = quantidade;
	}

	public TipoArredondamento getArredondamento() {
		return arredondamento;
	}

	public void setArredondamento(TipoArredondamento arredondamento) {
		this.arredondamento = arredondamento;
	}

	@Override
	public String toString() {
		return getQuantidade().toString();
	}

}
