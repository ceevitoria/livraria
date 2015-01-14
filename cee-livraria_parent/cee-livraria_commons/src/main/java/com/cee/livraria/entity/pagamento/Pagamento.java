package com.cee.livraria.entity.pagamento;

import java.math.BigDecimal;

public class Pagamento {

	private FormaPagLivro formaPag;
	private BigDecimal valor;

	public Pagamento() {
		super();
	}
	
	public FormaPagLivro getFormaPag() {
		return formaPag;
	}

	public void setFormaPag(FormaPagLivro formaPag) {
		this.formaPag = formaPag;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
}
