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
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.LocalizacaoEntity;
import com.cee.livraria.entity.produto.Produto;

@MappedSuperclass
public abstract class ItemConferencia extends AppBaseEntity {

	private static final long serialVersionUID = 7570332784853063134L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_CONFERENCIA")
	private Long id;

	@ManyToOne(targetEntity = ConferenciaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_CONFERENCIA")
	@NotNull
	private Conferencia conferencia;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMCONFERENCIA_PRODUTO")
	private Produto produto;

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
