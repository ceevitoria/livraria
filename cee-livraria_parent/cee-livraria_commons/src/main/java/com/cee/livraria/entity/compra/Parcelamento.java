package com.cee.livraria.entity.compra;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@SPlcEntity
@Entity
@Table(name = "PARCELAMENTO")
@SequenceGenerator(name = "SE_PARCELAMENTO", sequenceName = "SE_PARCELAMENTO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Parcelamento.queryMan", query="from Parcelamento"), @NamedQuery(name = "Parcelamento.querySelLookup", query = "select id as id, nome as nome from Parcelamento where id = ? order by id asc") })
public class Parcelamento extends AppBaseEntity {
	private static final long serialVersionUID = 4366731055546113881L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_PARCELAMENTO")
	private Long id;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 80)
	@PlcReference(testDuplicity = true)
	private String nome;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "nome", is = RequiredIfType.not_empty)
	@Digits(integer = 5, fraction = 0)
	private Integer numeroParcelas;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "nome", is = RequiredIfType.not_empty)
	@Digits(integer = 5, fraction = 0)
	private Integer carenciaInicial;

	public Parcelamento() {
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

	public Integer getNumeroParcelas() {
		return numeroParcelas;
	}

	public void setNumeroParcelas(Integer numeroParcelas) {
		this.numeroParcelas = numeroParcelas;
	}

	public Integer getCarenciaInicial() {
		return carenciaInicial;
	}

	public void setCarenciaInicial(Integer carenciaInicial) {
		this.carenciaInicial = carenciaInicial;
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
