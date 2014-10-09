package com.cee.livraria.entity.estoque.conferencia;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.cee.livraria.entity.Autor;
import com.cee.livraria.entity.Colecao;
import com.cee.livraria.entity.Editora;
import com.cee.livraria.entity.Espirito;
import com.cee.livraria.entity.Localizacao;

@Embeddable
@Access(AccessType.FIELD)
public class RegraPesquisaLivros implements Serializable {

	@Size(max = 40)
	@Transient
	private String titulo;

	@Size(max = 40)
	@Transient
	private String codigoBarras;

	@Transient
	private Autor autor;

	@Transient
	private Espirito espirito;

	@Transient
	private Editora editora;

	@Digits(integer = 5, fraction = 0)
	@Transient
	private Integer edicao;

	@Transient
	private Colecao colecao;

	@Size(max = 40)
	@Transient
	private String palavraChave;

	@Transient
	private Localizacao localizacao;

	public RegraPesquisaLivros() {
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
