package com.cee.livraria.entity;

import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@SPlcEntity
@Entity
@Table(name = "UF")
@SequenceGenerator(name = "SE_UF", sequenceName = "SE_UF")
@Access(AccessType.FIELD)
@NamedQueries({
		@NamedQuery(name = "Uf.querySel", query = "select id as id, nome as nome, sigla as sigla from Uf order by nome asc"),
		@NamedQuery(name = "Uf.queryMan", query = "from Uf"),
		@NamedQuery(name = "Uf.querySelLookup", query = "select id as id, nome as nome from Uf where id = ? order by id asc") })
public class Uf extends AppBaseEntity {
	private static final long serialVersionUID = -4145898223612443831L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_UF")
	private Long id;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 60)
	@PlcReference(testDuplicity = true)
	private String nome;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "nome", is = RequiredIfType.not_empty)
	@Size(max = 2)
	private String sigla;

	@OneToMany (targetEntity = com.cee.livraria.entity.Cidade.class, fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="uf")
	@ForeignKey(name="FK_CIDADE_UF")
	@PlcValDuplicity(property="nome")
	@PlcValMultiplicity(referenceProperty="nome",  message="{jcompany.aplicacao.mestredetalhe.multiplicidade.CidadeEntity}")
	@Valid
	private List<Cidade> cidade;

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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

	public List<Cidade> getCidade() {
		return cidade;
	}

	public void setCidade(List<Cidade> cidade) {
		this.cidade=cidade;
	}

	/*
	 * Construtor padrao
	 */
	public Uf() {
	}

	@Override
	public String toString() {
		return getNome();
	}

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}

	
}
