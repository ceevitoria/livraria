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
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.LocalizacaoEntity;
import com.cee.livraria.entity.produto.Produto;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ITEM_AJUSTEESTOQUE")
@SequenceGenerator(name = "SE_ITEM_AJUSTEESTOQUE", sequenceName = "SE_ITEM_AJUSTEESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name="ItemAjusteEstoque.querySelLookup", query = "select id as id, produto as produto from ItemAjusteEstoque where id = ? order by id asc") })
public class ItemAjusteEstoque extends AppBaseEntity {
	private static final long serialVersionUID = 1231235L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_AJUSTEESTOQUE")
	private Long id;

	@ManyToOne(targetEntity = AjusteEstoque.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_AJUSTEESTOQUE")
	@NotNull
	private AjusteEstoque ajusteEstoque;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMAJUSTEESTOQUE_PRODUTO")
	private Produto produto;

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

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
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

		if (getProduto() != null && getProduto().getTitulo() != null) {
			return getProduto().getTitulo();
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
