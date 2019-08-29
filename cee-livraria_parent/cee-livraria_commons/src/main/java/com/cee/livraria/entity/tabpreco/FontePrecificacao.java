package com.cee.livraria.entity.tabpreco;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum FontePrecificacao {
    
	PV("{fontePrecificacao.PV}"),
	PC("{fontePrecificacao.PC}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private FontePrecificacao(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
