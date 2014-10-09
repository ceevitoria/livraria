package com.cee.livraria.entity.caixa;

/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMovimentoCaixa {
    
	SA("{tipoMovimentoCaixa.Sangria}"),
	SU("{tipoMovimentoCaixa.Suprimento}"),
	VD("{tipoMovimentoCaixa.Venda}"),
	AA("{tipoMovimentoCaixa.AjusteAutomatico}"),
	AM("{tipoMovimentoCaixa.AjusteManual}"),
	AB("{tipoMovimentoCaixa.Abertura}"),
	FE("{tipoMovimentoCaixa.Fechamento}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoMovimentoCaixa(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
