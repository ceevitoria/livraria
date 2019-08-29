package com.cee.livraria.entity.caixa;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum StatusCaixa {
    
	A("{statusCaixa.A}"),
	F("{statusCaixa.F}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private StatusCaixa(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
