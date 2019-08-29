package com.cee.livraria.entity.pagamento;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.persistence.Enumerated;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import org.hibernate.annotations.ForeignKey;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.EnumType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

@MappedSuperclass
public abstract class FormaPagProduto extends AppBaseEntity {

	private static final long serialVersionUID = -1359478287021337443L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORMA_PAG_PRODUTO")
	private Long id;

	@ManyToOne(targetEntity = FormaPagtoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FORMAPAGPROD_FORMAPAGTO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@PlcReference(testDuplicity = true)
	private FormaPagto formaPagto;

	@Enumerated(EnumType.STRING)
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "formaPagto", is = RequiredIfType.not_empty)
	@Column(length = 1)
	private PlcYesNo isGeraCaixa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public FormaPagto getFormaPagto() {
		return formaPagto;
	}

	public void setFormaPagto(FormaPagto formaPagto) {
		this.formaPagto = formaPagto;
	}

	public PlcYesNo getIsGeraCaixa() {
		return isGeraCaixa;
	}

	public void setIsGeraCaixa(PlcYesNo isGeraCaixa) {
		this.isGeraCaixa = isGeraCaixa;
	}

}
