package com.cee.livraria.entity.produto;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Estocavel;
import com.cee.livraria.entity.Localizacao;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "PRODUTO")
@SequenceGenerator(name = "SE_PRODUTO", sequenceName = "SE_PRODUTO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Produto.queryMan", query="from Produto"),
	@NamedQuery(name="Produto.querySel", query="select obj.id as id, obj.tipoProduto as tipoProduto, obj.titulo as titulo, obj.codigoBarras as codigoBarras, obj.palavrasChave as palavrasChave, obj.precoUltCompra as precoUltCompra, obj.precoVendaSugerido as precoVendaSugerido from Produto obj order by obj.titulo asc"),
	@NamedQuery(name="Produto.queryEdita", query="select obj from Produto obj where obj.id = ?"),
	@NamedQuery(name="Produto.queryPrecoTabela", query = 
			" select t.id as idTabela, t.nome as nomeTabela, i.preco as precoTabela " +
			"   from ItemTabelaEntity i " +
			"     left outer join i.produto as p " +
			"     left outer join i.tabelaPreco as t " +
			"  where p.id = :id " +
			"    and t.ativa = 'S' " +
			"    and t.sitHistoricoPlc = 'A' " +
			"    and i.sitHistoricoPlc = 'A' " +
			"    and ((t.dataFim is null and t.dataInicio <= current_date()) " +
			"     or  (t.dataFim is not null and current_date() between t.dataInicio and t.dataFim)) " +
			"   order by t.dataInicio desc "),
	@NamedQuery(name="Produto.querySelLookup", query="select id as id, codigoBarras as codigoBarras, titulo as titulo from Produto where id = ? order by id asc") })
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name="tipo_produto", length=1, discriminatorType=DiscriminatorType.STRING)
@DiscriminatorValue("P")
public class Produto extends AppBaseEntity implements Estocavel {
	private static final long serialVersionUID = 7010726688922277123L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_PRODUTO")
	private Long id;

	@Column(length=1, insertable=true, updatable=false)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;

	@NotNull
	@Size(max = 40)
	private String codigoBarras;

	@NotNull
	@Size(max = 200)
	private String titulo;

	@Size(max = 200)
	private String palavrasChave;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoUltCompra;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoVendaSugerido;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
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
	
	public String getPalavrasChave() {
		return palavrasChave;
	}
	
	public void setPalavrasChave(String palavrasChave) {
		this.palavrasChave = palavrasChave;
	}
	
	public BigDecimal getPrecoUltCompra() {
		return precoUltCompra;
	}
	
	public void setPrecoUltCompra(BigDecimal precoUltCompra) {
		this.precoUltCompra = precoUltCompra;
	}
	
	public BigDecimal getPrecoVendaSugerido() {
		return precoVendaSugerido;
	}
	
	public void setPrecoVendaSugerido(BigDecimal precoVendaSugerido) {
		this.precoVendaSugerido = precoVendaSugerido;
	}

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
	public Produto() {
	}

	@Override
	public String toString() {
		if (getTitulo() != null) {
			return getTitulo();
		} else {
			return "Produto";
		}
	}
	
	@Transient
	private transient Localizacao localizacao; 
	
	public Localizacao getLocalizacao() {
		return localizacao;
	}
	
	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}
	
	@Transient
	private transient Integer quantidadeEstoque; 
	
	public Integer getQuantidadeEstoque() {
		return quantidadeEstoque;
	}

	public void setQuantidadeEstoque(Integer quantidadeEstoque) {
		this.quantidadeEstoque = quantidadeEstoque;
	}
	
}
