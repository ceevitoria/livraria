package com.cee.livraria.controller.jsf.notafiscal;

import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;

import com.cee.livraria.entity.compra.ItemNotaFiscal;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcSpecific;
import com.powerlogic.jcompany.controller.jsf.PlcBaseSearchMB;
import com.powerlogic.jcompany.controller.util.PlcBeanPopulateUtil;

@QPlcSpecific(name="notafiscal")
public class NotaFiscalSearchMB extends PlcBaseSearchMB {

	private static final long serialVersionUID = -7093850395491050093L;

	@Inject @QPlcDefault
	PlcBeanPopulateUtil beanPopulateUtil;

	
	public String  navigationDetailNext (Object entityPlc) {
		String ret = super.navigationDetailNext(entityPlc);
		atualizaTipoProduto(entityPlc);
		return ret;
	}

	public String  navigationDetailPrevious (Object entityPlc) {
		String ret = super.navigationDetailPrevious(entityPlc);
		atualizaTipoProduto(entityPlc);
		return ret;
	}
	
	public String  navigationDetailFirst (Object entityPlc) {
		String ret = super.navigationDetailFirst(entityPlc);
		atualizaTipoProduto(entityPlc);
		return ret;
	}
	
	public String  navigationDetailLast (Object entityPlc) {
		String ret = super.navigationDetailLast(entityPlc);
		atualizaTipoProduto(entityPlc);
		return ret;
	}
	
	
	public String navigationToPage (ValueChangeEvent event, Object entityPlc) {
		String ret = super.navigationToPage(event, entityPlc);
		atualizaTipoProduto(entityPlc);
		return ret;
	}

	private void atualizaTipoProduto(Object entityPlc) {
		NotaFiscal notaFiscal = (NotaFiscal) entityPlc;

		if (notaFiscal != null) {
			
			for (Object o : notaFiscal.getItemNotaFiscal()) {
				ItemNotaFiscal itemNF = (ItemNotaFiscal)o;
				
				if (itemNF.getProduto() != null && itemNF.getProduto().getId() != null && itemNF.getProduto().getTipoProduto() != null) {
					itemNF.setTipoProduto(itemNF.getProduto().getTipoProduto());
				}
			}
		}
	}
	
}
