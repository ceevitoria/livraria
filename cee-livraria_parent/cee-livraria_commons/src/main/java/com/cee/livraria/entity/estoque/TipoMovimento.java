package com.cee.livraria.entity.estoque;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMovimento {
    
	A("{tipoMovimento.A}"),
	E("{tipoMovimento.E}"),
	S("{tipoMovimento.S}");
	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoMovimento(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
