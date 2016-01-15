package com.cee.livraria.entity.compra;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum StatusContaPagar {
    
	A("{statusContaPagar.A}"),
	P("{statusContaPagar.P}"),
	C("{statusContaPagar.C}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private StatusContaPagar(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
