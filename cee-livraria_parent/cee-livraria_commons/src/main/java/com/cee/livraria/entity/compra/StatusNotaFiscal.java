package com.cee.livraria.entity.compra;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum StatusNotaFiscal {
    
	A("{statusNotaFiscal.A}"),
	R("{statusNotaFiscal.R}"),
	E("{statusNotaFiscal.E}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private StatusNotaFiscal(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
