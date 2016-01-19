package com.cee.livraria.entity.compra;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValDuplicity;
import com.powerlogic.jcompany.domain.validation.PlcValMultiplicity;

@SPlcEntity
@Entity
@Table(name = "NOTA_FISCAL")
@SequenceGenerator(name = "SE_NOTA_FISCAL", sequenceName = "SE_NOTA_FISCAL")
@Access(AccessType.FIELD)
@NamedQueries({
	@NamedQuery(name = "NotaFiscal.queryMan", query = "from NotaFiscal"),
	@NamedQuery(name = "NotaFiscal.querySel", query = "select obj.id as id, obj.numero as numero, obj.dataEmissao as dataEmissao, obj.dataEntrada as dataEntrada, obj.valorTotal as valorTotal, obj1.id as fornecedor_id , obj1.nome as fornecedor_nome from NotaFiscal obj left outer join obj.fornecedor as obj1 order by obj.numero asc"),
	@NamedQuery(name = "NotaFiscal.querySelLookup", query = "select id as id, numero as numero from NotaFiscal where id = ? order by id asc") })
public class NotaFiscal extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103575L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_NOTA_FISCAL")
	private Long id;

	@NotNull
	@Size(max = 20)
	private String numero;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEmissao;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataEntrada;

	@ManyToOne(targetEntity = Fornecedor.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_NOTAFISCAL_FORNECEDOR")
	@NotNull
	private Fornecedor fornecedor;

	@NotNull
	@Digits(integer = 8, fraction = 2)
	private BigDecimal valorTotal;

	@OneToMany(targetEntity = ItemNotaFiscal.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "notaFiscal")
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_NOTAFISCAL")
	@PlcValDuplicity(property = "codigoProduto")
	@PlcValMultiplicity(referenceProperty = "codigoProduto", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.ItemNotaFiscal}")
	@Valid
	private List<ItemNotaFiscal> itemNotaFiscal;

	@OneToMany(targetEntity = ContaPagar.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "notaFiscal")
	@ForeignKey(name = "FK_CONTAPAGAR_NOTAFISCAL")
	@PlcValDuplicity(property = "observacao")
	@PlcValMultiplicity(referenceProperty = "observacao", message = "{jcompany.aplicacao.mestredetalhe.multiplicidade.ContaPagar}")
	@Valid
	private List<ContaPagar> contaPagar;

	public NotaFiscal() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Date getDataEntrada() {
		return dataEntrada;
	}

	public void setDataEntrada(Date dataEntrada) {
		this.dataEntrada = dataEntrada;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public List<ItemNotaFiscal> getItemNotaFiscal() {
		return itemNotaFiscal;
	}

	public void setItemNotaFiscal(List<ItemNotaFiscal> itemNotaFiscal) {
		this.itemNotaFiscal = itemNotaFiscal;
	}

	public List<ContaPagar> getContaPagar() {
		return contaPagar;
	}

	public void setContaPagar(List<ContaPagar> contaPagar) {
		this.contaPagar = contaPagar;
	}

	@Override
	public String toString() {
		return getNumero();
	}

}
