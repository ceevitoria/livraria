package com.cee.livraria.entity.config;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMensagemAjusteEstoqueConfig {
    
	B("{tipoMensagemAjusteEstoque.B}"),
	Q("{tipoMensagemAjusteEstoque.Q}"),
	D("{tipoMensagemAjusteEstoque.D}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoMensagemAjusteEstoqueConfig(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
