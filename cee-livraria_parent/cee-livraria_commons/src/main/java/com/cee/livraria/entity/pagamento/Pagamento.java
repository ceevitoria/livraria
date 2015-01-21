package com.cee.livraria.entity.pagamento;

import java.math.BigDecimal;

public class Pagamento {

	private FormaPagto formaPagto;
	private BigDecimal valor;

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
}
