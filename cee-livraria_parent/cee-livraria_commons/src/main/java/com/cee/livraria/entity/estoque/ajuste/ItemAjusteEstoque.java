package com.cee.livraria.entity.estoque.ajuste;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
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
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.LocalizacaoEntity;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Livro;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ITEM_AJUSTEESTOQUE")
@SequenceGenerator(name = "SE_ITEM_AJUSTEESTOQUE", sequenceName = "SE_ITEM_AJUSTEESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name="ItemAjusteEstoque.querySelLookup", query = "select id as id, livro as livro from ItemAjusteEstoque where id = ? order by id asc") })
public class ItemAjusteEstoque extends AppBaseEntity {
	private static final long serialVersionUID = 1231235L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_AJUSTEESTOQUE")
	private Long id;

	@ManyToOne(targetEntity = AjusteEstoque.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_AJUSTEESTOQUE")
	@NotNull
	private AjusteEstoque ajusteEstoque;

	@ManyToOne(targetEntity = Livro.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_LIVRO")
	private Livro livro;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_AUTOR")
	private Autor autor;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_ESPIRITO")
	private Espirito espirito;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_EDITORA")
	private Editora editora;

	@Digits(integer = 8, fraction = 0)
	private Integer edicao;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_COLECAO")
	private Colecao colecao;

	@ManyToOne(targetEntity = LocalizacaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_LOCALIZACAO")
	private Localizacao localizacao;

	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeInformada;

	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeEstoque;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AjusteEstoque getAjusteEstoque() {
		return ajusteEstoque;
	}

	public void setAjusteEstoque(AjusteEstoque ajusteEstoque) {
		this.ajusteEstoque = ajusteEstoque;
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

	public Integer getQuantidadeInformada() {
		return quantidadeInformada;
	}

	public void setQuantidadeInformada(Integer quantidadeInformada) {
		this.quantidadeInformada = quantidadeInformada;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
	/*
	 * Construtor padrao
	 */
	public ItemAjusteEstoque() {
	}

	@Override
	public String toString() {

		if (getLivro() != null && getLivro().getTitulo() != null) {
			return getLivro().getTitulo();
		}

		return "";
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	private transient String livroAuxLookup;

	public void setLivroAuxLookup(String livroAuxLookup) {
		this.livroAuxLookup = livroAuxLookup;
	}

	public String getLivroAuxLookup() {
		return livroAuxLookup;
	}
	
}
