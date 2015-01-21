package com.cee.livraria.entity.caixa;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.pagamento.FormaPagto;
import com.cee.livraria.entity.pagamento.FormaPagtoEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class CaixaFormaPagto extends AppBaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CAIXAFORMAPAGTO")
	private Long id;

	@NotNull
	@Column(length = 3)
	private String sistema = "LIV";

	@ManyToOne(targetEntity = CaixaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_CAIXAFORMAPAGTO_CAIXA")
	@NotNull
	private Caixa caixa;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@ManyToOne(targetEntity = FormaPagtoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_CAIXAFORMAPAGTO_FORMAPAGTO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	private FormaPagto formaPagto;

	@NotNull
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "data", is = RequiredIfType.not_empty)
	@Size(max = 1)
	private String sitHistoricoPlc = "A";

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getSistema() {
		return sistema;
	}

	public void setSistema(String sistema) {
		this.sistema = sistema;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public FormaPagto getFormaPagto() {
		return formaPagto;
	}

	public void setFormaPagto(FormaPagto formaPagto) {
		this.formaPagto = formaPagto;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

}
