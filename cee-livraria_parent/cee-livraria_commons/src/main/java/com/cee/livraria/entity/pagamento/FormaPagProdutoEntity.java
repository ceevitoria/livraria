package com.cee.livraria.entity.pagamento;

import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Access;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.AccessType;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "FORMA_PAG_PRODUTO")
@SequenceGenerator(name = "SE_FORMA_PAG_PRODUTO", sequenceName = "SE_FORMA_PAG_PRODUTO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="FormaPagProdutoEntity.queryMan", query="from FormaPagProdutoEntity"), 
	@NamedQuery(name="FormaPagProdutoEntity.querySel", query="select obj.id as id, obj.isGeraCaixa as isGeraCaixa, obj2.id as formaPagto_id, obj2.nome as formaPagto_nome from FormaPagProdutoEntity obj inner join obj.formaPagto obj2 order by obj2.id asc"), 
	@NamedQuery(name="FormaPagProdutoEntity.querySelLookup", query="select id as id, formaPagto as formaPagto from FormaPagProdutoEntity where id = ? order by id asc") })
public class FormaPagProdutoEntity extends FormaPagProduto {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public FormaPagProdutoEntity() {
	}

	@Override
	public String toString() {
		return getFormaPagto().toString();
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

}
