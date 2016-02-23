package com.cee.livraria.entity.estoque.conferencia;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@MappedSuperclass
public abstract class Conferencia extends AppBaseEntity {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CONFERENCIA")
	private Long id;

	@NotNull
	@Size(max = 200)
	private String nome;

	@Size(max = 2048)
	private String descricao;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private StatusConferencia status; 

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private ResultadoConferencia resultado;
	
	@OneToMany (targetEntity = com.cee.livraria.entity.estoque.conferencia.ItemConferenciaEntity.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="conferencia")
	@ForeignKey(name="FK_ITEMCONFERENCIA_CONFERENCIA")
	@PlcValDuplicity(property="produto")
	@PlcValMultiplicity(referenceProperty="produto",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.ItemConferenciaEntity}")
	@Valid
	private List<ItemConferencia> itemConferencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public StatusConferencia getStatus() {
		return status;
	}

	public void setStatus(StatusConferencia status) {
		this.status = status;
	}

	public ResultadoConferencia getResultado() {
		return resultado;
	}
	
	public void setResultado(ResultadoConferencia resultado) {
		this.resultado = resultado;
	}
	
	public List<ItemConferencia> getItemConferencia() {
		return itemConferencia;
	}

	public void setItemConferencia(List<ItemConferencia> itemConferencia) {
		this.itemConferencia=itemConferencia;
	}
	
	@Valid
	@Embedded
	@Transient
	private transient RegraPesquisaProdutos regra;
	
	public RegraPesquisaProdutos getRegra() {
		return regra;
	}

	public void setRegra(RegraPesquisaProdutos regra) {
		this.regra = regra;
	}
	
}
