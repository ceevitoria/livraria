package com.cee.livraria.entity.produto;

/**
 * Enum de domínio discreto gerada automaticamente pelo assistente do jCompany.
 */
public enum TipoProduto {
    
	P("{tipoProduto.P}"),
	L("{tipoProduto.L}"),
	C("{tipoProduto.C}"),
	D("{tipoProduto.D}");

	
    /**
     * @return Retorna o codigo.
     */
     
	private String label;
    
    private TipoProduto(String label) {
    	this.label = label;
    }
     
    public String getLabel() {
        return label;
    }
	
}
