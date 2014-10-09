package com.cee.livraria.entity;

import java.math.BigDecimal;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

/**
 * Class Livro
 */
@MappedSuperclass
abstract public class Livro extends AppBaseEntity implements Estocavel {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_LIVRO")
	private Long id;

	@NotNull
	@Size(max = 40)
	private String codigoBarras;

	@NotNull
	@Size(max = 200)
	private String titulo;

	@Size(max = 20)
	private String isbn;

	@NotNull
	@Min(value=1)
	private Integer edicao;

	@Size(max = 200)
	private String palavrasChave;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_ESPIRITO")
	private Espirito espirito;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_AUTOR")
	@NotNull
	private Autor autor;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_EDITORA")
	@NotNull
	private Editora editora;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_LIVRO_COLECAO")
	private Colecao colecao;
	
	//@DecimalMax(value="1000")
	@Digits(integer = 10, fraction = 2)
	private BigDecimal preco;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

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

	public String getPalavrasChave() {
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
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

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

}
