package com.cee.livraria.entity.estoque.ajuste;

import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.produto.RegraPesquisaProdutos;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@SPlcEntity
@Entity
@Table(name = "AJUSTEESTOQUE")
@SequenceGenerator(name = "SE_AJUSTEESTOQUE", sequenceName = "SE_AJUSTEESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="AjusteEstoque.queryMan", query="from AjusteEstoque"),
	@NamedQuery(name="AjusteEstoque.querySel", query="select id as id, nome as nome, data as data, status as status from AjusteEstoque order by nome asc"), 
	@NamedQuery(name="AjusteEstoque.querySelLookup", query="select id as id, nome as nome from AjusteEstoque where id = ? order by id asc") })
public class AjusteEstoque extends AppBaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_AJUSTEESTOQUE")
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
	private StatusAjuste status; 

	@OneToMany (targetEntity = com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="ajusteEstoque")
	@ForeignKey(name="FK_ITEMAJUSTEESTOQUE_AJUSTEESTOQUE")
	@PlcValDuplicity(property="livro")
	@PlcValMultiplicity(referenceProperty="livro",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.ItemAjusteEstoque}")
	@Valid
	private List<ItemAjusteEstoque> itemAjusteEstoque;

	@ManyToOne(targetEntity = ConferenciaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_AJUSTEESTOQUE_CONFERENCIA")
	private Conferencia conferencia;
	
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

	public StatusAjuste getStatus() {
		return status;
	}

	public void setStatus(StatusAjuste status) {
		this.status = status;
	}

	public List<ItemAjusteEstoque> getItemAjusteEstoque() {
		return itemAjusteEstoque;
	}

	public void setItemAjusteEstoque(List<ItemAjusteEstoque> itemAjusteEstoque) {
		this.itemAjusteEstoque = itemAjusteEstoque;
	}
	
	public Conferencia getConferencia() {
		return conferencia;
	}

	public void setConferencia(Conferencia conferencia) {
		this.conferencia = conferencia;
	}

	/*
	 * Construtor padrao
	 */
	public AjusteEstoque() {
	}

	@Override
	public String toString() {
		return getNome();
	}

	@Embedded
	@NotNull
	@Valid
	@Transient
	private transient RegraPesquisaProdutos regra;
	
	public RegraPesquisaProdutos getRegra() {
		return regra;
	}

	public void setRegra(RegraPesquisaProdutos regra) {
		this.regra = regra;
	}
	
	
}
