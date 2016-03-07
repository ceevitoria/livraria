package com.cee.livraria.entity.rest;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "PRODUTO_REST")
@SequenceGenerator(name = "SE_PRODUTO_REST", sequenceName = "SE_PRODUTO_REST")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="ProdutoRest.querySel", query="select obj.id as id, obj.tipoProduto as tipoProduto, obj.tipoProdutoNome as tipoProdutoNome, obj.titulo as titulo, obj.codigoBarras as codigoBarras, obj.autor as autor, obj.editora as editora, obj.isbn as isbn, obj.palavrasChave as palavrasChave, obj.precoVenda as precoVenda, obj.quantidadeEstoque as quantidadeEstoque, obj.localizacaoId as localizacaoId, obj.localizacaoCodigo as localizacaoCodigo, obj.localizacaoDescricao as localizacaoDescricao, obj.tabelaId as tabelaId, obj.tabelaNome as tabelaNome from ProdutoRest obj order by obj.titulo asc")})
public class ProdutoRest implements Serializable {
	private static final long serialVersionUID = 2831827913958934837L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_PRODUTO_REST")
	private Long id;

	@Size(max = 40)
	private String codigoBarras;
	
	@Size(max = 200)
	private String titulo;

	@Size(max = 100)
	private String autor;
	
	@Size(max = 100)
	private String editora;
	
	@Size(max = 20)
	private String isbn;
	
	@Size(max = 1)
	private String tipoProduto;
	
	@Size(max = 20)
	private String tipoProdutoNome;
	
	@Size(max = 200)
	private String palavrasChave;
	
	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeEstoque;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoVenda;
	
	@Digits(integer = 10, fraction = 0)
	private Long tabelaId;

	@Size(max = 100)
	private String tabelaNome;

	@Digits(integer = 10, fraction = 0)
	private Long localizacaoId;
	
	@Size(max = 30)
	private String localizacaoCodigo;
	
	@Size(max = 200)
	private String localizacaoDescricao;

	public ProdutoRest() {
	}

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

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getEditora() {
		return editora;
	}

	public void setEditora(String editora) {
		this.editora = editora;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(String tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getTipoProdutoNome() {
		return tipoProdutoNome;
	}

	public void setTipoProdutoNome(String tipoProdutoNome) {
		this.tipoProdutoNome = tipoProdutoNome;
	}

	public String getPalavrasChave() {
		return palavrasChave;
	}

	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}

	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}

	public BigDecimal getPrecoVenda() {
		return precoVenda;
	}

	public void setPrecoVenda(BigDecimal precoVenda) {
		this.precoVenda = precoVenda;
	}

	public Long getTabelaId() {
		return tabelaId;
	}

	public void setTabelaId(Long tabelaId) {
		this.tabelaId = tabelaId;
	}

	public String getTabelaNome() {
		return tabelaNome;
	}

	public void setTabelaNome(String tabelaNome) {
		this.tabelaNome = tabelaNome;
	}

	public Long getLocalizacaoId() {
		return localizacaoId;
	}

	public void setLocalizacaoId(Long localizacaoId) {
		this.localizacaoId = localizacaoId;
	}

	public String getLocalizacaoCodigo() {
		return localizacaoCodigo;
	}

	public void setLocalizacaoCodigo(String localizacaoCodigo) {
		this.localizacaoCodigo = localizacaoCodigo;
	}

	public String getLocalizacaoDescricao() {
		return localizacaoDescricao;
	}

	public void setLocalizacaoDescricao(String localizacaoDescricao) {
		this.localizacaoDescricao = localizacaoDescricao;
	}

	public String toString() {
		return codigoBarras + " - " + titulo;
	}
	
	@Transient
	@Digits(integer = 10, fraction = 2)
	private transient BigDecimal precoVendaMin;
	
	@Transient
	@Digits(integer = 10, fraction = 2)
	private transient BigDecimal precoVendaMax;

	public BigDecimal getPrecoVendaMin() {
		return precoVendaMin;
	}

	public void setPrecoVendaMin(BigDecimal precoVendaMin) {
		this.precoVendaMin = precoVendaMin;
	}

	public BigDecimal getPrecoVendaMax() {
		return precoVendaMax;
	}

	public void setPrecoVendaMax(BigDecimal precoVendaMax) {
		this.precoVendaMax = precoVendaMax;
	}
	
}
