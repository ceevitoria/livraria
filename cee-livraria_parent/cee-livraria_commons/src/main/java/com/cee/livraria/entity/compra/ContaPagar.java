package com.cee.livraria.entity.compra;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
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
@Table(name = "CONTA_PAGAR")
@SequenceGenerator(name = "SE_CONTA_PAGAR", sequenceName = "SE_CONTA_PAGAR")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "ContaPagar.querySelLookup", query = "select id as id, observacao as observacao from ContaPagar where id = ? order by id asc") })
public class ContaPagar extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103571L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CONTA_PAGAR")
	private Long id;

	@ManyToOne(targetEntity = NotaFiscal.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_CONTAPAGAR_NOTAFISCAL")
	private NotaFiscal notaFiscal;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataVencimento;

	@Enumerated(EnumType.STRING)
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "dataVencimento", is = RequiredIfType.not_empty)
	@Column(length = 1)
	private StatusContaPagar status;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPagamento;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "dataVencimento", is = RequiredIfType.not_empty)
	@Digits(integer = 8, fraction = 2)
	private BigDecimal valor;

	@Size(max = 1024)
	@Column(length=1024)
	private String observacao;

	public ContaPagar() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public StatusContaPagar getStatus() {
		return status;
	}

	public void setStatus(StatusContaPagar status) {
		this.status = status;
	}

	public Date getDataPagamento() {
		return dataPagamento;
	}

	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
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

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	@Override
	public String toString() {
		return getObservacao();
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
