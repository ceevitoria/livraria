package com.cee.livraria.entity.config;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMensagemSucessoVendaConfig {
    
	B("{tipoMensagemSucessoVendaConfig.B}"),
	V("{tipoMensagemSucessoVendaConfig.V}"),
	D("{tipoMensagemSucessoVendaConfig.D}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoMensagemSucessoVendaConfig(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
