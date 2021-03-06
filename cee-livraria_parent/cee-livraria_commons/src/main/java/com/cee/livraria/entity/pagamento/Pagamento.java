package com.cee.livraria.entity.pagamento;

import java.math.BigDecimal;

import javax.validation.constraints.Digits;

public class Pagamento {

	private FormaPagto formaPagto;
	
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;

	private Boolean automatico;
	
	public Pagamento() {
		super();
	}
	
	public FormaPagto getFormaPagto() {
		return formaPagto;
	}

	public void setFormaPagto(FormaPagto formaPagto) {
		this.formaPagto = formaPagto;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public Boolean getAutomatico() {
		return automatico;
	}

	public void setAutomatico(Boolean automatico) {
		this.automatico = automatico;
	}
}
