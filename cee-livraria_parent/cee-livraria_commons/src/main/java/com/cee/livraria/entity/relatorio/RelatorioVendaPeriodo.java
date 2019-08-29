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
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.produto.Produto;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ITEM_MOVIMENTO")
@SequenceGenerator(name = "SE_ITEM_MOVIMENTO", sequenceName = "SE_ITEM_MOVIMENTO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "RelatorioVendaPeriodo.querySel", query = "select obj.produto as produto, sum(obj.quantidade) as quantidade, sum(obj.valorTotal) as valorTotal from RelatorioVendaPeriodo obj inner join obj.produto obj1 inner join obj.movimento obj2 where obj2.tipo = 'S' and obj2.modo = 'N' group by obj.produto order by obj.produto.titulo asc") })
public class RelatorioVendaPeriodo extends AppBaseEntity {
	private static final long serialVersionUID = -8819729770996656103L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_MOVIMENTO")
	private Long id;

	@ManyToOne(targetEntity = MovimentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMMOVIMENTO_MOVIMENTO")
	@NotNull
	private Movimento movimento;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_ITEMMOVIMENTO_PRODUTO")
	private Produto produto;

	@Digits(integer = 8, fraction = 0)
	private Long quantidade;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorUnitario;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorTotal;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMovimento tipo;

	@Override
	public String toString() {
		
		if (getProduto() != null) {
			return getProduto().toString();
		} else {
			return "Produto";
		}
	}
	
	@Transient
	private transient Date dataInicio;
	
	@Transient
	private transient Date dataFim;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Movimento getMovimento() {
		return movimento;
	}

	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public Long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Long quantidade) {
		this.quantidade = quantidade;
	}

	public BigDecimal getValorUnitario() {
		return valorUnitario;
	}

	public void setValorUnitario(BigDecimal valorUnitario) {
		this.valorUnitario = valorUnitario;
	}

	public BigDecimal getValorTotal() {
		return valorTotal;
	}

	public void setValorTotal(BigDecimal valorTotal) {
		this.valorTotal = valorTotal;
	}

	public TipoMovimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}

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
