package com.cee.livraria.entity.compra;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;
import org.hibernate.annotations.ForeignKey;

import com.cee.livraria.entity.AppBaseEntity;
import com.cee.livraria.entity.produto.Produto;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

@SPlcEntity
@Entity
@Table(name = "FORNECEDOR_PRODUTO")
@SequenceGenerator(name = "SE_FORNECEDOR_PRODUTO", sequenceName = "SE_FORNECEDOR_PRODUTO")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "FornecedorProduto.querySelLookup", query = "select id as id, codigoProduto as codigoProduto from FornecedorProduto where id = ? order by id asc") })
public class FornecedorProduto extends AppBaseEntity {
	private static final long serialVersionUID = -305835460793103573L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_FORNECEDOR_PRODUTO")
	private Long id;

	@ManyToOne(targetEntity = Fornecedor.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_FORNECEDORPRODUTO_FORNECEDOR")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	private Fornecedor fornecedor;

	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "id", is = RequiredIfType.not_empty)
	@Size(max = 5)
	private String codigoProduto;

	@ManyToOne(targetEntity = Produto.class, fetch = FetchType.EAGER)
	@ForeignKey(name = "FK_FORNECEDORPRODUTO_PRODUTO")
	@NotNull(groups = PlcValGroupEntityList.class)
	@RequiredIf(valueOf = "codigoProduto", is = RequiredIfType.not_empty)
	private Produto produto;

	public FornecedorProduto() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	public String getCodigoProduto() {
		return codigoProduto;
	}

	public void setCodigoProduto(String codigoProduto) {
		this.codigoProduto = codigoProduto;
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	@Override
	public String toString() {
		return getCodigoProduto();
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
