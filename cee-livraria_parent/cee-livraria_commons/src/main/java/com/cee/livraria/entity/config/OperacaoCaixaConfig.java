package com.cee.livraria.entity.config;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;

import com.powerlogic.jcompany.domain.type.PlcYesNo;

@MappedSuperclass
public abstract class OperacaoCaixaConfig extends Config {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_OPERACAO_CAIXA_CONFIG")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo permitirFechamentoSaldoDivergente = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo permitirAjusteSaldoDivergente = PlcYesNo.S;
	
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorMaximoAjusteSaldoDivergente = new BigDecimal(0.10);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlcYesNo getPermitirFechamentoSaldoDivergente() {
		return permitirFechamentoSaldoDivergente;
	}

	public void setPermitirFechamentoSaldoDivergente(
			PlcYesNo permitirFechamentoSaldoDivergente) {
		this.permitirFechamentoSaldoDivergente = permitirFechamentoSaldoDivergente;
	}

	public PlcYesNo getPermitirAjusteSaldoDivergente() {
		return permitirAjusteSaldoDivergente;
	}

	public void setPermitirAjusteSaldoDivergente(
			PlcYesNo permitirAjusteSaldoDivergente) {
		this.permitirAjusteSaldoDivergente = permitirAjusteSaldoDivergente;
	}

	public BigDecimal getValorMaximoAjusteSaldoDivergente() {
		return valorMaximoAjusteSaldoDivergente;
	}

	public void setValorMaximoAjusteSaldoDivergente(
			BigDecimal valorMaximoAjusteSaldoDivergente) {
		this.valorMaximoAjusteSaldoDivergente = valorMaximoAjusteSaldoDivergente;
	}
	
	
}
