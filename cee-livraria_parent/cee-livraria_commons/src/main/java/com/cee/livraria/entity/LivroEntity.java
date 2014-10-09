package com.cee.livraria.entity;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Classe Concreta gerada a partir do assistente
 */
@SPlcEntity
@Entity
@Table(name = "LIVRO")
@SequenceGenerator(name = "SE_LIVRO", sequenceName = "SE_LIVRO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "LivroEntity.queryMan", query = "from LivroEntity"),
	@NamedQuery(name = "LivroEntity.querySel", query = "select obj.id as id, obj.codigoBarras as codigoBarras, obj.titulo as titulo, obj.isbn as isbn, obj.edicao as edicao, obj.palavrasChave as palavrasChave, obj1.id as espirito_id , obj1.nome as espirito_nome, obj2.id as autor_id , obj2.nome as autor_nome, obj3.id as editora_id , obj3.nome as editora_nome, obj4.id as colecao_id , obj4.nome as colecao_nome, obj.preco as preco from LivroEntity obj left outer join obj.espirito as obj1 left outer join obj.autor as obj2 left outer join obj.editora as obj3 left outer join obj.colecao as obj4 order by obj.titulo asc"),
//	@NamedQuery(name = "LivroEntity.queryPrecoTabela", query = "select t.nome as nomeTabela, i.preco as precoTabela from ItemTabelaEntity i left outer join i.livro as l left outer join i.tabelaPreco as t where l.id = :id order by l.titulo asc"),
	@NamedQuery(name = "LivroEntity.queryPrecoTabela", query = 
			" select t.id as idTabela, t.nome as nomeTabela, i.preco as precoTabela " +
			"   from ItemTabelaEntity i " +
			"     left outer join i.livro as l " +
			"     left outer join i.tabelaPreco as t " +
			"  where l.id = :id " +
			"    and t.ativa = 'S' " +
			"    and t.sitHistoricoPlc = 'A' " +
			"    and i.sitHistoricoPlc = 'A' " +
			"    and ((t.dataFim is null and t.dataInicio <= current_date()) " +
			"     or  (t.dataFim is not null and current_date() between t.dataInicio and t.dataFim)) " +
			"   order by t.dataInicio desc "),
			
	@NamedQuery(name = "LivroEntity.querySelLookup", query = "select id as id, codigoBarras as codigoBarras, titulo as titulo from LivroEntity where id = ? order by id asc") })
public class LivroEntity extends Livro {

	private static final long serialVersionUID = 1L;

	private transient Long idTabela;

	private transient BigDecimal precoTabela;
	
	private transient String nomeTabela;

	public Long getIdTabela() {
		return idTabela;
	}
	
	public void setIdTabela(Long idTabela) {
		this.idTabela = idTabela;
	}
	
	public String getNomeTabela() {
		return nomeTabela;
	}

	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
	}

	public void setPrecoTabela(BigDecimal precoTabela) {
		this.precoTabela = precoTabela;
	}
	
	public BigDecimal getPrecoTabela() {
		return precoTabela;
	}
	
	/*
	 * Construtor padrao
	 */
	public LivroEntity() {
	}

	@Override
	public String toString() {
		return getTitulo();
	}

}
