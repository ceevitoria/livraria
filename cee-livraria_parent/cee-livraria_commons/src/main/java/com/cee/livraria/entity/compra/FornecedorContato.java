package com.cee.livraria.entity.compra;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@SPlcEntity
@Entity
@Table(name = "FORNECEDOR_CONTATO")
@SequenceGenerator(name = "SE_FORNECEDOR_CONTATO", sequenceName = "SE_FORNECEDOR_CONTATO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "FornecedorContato.querySelLookup", query = "select id as id, nome as nome from FornecedorContato where id = ? order by id asc") })
public class FornecedorContato extends AppBaseEntity {
	private static final long serialVersionUID = -30583546079310357L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORNECEDOR_CONTATO")
	private Long id;

	@ManyToOne(targetEntity = Fornecedor.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FORNECEDORCONTATO_FORNECEDOR")
	@NotNull
	private Fornecedor fornecedor;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 100)
	private String nome;

	@Size(max = 20)
	private String celular;

	@Size(max = 20)
	private String fixo;

	@Size(max = 10)
	private String ramal;

	@Size(max = 100)
	private String email;

	public FornecedorContato() {
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

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getFixo() {
		return fixo;
	}

	public void setFixo(String fixo) {
		this.fixo = fixo;
	}

	public String getRamal() {
		return ramal;
	}

	public void setRamal(String ramal) {
		this.ramal = ramal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
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
