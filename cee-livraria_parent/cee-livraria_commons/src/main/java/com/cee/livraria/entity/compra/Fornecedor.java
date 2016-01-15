package com.cee.livraria.entity.compra;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@NamedQueries({ @NamedQuery(name = "Fornecedor.querySelLookup", query = "select id as id, nome as nome from Fornecedor where id = ? order by id asc") })
public class Fornecedor extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103576L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORNECEDOR")
	private Long id;

	@NotNull
	@Size(max = 5)
	private String nome;

	@NotNull
	@Size(max = 5)
	private String razaoSocial;

	@Size(max = 5)
	private String inscricaoEstatudal;

	@NotNull
	@Size(max = 5)
	private String cnpj;

	@Embedded
	@Valid
	private Endereco endereco;

	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal descontoPadrao;

	@ManyToOne(targetEntity = Parcelamento.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FORNECEDOR_PARCELAMENTOPADRAO")
	@NotNull
	private Parcelamento parcelamentoPadrao;

	@OneToMany (targetEntity = FornecedorContato.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="fornecedor")
	@ForeignKey(name="FK_FORNECEDORCONTATO_FORNECEDOR")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.FornecedorContato}")
	@Valid
	private List<FornecedorContato> fornecedorContato;

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

}
