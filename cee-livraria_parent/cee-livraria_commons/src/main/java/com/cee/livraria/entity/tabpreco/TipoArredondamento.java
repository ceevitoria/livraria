package com.cee.livraria.entity.tabpreco;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoArredondamento {
    
	DE("{tipoArredondamento.DE}"),
	UN("{tipoArredondamento.UN}"),
	DC("{tipoArredondamento.DC}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoArredondamento(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
