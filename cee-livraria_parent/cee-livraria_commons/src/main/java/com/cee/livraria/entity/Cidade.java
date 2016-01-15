package com.cee.livraria.entity;

import javax.persistence.ManyToOne;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.validation.constraints.NotNull;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import javax.persistence.FetchType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import javax.persistence.GeneratedValue;

@SPlcEntity
@Entity
@Table(name = "CIDADE")
@SequenceGenerator(name = "SE_CIDADE", sequenceName = "SE_CIDADE")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "Cidade.querySelLookup", query = "select id as id, nome as nome from Cidade where id = ? order by id asc") })
public abstract class Cidade extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CIDADE")
	private Long id;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 100)
	private String nome;

	@ManyToOne(targetEntity = Uf.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_CIDADE_UF")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "nome", is = RequiredIfType.not_empty)
	private Uf uf;

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

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	public Cidade() {
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
