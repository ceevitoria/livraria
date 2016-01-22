package com.cee.livraria.entity.relatorio;

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
import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@SPlcEntity
@Entity
@Table(name = "CAIXA_MOVIMENTO")
@SequenceGenerator(name = "SE_CAIXA_MOVIMENTO", sequenceName = "SE_CAIXA_MOVIMENTO")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name="RelatorioFechamentoCaixa.queryMovimentoAbertura", query = "select obj from RelatorioFechamentoCaixa obj where obj.data = (select max(obj2.data) from RelatorioFechamentoCaixa obj2 left outer join obj2.caixa as obj3 where obj2.tipo = 'AB' and obj2.sitHistoricoPlc = 'A' and obj3.status = 'A' order by obj2.data asc)"),
	@NamedQuery(name="RelatorioFechamentoCaixa.querySel", query = "select obj.id as id, obj.data as data, obj.tipo as tipo, obj.valor as valor, obj.observacao as observacao, obj2.id as caixa_id, obj2.status as caixa_status, obj2.saldo as caixa_saldo from RelatorioFechamentoCaixa obj inner join obj.caixa as obj2 where obj.sitHistoricoPlc = 'A' and obj2.status = 'A' order by obj.data asc") })
public class RelatorioFechamentoCaixa extends AppBaseEntity {
	private static final long serialVersionUID = 6776173807857711955L;

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

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "observacao", is = RequiredIfType.not_empty)
	@Size(max = 1)
	private String sitHistoricoPlc = "A";

	/*
	 * Construtor padrao
	 */
	public RelatorioFechamentoCaixa() {
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

	public String getSitHistoricoPlc() {
		return sitHistoricoPlc;
	}

	public void setSitHistoricoPlc(String sitHistoricoPlc) {
		this.sitHistoricoPlc = sitHistoricoPlc;
	}

	@Transient
	@Digits(integer = 10, fraction = 2)
	private transient BigDecimal saldo;
	
	public BigDecimal getSaldo() {
		return saldo;
	}
	
	public void setSaldo(BigDecimal saldo) {
		this.saldo = saldo;
	}
	
	@Transient
	@Digits(integer = 10, fraction = 2)
	private transient BigDecimal saldoFinal;
	
	public BigDecimal getSaldoFinal() {
		this.saldoFinal = saldo.add(valor);
		return saldoFinal;
	}
	
	public void setSaldoFinal(BigDecimal saldoFinal) {
		this.saldoFinal = saldo.add(valor);
	}
	@Override
	public String toString() {
		return getValor().toPlainString();
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
