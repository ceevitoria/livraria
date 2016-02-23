package com.cee.livraria.entity.produto;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OrderBy;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.cee.livraria.entity.Autor;
import com.cee.livraria.entity.AutorEntity;
import com.cee.livraria.entity.Colecao;
import com.cee.livraria.entity.ColecaoEntity;
import com.cee.livraria.entity.Editora;
import com.cee.livraria.entity.EditoraEntity;
import com.cee.livraria.entity.Espirito;
import com.cee.livraria.entity.EspiritoEntity;
import com.cee.livraria.entity.Localizacao;

@Embeddable
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "RegraPesquisaProdutos.querySelLookup", query = "select titulo as titulo from RegraPesquisaProdutos where id = ? order by titulo asc") })
public class RegraPesquisaProdutos implements Serializable {
	private static final long serialVersionUID = -9182328388056088277L;

	@Column(length=1, insertable=false, updatable=false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto  = TipoProduto.P;

	@Size(max = 40)
	private String titulo;

	@Size(max = 40)
	private String codigoBarras;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@OrderBy(value="nome")
	private Autor autor;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@OrderBy(value="nome")
	private Espirito espirito;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@OrderBy(value="nome")
	private Editora editora;

	@Digits(integer = 5, fraction = 0)
	private Integer edicao;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@OrderBy(value="nome")
	private Colecao colecao;

	@Size(max = 40)
	private String palavraChave;

	@Size(max = 100)
	private String artista;

	@Size(max = 100)
	private String gravadora;
	
	@ManyToOne(targetEntity = Localizacao.class, fetch = FetchType.LAZY)
	@OrderBy(value="codigo")
	private Localizacao localizacao;

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public RegraPesquisaProdutos() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getCodigoBarras() {
		return codigoBarras;
	}

	public void setCodigoBarras(String codigoBarras) {
		this.codigoBarras = codigoBarras;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public Espirito getEspirito() {
		return espirito;
	}

	public void setEspirito(Espirito espirito) {
		this.espirito = espirito;
	}

	public Editora getEditora() {
		return editora;
	}

	public void setEditora(Editora editora) {
		this.editora = editora;
	}

	public Integer getEdicao() {
		return edicao;
	}

	public void setEdicao(Integer edicao) {
		this.edicao = edicao;
	}

	public Colecao getColecao() {
		return colecao;
	}

	public void setColecao(Colecao colecao) {
		this.colecao = colecao;
	}

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
	}

	public String getArtista() {
		return artista;
	}

	public void setArtista(String artista) {
		this.artista = artista;
	}

	public String getGravadora() {
		return gravadora;
	}

	public void setGravadora(String gravadora) {
		this.gravadora = gravadora;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	@Override
	public String toString() {
		return getTitulo();
	}

}
