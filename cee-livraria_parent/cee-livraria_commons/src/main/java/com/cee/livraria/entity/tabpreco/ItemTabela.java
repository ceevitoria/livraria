package com.cee.livraria.entity.tabpreco;

import java.math.BigDecimal;

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

import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.Valid;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.persistence.Embedded;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Livro;

import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import org.hibernate.annotations.ForeignKey;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.validation.constraints.Digits;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

@MappedSuperclass
public abstract class ItemTabela extends AppBaseEntity {

	@NotNull
	@Size(max = 1)
	private String sitHistoricoPlc = "A";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_TABELA")
	private Long id;

	@ManyToOne(targetEntity = TabelaPrecoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_TABELAPRECO")
	@NotNull
	private TabelaPreco tabelaPreco;

	@ManyToOne(targetEntity = Livro.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_LIVRO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	private Livro livro;

	@Size(max = 40)
	private String titulo;

	@Size(max = 40)
	private String codigoBarras;

	@ManyToOne(targetEntity = AutorEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_AUTOR")
	private Autor autor;

	@ManyToOne(targetEntity = EspiritoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_ESPIRITO")
	private Espirito espirito;

	@ManyToOne(targetEntity = EditoraEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_EDITORA")
	private Editora editora;

	@Digits(integer = 5, fraction = 0)
	private Integer edicao;

	@ManyToOne(targetEntity = ColecaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_COLECAO")
	private Colecao colecao;

	@Size(max = 40)
	private String palavraChave;

	@ManyToOne(targetEntity = LocalizacaoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_LOCALIZACAO")
	private Localizacao localizacao;
	
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "livro", is = RequiredIfType.not_empty)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal preco;

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

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public TabelaPreco getTabelaPreco() {
		return tabelaPreco;
	}

	public void setTabelaPreco(TabelaPreco tabelaPreco) {
		this.tabelaPreco = tabelaPreco;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

	
}
