package com.cee.livraria.entity.tabpreco.apoio;

import java.math.BigDecimal;

public class PrecoTabela {

	private Long idTabela;
	private String nomeTabela;
	private BigDecimal precoTabela;

	public Long getIdTabela() {
		return idTabela;
	}
	
	public void setIdTabela(Long idTabela) {
		this.idTabela = idTabela;
	}
	
	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public BigDecimal getPrecoTabela() {
		return precoTabela;
	}

	public void setPrecoTabela(BigDecimal precoTabela) {
		this.precoTabela = precoTabela;
	}
}
