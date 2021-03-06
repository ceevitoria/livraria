package com.cee.livraria.entity.pagamento;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.validation.constraints.NotNull;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.validation.constraints.Size;
import javax.persistence.GenerationType;
import javax.persistence.MappedSuperclass;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import javax.persistence.GeneratedValue;

@MappedSuperclass
public abstract class FormaPagto extends AppBaseEntity {
	private static final long serialVersionUID = 7982967225496293107L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORMA_PAGTO")
	private Long id;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 80)
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
