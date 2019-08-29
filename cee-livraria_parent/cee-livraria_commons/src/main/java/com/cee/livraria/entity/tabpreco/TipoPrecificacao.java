package com.cee.livraria.entity.tabpreco;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoPrecificacao {
    
	A("{tipoPrecificacao.A}"),
	D("{tipoPrecificacao.D}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoPrecificacao(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
