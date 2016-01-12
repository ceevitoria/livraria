package com.cee.livraria.entity.estoque;

import java.util.Date;

import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Localizacao;
import com.cee.livraria.entity.LocalizacaoEntity;
import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Produto;

@MappedSuperclass
public abstract class Estoque extends AppBaseEntity {
	private static final long serialVersionUID = -1730467297686228589L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ESTOQUE")
	private Long id;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ESTOQUE_PRODUTO")
	@NotNull
	private Produto produto;

	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidade;

	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeMinima;
	
	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeMaxima;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConferencia;

	@ManyToOne(targetEntity = LocalizacaoEntity.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ESTOQUE_LOCALIZACAO")
	@NotNull
	private Localizacao localizacao;
	
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getQuantidadeMinima() {
		return quantidadeMinima;
	}
	
	public void setQuantidadeMinima(Integer quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}
	
	public Integer getQuantidadeMaxima() {
		return quantidadeMaxima;
	}
	
	public void setQuantidadeMaxima(Integer quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}
	
	public Date getDataConferencia() {
		return dataConferencia;
	}
	
	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}

	public Localizacao getLocalizacao() {
		return localizacao;
	}
	
	public void setLocalizacao(Localizacao localizacao) {
		this.localizacao = localizacao;
	}
}


