package com.cee.livraria.entity.config;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMensagemConferenciaConfig {
    
	B("{tipoMensagemConferencia.B}"),
	Q("{tipoMensagemConferencia.Q}"),
	D("{tipoMensagemConferencia.D}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoMensagemConferenciaConfig(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
