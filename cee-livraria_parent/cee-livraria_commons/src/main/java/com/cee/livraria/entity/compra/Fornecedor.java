package com.cee.livraria.entity.compra;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.Endereco;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@SPlcEntity
@Entity
@Table(name = "FORNECEDOR")
@SequenceGenerator(name = "SE_FORNECEDOR", sequenceName = "SE_FORNECEDOR")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Fornecedor.queryMan", query="from Fornecedor"),
	@NamedQuery(name="Fornecedor.querySel", query="select obj.id as id, obj.nome as nome, obj.razaoSocial as razaoSocial, obj.inscricaoEstatudal as inscricaoEstatudal, obj.cnpj as cnpj, obj.descontoPadrao as descontoPadrao, obj.parcelamentoPadrao as parcelamentoPadrao, obj1.id as endereco_cidade_id , obj1.nome as endereco_cidade_nome, obj2.id as endereco_uf_id, obj2.nome as endereco_uf_nome from Fornecedor obj left outer join obj.endereco.cidade as obj1 left outer join obj.endereco.uf as obj2 order by obj.nome asc"), 
	@NamedQuery(name="Fornecedor.querySelLookup", query="select id as id, nome as nome from Fornecedor where id = ? order by id asc") })
public class Fornecedor extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103576L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORNECEDOR")
	private Long id;

	@NotNull
	@Size(max = 200)
	@Column(length=200)
	private String nome;

	@NotNull
	@Size(max = 200)
	@Column(length=200)
	private String razaoSocial;

	@Size(max = 20)
	@Column(length=20)
	private String inscricaoEstatudal;

	@NotNull
	@Size(max = 20)
	@Column(length=20)
	private String cnpj;

	@Embedded
	@Valid
	private Endereco endereco;

	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal descontoPadrao;

	@ManyToOne(targetEntity = Parcelamento.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_FORNECEDOR_PARCELAMENTOPADRAO")
	@NotNull
	private Parcelamento parcelamentoPadrao;

	@OneToMany (targetEntity = FornecedorContato.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="fornecedor")
	@ForeignKey(name="FK_FORNECEDORCONTATO_FORNECEDOR")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.FornecedorContato}")
	@Valid
	private List<FornecedorContato> fornecedorContato;

	@OneToMany (targetEntity = FornecedorProduto.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="fornecedor")
	@ForeignKey(name="FK_FORNECEDORPRODUTO_FORNECEDOR")
	@PlcValDuplicity(property="codigoProduto")
	@PlcValMultiplicity(referenceProperty="codigoProduto",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.FornecedorProduto}")
	@Valid
	private List<FornecedorProduto> fornecedorProduto;

	public Fornecedor() {
	}

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

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getInscricaoEstatudal() {
		return inscricaoEstatudal;
	}

	public void setInscricaoEstatudal(String inscricaoEstatudal) {
		this.inscricaoEstatudal = inscricaoEstatudal;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public BigDecimal getDescontoPadrao() {
		return descontoPadrao;
	}

	public void setDescontoPadrao(BigDecimal descontoPadrao) {
		this.descontoPadrao = descontoPadrao;
	}

	public Parcelamento getParcelamentoPadrao() {
		return parcelamentoPadrao;
	}

	public void setParcelamentoPadrao(Parcelamento parcelamentoPadrao) {
		this.parcelamentoPadrao = parcelamentoPadrao;
	}

	@Override
	public String toString() {
		return getNome();
	}

	public List<FornecedorContato> getFornecedorContato() {
		return fornecedorContato;
	}

	public void setFornecedorContato(List<FornecedorContato> fornecedorContato) {
		this.fornecedorContato=fornecedorContato;
	}

	public List<FornecedorProduto> getFornecedorProduto() {
		return fornecedorProduto;
	}

	public void setFornecedorProduto(List<FornecedorProduto> fornecedorProduto) {
		this.fornecedorProduto=fornecedorProduto;
	}

}
