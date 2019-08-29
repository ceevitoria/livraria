package com.cee.livraria.entity.relatorio;

import java.math.BigDecimal;
import java.util.Date;

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
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.compra.Fornecedor;
import com.cee.livraria.entity.compra.NotaFiscal;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.produto.Produto;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ITEM_NOTA_FISCAL")
@SequenceGenerator(name = "SE_ITEM_NOTA_FISCAL", sequenceName = "SE_ITEM_NOTA_FISCAL")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "RelatorioUltimasComprasPeriodo.querySel", query = 
	"select obj.produto as produto,  " +
	"       obj.valorUnitario as valorUnitario, " +
	"       obj.quantidade as quantidade,       " +
	"       obj.notaFiscal as notaFiscal,       " +
	"       est as estoque,                     " +
	"       for as fornecedor                   " +
	"  from RelatorioUltimasComprasPeriodo obj, " + 
	"       Estoque est                    " +
	"     inner join obj.produto obj1      " +
	"     inner join obj.notaFiscal obj2   " +
	"     inner join obj2.fornecedor for   " +
	"     inner join obj1.localizacao loc  " +
	"where obj1.id = est.id                " +
	"   and obj2.dataEmissao = (           " +
	"	select max(sobj2.dataEmissao)      " +
	"	  from RelatorioUltimasComprasPeriodo sobj " +
	"	     inner join sobj.produto sobj1         " +
	"	     inner join sobj.notaFiscal sobj2      " +
	"	  where obj1.id = sobj1.id)                " )})
public class RelatorioUltimasComprasPeriodo extends AppBaseEntity {
	private static final long serialVersionUID = -8819729450996123103L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_NOTA_FISCAL")
	private Long id;

	@ManyToOne(targetEntity = NotaFiscal.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_NOTAFISCAL")
	@NotNull
	private NotaFiscal notaFiscal;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ITEMNOTAFISCAL_PRODUTO")
	private Produto produto;
	
	@ManyToOne(targetEntity = Estoque.class, fetch = FetchType.EAGER)
	private Estoque estoque;
	
	@ManyToOne(targetEntity = Fornecedor.class, fetch = FetchType.EAGER)
	private Fornecedor fornecedor;
	
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorUnitario;

	@Digits(integer = 5, fraction = 0)
	private Integer quantidade;

	@Override
	public String toString() {
		
		if (getProduto() != null) {
			return getProduto().toString();
		} else {
			return "Produto";
		}
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public NotaFiscal getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(NotaFiscal notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Estoque getEstoque() {
		return estoque;
	}

	public void setEstoque(Estoque estoque) {
		this.estoque = estoque;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	@Transient
	private transient Date dataInicio;
	
	@Transient
	private transient Date dataFim;
	
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

}
