package com.cee.livraria.entity.caixa;

import java.math.BigDecimal;
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
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class Caixa extends AppBaseEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CAIXA")
	private Long id;

	@NotNull
	@Column(length = 3)
	private String sistema = "LIV";
	
	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private StatusCaixa status;

	@NotNull
	@Digits(integer = 10, fraction = 2)
	private BigDecimal saldo;

	@OneToMany(targetEntity = com.cee.livraria.entity.caixa.CaixaMovimentoEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "caixa")
	@ForeignKey(name = "FK_CAIXAMOVIMENTO_CAIXA")
	@PlcValDuplicity(property = "observacao")
	@PlcValMultiplicity(referenceProperty = "observacao", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.CaixaMovimentoEntity}")
	@Valid
	private List<CaixaMovimento> caixaMovimento;

	@OneToMany(targetEntity = com.cee.livraria.entity.caixa.CaixaFormaPagtoEntity.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "caixa")
	@ForeignKey(name = "FK_CAIXAFORMAPAGTO_CAIXA")
	@PlcValDuplicity(property = "observacao")
	@PlcValMultiplicity(referenceProperty = "observacao", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.CaixaFormaPagtoEntity}")
	@Valid
	private List<CaixaFormaPagto> caixaFormaPagto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public StatusCaixa getStatus() {
		return status;
	}

	public void setStatus(StatusCaixa status) {
		this.status = status;
	}

	public BigDecimal getSaldo() {
		return saldo;
	}

	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}

	public List<CaixaMovimento> getCaixaMovimento() {
		return caixaMovimento;
	}

	public void setCaixaMovimento(List<CaixaMovimento> caixaMovimento) {
		this.caixaMovimento = caixaMovimento;
	}

	public List<CaixaFormaPagto> getCaixaFormaPagto() {
		return caixaFormaPagto;
	}

	public void setCaixaFormaPagto(List<CaixaFormaPagto> caixaFormaPagto) {
		this.caixaFormaPagto = caixaFormaPagto;
	}

}
