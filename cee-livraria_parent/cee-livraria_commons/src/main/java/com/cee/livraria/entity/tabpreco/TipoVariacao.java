package com.cee.livraria.entity.tabpreco;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoVariacao {
    
	P("{tipoVariacao.P}"),
	V("{tipoVariacao.V}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoVariacao(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
