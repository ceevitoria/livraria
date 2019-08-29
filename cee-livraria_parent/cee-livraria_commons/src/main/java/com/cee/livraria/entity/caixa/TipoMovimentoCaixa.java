package com.cee.livraria.entity.caixa;


/**
 * Enum de dominio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoMovimentoCaixa {
    
	SA("{tipoMovimentoCaixa.Sangria}"),
	SU("{tipoMovimentoCaixa.Suprimento}"),
	VD("{tipoMovimentoCaixa.Venda}"),
	DE("{tipoMovimentoCaixa.Devolucao}"),
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
	
    
    public String getName() {
    	String ret = "Critical Application";
    	
    	if (this.equals(TipoMovimentoCaixa.SA)) {
    		ret = "Sangria";
    	} else if (this.equals(TipoMovimentoCaixa.SU)) {
    		ret = "Suprimento";
    	} else if (this.equals(TipoMovimentoCaixa.VD)) {
    		ret = "Venda";
    	} else if (this.equals(TipoMovimentoCaixa.DE)) {
    		ret = "Devolução";
    	} else if (this.equals(TipoMovimentoCaixa.AA)) {
    		ret = "Ajuste Automatico";
    	} else if (this.equals(TipoMovimentoCaixa.AM)) {
    		ret = "Ajuste Manual";
    	} else if (this.equals(TipoMovimentoCaixa.AB)) {
    		ret = "Abertura";
    	} else if (this.equals(TipoMovimentoCaixa.FE)) {
    		ret = "Fechamento";
    	} 
    	
    	return ret;
    }
    
}
