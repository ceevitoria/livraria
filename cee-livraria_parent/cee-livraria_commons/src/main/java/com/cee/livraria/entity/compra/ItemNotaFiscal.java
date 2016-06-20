package com.cee.livraria.entity.compra;

import java.math.BigDecimal;

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
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.produto.Produto;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ITEM_NOTA_FISCAL")
@SequenceGenerator(name = "SE_ITEM_NOTA_FISCAL", sequenceName = "SE_ITEM_NOTA_FISCAL")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ItemNotaFiscal.querySelLookup", query = "select id as id, codigoProduto as codigoProduto from ItemNotaFiscal where id = ? order by id asc") })
public class ItemNotaFiscal extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103574L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_NOTA_FISCAL")
	private Long id;

	@ManyToOne(targetEntity = NotaFiscal.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_NOTAFISCAL")
	@NotNull
	private NotaFiscal notaFiscal;

//	@NotNull(groups = PlcValGroupEntityList.class)
//	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 20)
	private String codigoProduto;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_PRODUTO")
//	@NotNull
	private Produto produto;
	
//	@NotNull(groups = PlcValGroupEntityList.class)
//	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorUnitario;

//	@NotNull(groups = PlcValGroupEntityList.class)
//	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	@Digits(integer = 5, fraction = 0)
	private Integer quantidade;

//	@NotNull(groups = PlcValGroupEntityList.class)
//	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal percentualDesconto;

//	@NotNull(groups = PlcValGroupEntityList.class)
//	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorLiquido;

	@ManyToOne(targetEntity = Localizacao.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_LOCALIZACAO")
	private Localizacao localizacao;
	
	public ItemNotaFiscal() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Produto getProduto() {
		return produto;
	}
	
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getPercentualDesconto() {
		return percentualDesconto;
	}

	public void setPercentualDesconto(BigDecimal percentualDesconto) {
		this.percentualDesconto = percentualDesconto;
	}

	public BigDecimal getValorLiquido() {
		return valorLiquido;
	}

	public void setValorLiquido(BigDecimal valorLiquido) {
		this.valorLiquido = valorLiquido;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		localizacao = localizacao;
	}

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public String toString() {
		return getCodigoProduto();
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	@Transient
	private transient boolean isProdutoFornecedorExistente = false;

	public boolean isProdutoFornecedorExistente() {
		return isProdutoFornecedorExistente;
	}

	public void setProdutoFornecedorExistente(boolean isProdutoFornecedorExistente) {
		this.isProdutoFornecedorExistente = isProdutoFornecedorExistente;
	}
	
}
