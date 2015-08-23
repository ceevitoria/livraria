package com.cee.livraria.entity.estoque.conferencia;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Autor;
import com.cee.livraria.entity.AutorEntity;
import com.cee.livraria.entity.Colecao;
import com.cee.livraria.entity.ColecaoEntity;
import com.cee.livraria.entity.Editora;
import com.cee.livraria.entity.EditoraEntity;
import com.cee.livraria.entity.Espirito;
import com.cee.livraria.entity.EspiritoEntity;
import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.LivroEntity;
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.LocalizacaoEntity;

@MappedSuperclass
public abstract class ItemConferencia extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_CONFERENCIA")
	private Long id;

	@ManyToOne(targetEntity = ConferenciaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_CONFERENCIA")
	@NotNull
	private Conferencia conferencia;

	@ManyToOne(targetEntity = LivroEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_LIVRO")
	private Livro livro;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_AUTOR")
	private Autor autor;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_ESPIRITO")
	private Espirito espirito;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_EDITORA")
	private Editora editora;

	@Digits(integer = 8, fraction = 0)
	private Integer edicao;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_COLECAO")
	private Colecao colecao;

	@ManyToOne(targetEntity = LocalizacaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_LOCALIZACAO")
	private Localizacao localizacao;

	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeConferida;

	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeEstoque;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
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

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	public Integer getQuantidadeConferida() {
		return quantidadeConferida;
	}

	public void setQuantidadeConferida(Integer quantidadeConferida) {
		this.quantidadeConferida = quantidadeConferida;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public Conferencia getConferencia() {
		return conferencia;
	}

	public void setConferencia(Conferencia conferencia) {
		this.conferencia = conferencia;
	}

}
