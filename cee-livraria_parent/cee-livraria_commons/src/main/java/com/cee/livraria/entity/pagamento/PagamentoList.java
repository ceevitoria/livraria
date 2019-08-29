package com.cee.livraria.entity.pagamento;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public final class PagamentoList implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private List itens;

	public PagamentoList() {
		super();
		itens = new ArrayList();
	}

	public List getItens() {
		return itens;
	}

	public void setItens(List itens) {
		this.itens = itens;
	}
    
}
