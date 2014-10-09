package com.cee.livraria.entity.estoque.conferencia;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum StatusConferencia {
    
	F("{statusConferencia.F}"),
	A("{statusConferencia.A}"),
	C("{statusConferencia.C}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private StatusConferencia(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
