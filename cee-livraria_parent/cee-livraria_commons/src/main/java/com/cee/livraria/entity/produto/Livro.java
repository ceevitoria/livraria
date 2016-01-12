package com.cee.livraria.entity.produto;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.Autor;
import com.cee.livraria.entity.AutorEntity;
import com.cee.livraria.entity.Colecao;
import com.cee.livraria.entity.ColecaoEntity;
import com.cee.livraria.entity.Editora;
import com.cee.livraria.entity.EditoraEntity;
import com.cee.livraria.entity.Espirito;
import com.cee.livraria.entity.EspiritoEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

/**
 * Class Livro
 */
@SPlcEntity
@Entity
@Table(name = "LIVRO")
@SequenceGenerator(name = "SE_LIVRO", sequenceName = "SE_LIVRO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Livro.queryMan", query="from Livro"),
	@NamedQuery(name="Livro.querySel", query="select obj.id as id, obj.codigoBarras as codigoBarras, obj.titulo as titulo, obj.isbn as isbn, obj.edicao as edicao, obj.palavrasChave as palavrasChave, obj.precoUltCompra as precoUltCompra, obj1.id as espirito_id, obj1.nome as espirito_nome, obj2.id as autor_id, obj2.nome as autor_nome, obj3.id as editora_id, obj3.nome as editora_nome, obj4.id as colecao_id, obj4.nome as colecao_nome from Livro obj left outer join obj.espirito as obj1 left outer join obj.autor as obj2 left outer join obj.editora as obj3 left outer join obj.colecao as obj4 order by obj.titulo asc"),
	@NamedQuery(name="Livro.queryEdita", query="select obj from Livro obj where obj.id = ?"),
//	@NamedQuery(name="Livro.queryPrecoTabela", query = 
//			" select t.id as idTabela, t.nome as nomeTabela, i.preco as precoTabela " +
//			"   from ItemTabelaEntity i " +
//			"     left outer join i.livro as l " +
//			"     left outer join i.tabelaPreco as t " +
//			"  where l.id = :id " +
//			"    and t.ativa = 'S' " +
//			"    and t.sitHistoricoPlc = 'A' " +
//			"    and i.sitHistoricoPlc = 'A' " +
//			"    and ((t.dataFim is null and t.dataInicio <= current_date()) " +
//			"     or  (t.dataFim is not null and current_date() between t.dataInicio and t.dataFim)) " +
//			"   order by t.dataInicio desc "),
	@NamedQuery(name="Livro.querySelLookup", query="select id as id, codigoBarras as codigoBarras, titulo as titulo from Livro where id = ? order by id asc") })
@DiscriminatorValue("L")
//@PrimaryKeyJoinColumn(name = "PRODUTO_ID")
@ForeignKey(name = "FK_LIVRO_PRODUTO")
public class Livro extends Produto {
	private static final long serialVersionUID = 7797394047043215934L;

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_LIVRO")
//	private Long id;

	@Size(max = 20)
	private String isbn;

	@NotNull
	@Min(value=1)
	private Integer edicao;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_ESPIRITO")
	@OrderBy(value="nome")
	private Espirito espirito;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_AUTOR")
	@NotNull
	@OrderBy(value="nome")
	private Autor autor;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_EDITORA")
	@NotNull
	@OrderBy(value="nome")
	private Editora editora;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_COLECAO")
	@OrderBy(value="nome")
	private Colecao colecao;
	
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public Integer getEdicao() {
		return edicao;
	}

	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}

	public Espirito getEspirito() {
		return espirito;
	}

	public void setEspirito(Espirito espirito) {
		this.espirito = espirito;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public Colecao getColecao() {
		return colecao;
	}

	public void setColecao(Colecao colecao) {
		this.colecao = colecao;
	}


	
	/*
	 * Construtor padrao
	 */
	public Livro() {
	}
	
	@Override
	public String toString() {
		if (getTitulo() != null) {
			return getTitulo();
		} else {
			return "Livro";
		}
	}
	
}
