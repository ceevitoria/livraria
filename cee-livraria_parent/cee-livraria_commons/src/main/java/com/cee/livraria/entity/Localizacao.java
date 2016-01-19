package com.cee.livraria.entity;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;

@SPlcEntity
@Entity
@Table(name = "LOCALIZACAO")
@SequenceGenerator(name = "SE_LOCALIZACAO", sequenceName = "SE_LOCALIZACAO")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name="Localizacao.queryMan", query="from Localizacao"), 
	@NamedQuery(name="Localizacao.querySel", query="select id as id, codigo as codigo from Localizacao order by codigo asc"), 
	@NamedQuery(name="Localizacao.querySelLookup", query="select id as id, codigo as codigo from Localizacao where id = ? order by codigo asc") 
})
public class Localizacao extends AppBaseEntity {
	private static final long serialVersionUID = 2231492944293367196L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_LOCALIZACAO")
	private Long id;

	@NotNull
	@Size(max = 30)
	@PlcReference(testDuplicity = true)
	private String codigo;

	@Size(max = 200)
	private String descricao;
	
	/*
	 * Construtor padrao
	 */
	public Localizacao() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public String toString() {
		return getCodigo();
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
