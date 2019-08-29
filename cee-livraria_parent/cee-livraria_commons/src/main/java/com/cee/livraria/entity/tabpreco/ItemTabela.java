package com.cee.livraria.entity.tabpreco;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.entity.produto.TipoProduto;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@MappedSuperclass
public abstract class ItemTabela extends AppBaseEntity {
	private static final long serialVersionUID = 6970771560739108643L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_TABELA")
	private Long id;

	@ManyToOne(targetEntity = TabelaPrecoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_TABELAPRECO")
	@NotNull
	private TabelaPreco tabelaPreco;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMTABELA_PRODUTO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	private Produto produto;

	@Column(length=1)
	@Enumerated(EnumType.STRING)
	private TipoProduto tipoProduto;

	@Size(max = 200)
	private String titulo;

	@Size(max = 40)
	private String codigoBarras;

	@Size(max = 200)
	private String palavraChave;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "produto", is = RequiredIfType.not_empty)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal preco;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoUltCompra;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal precoVendaSugerido;
	
	@ManyToOne(targetEntity = Localizacao.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ITEMTABELA_LOCALIZACAO")
	@NotNull
	private Localizacao localizacao;
	
	@NotNull
	@Size(max = 1)
	private String sitHistoricoPlc = "A";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public TipoProduto getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProduto tipoProduto) {
		this.tipoProduto = tipoProduto;
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

	public String getPalavraChave() {
		return palavraChave;
	}

	public void setPalavraChave(String palavraChave) {
		this.palavraChave = palavraChave;
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

	public Localizacao getLocalizacao() {
		return localizacao;
	}

	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

}
