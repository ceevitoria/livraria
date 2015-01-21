package com.cee.livraria.entity.caixa.apoio;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.validation.constraints.Digits;

import com.cee.livraria.entity.caixa.StatusCaixa;
import com.cee.livraria.entity.pagamento.FormaPagto;

@Access(AccessType.FIELD)
public class CaixaConsulta {

	@Id
	private Long id;

	private StatusCaixa status;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal saldo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	
}
