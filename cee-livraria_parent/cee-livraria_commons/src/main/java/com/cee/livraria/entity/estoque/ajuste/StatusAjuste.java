package com.cee.livraria.entity.estoque.ajuste;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum StatusAjuste {
    
	F("{statusAjuste.F}"), // Formação
	A("{statusAjuste.A}"), // Aberto
	C("{statusAjuste.C}"); // Concluido
	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private StatusAjuste(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
