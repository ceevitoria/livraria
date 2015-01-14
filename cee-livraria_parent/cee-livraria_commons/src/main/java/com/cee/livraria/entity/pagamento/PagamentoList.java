package com.cee.livraria.entity.pagamento;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class PagamentoList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List<Object> itens;

	public PagamentoList() {
		super();
	}

	public List<Object> getItens() {
		return itens;
	}

	public void setItens(List<Object> itens) {
		this.itens = itens;
	}
    
}
