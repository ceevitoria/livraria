package com.cee.livraria.entity;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.validation.constraints.NotNull;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import javax.persistence.GeneratedValue;

/**
 * Class Editora
 */
@MappedSuperclass
abstract public class Editora extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_EDITORA")
	private Long id;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 100)
	@PlcReference(testDuplicity = true)
	private String nome;

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

}
