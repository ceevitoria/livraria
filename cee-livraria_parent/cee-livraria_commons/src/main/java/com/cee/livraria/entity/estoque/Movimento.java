package com.cee.livraria.entity.estoque;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class Movimento extends AppBaseEntity {
	private static final long serialVersionUID = -8683700782867114908L;

	@OneToMany (targetEntity = com.cee.livraria.entity.estoque.ItemMovimentoEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="movimento")
	@ForeignKey(name="FK_ITEMMOVIMENTO_MOVIMENTO")
	@PlcValDuplicity(property="livro")
	@PlcValMultiplicity(referenceProperty="produto",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.ItemMovimentoEntity}")
	@Valid
	private List<ItemMovimento> itemMovimento;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_MOVIMENTO")
	private Long id;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMovimento tipo;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private ModoMovimento modo;

	@NotNull
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoMovimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}

	public ModoMovimento getModo() {
		return modo;
	}

	public void setModo(ModoMovimento modo) {
		this.modo = modo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public List<ItemMovimento> getItemMovimento() {
		return itemMovimento;
	}

	public void setItemMovimento(List<ItemMovimento> itemMovimento) {
		this.itemMovimento=itemMovimento;
	}

}
