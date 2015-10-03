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
@Table(name = "FORMA_PAG_LIVRO")
@SequenceGenerator(name = "SE_FORMA_PAG_LIVRO", sequenceName = "SE_FORMA_PAG_LIVRO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="FormaPagLivroEntity.queryMan", query="from FormaPagLivroEntity"), 
	@NamedQuery(name="FormaPagLivroEntity.querySel", query="select obj.id as id, obj.isGeraCaixa as isGeraCaixa, obj2.id as formaPagto_id, obj2.nome as formaPagto_nome from FormaPagLivroEntity obj inner join obj.formaPagto obj2 order by obj2.nome asc"), 
	@NamedQuery(name="FormaPagLivroEntity.querySelLookup", query="select id as id, formaPagto as formaPagto from FormaPagLivroEntity where id = ? order by id asc") })
public class FormaPagLivroEntity extends FormaPagLivro {

	private static final long serialVersionUID = 1L;

	/*
	 * Construtor padrao
	 */
	public FormaPagLivroEntity() {
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
