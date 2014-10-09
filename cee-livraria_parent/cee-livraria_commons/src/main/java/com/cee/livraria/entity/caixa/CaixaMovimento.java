package com.cee.livraria.entity.caixa;

import java.math.BigDecimal;
import java.util.Date;

import com.cee.livraria.entity.AppBaseEntity;
import javax.persistence.ManyToOne;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.TemporalType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import org.hibernate.annotations.ForeignKey;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.EnumType;
import javax.persistence.Temporal;
import javax.validation.constraints.Size;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.persistence.GeneratedValue;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class CaixaMovimento extends AppBaseEntity {

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "observacao", is = RequiredIfType.not_empty)
	@Size(max = 1)
	private String sitHistoricoPlc = "A";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CAIXA_MOVIMENTO")
	private Long id;

	@ManyToOne(targetEntity = CaixaEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_CAIXAMOVIMENTO_CAIXA")
	@NotNull
	private Caixa caixa;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date data;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 2)
	private TipoMovimentoCaixa tipo;

	@NotNull
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valor;

	@Column(length = 2000)
	private String observacao;

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public TipoMovimentoCaixa getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimentoCaixa tipo) {
		this.tipo = tipo;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Caixa getCaixa() {
		return caixa;
	}

	public void setCaixa(Caixa caixa) {
		this.caixa = caixa;
	}

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

}
