package com.cee.livraria.entity.estoque;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum ModoMovimento {
    
	A("{modoMovimento.A}"),
	N("{modoMovimento.N}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private ModoMovimento(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
