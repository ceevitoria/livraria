package com.cee.livraria.entity.estoque.conferencia;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum ResultadoConferencia {
    
	S("{resultadoConferencia.S}"),
	D("{resultadoConferencia.D}"),
	A("{resultadoConferencia.A}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private ResultadoConferencia(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
