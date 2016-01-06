package com.cee.livraria.entity.estoque;

import java.math.BigDecimal;

import com.cee.livraria.entity.AppBaseEntity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import javax.persistence.Id;

import com.cee.livraria.entity.produto.Livro;
import com.cee.livraria.entity.produto.Livro;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.GenerationType;
import org.hibernate.annotations.ForeignKey;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import javax.persistence.FetchType;
import javax.validation.constraints.Digits;
import javax.persistence.GeneratedValue;

/**
 * @author joao.machado
 *
 */
@MappedSuperclass
public abstract class ItemMovimento extends AppBaseEntity {

	private static final long serialVersionUID = -417905658827262392L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ITEM_MOVIMENTO")
	private Long id;

	@ManyToOne(targetEntity = MovimentoEntity.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMMOVIMENTO_MOVIMENTO")
	@NotNull
	private Movimento movimento;

	@ManyToOne(targetEntity = Livro.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ITEMMOVIMENTO_LIVRO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	private Livro livro;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "livro", is = RequiredIfType.not_empty)
	@Digits(integer = 8, fraction = 0)
	private Integer quantidade;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "livro", is = RequiredIfType.not_empty)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorUnitario;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "livro", is = RequiredIfType.not_empty)
	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorTotal;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMovimento tipo;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Livro getLivro() {
		return livro;
	}

	public void setLivro(Livro livro) {
		this.livro = livro;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
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

	public Movimento getMovimento() {
		return movimento;
	}

	public void setMovimento(Movimento movimento) {
		this.movimento = movimento;
	}

	public TipoMovimento getTipo() {
		return tipo;
	}

	public void setTipo(TipoMovimento tipo) {
		this.tipo = tipo;
	}

}
