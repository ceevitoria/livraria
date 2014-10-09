package com.cee.livraria.entity.estoque.apoio;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIf;
import org.apache.myfaces.extensions.validator.crossval.annotation.RequiredIfType;

import com.cee.livraria.entity.Livro;
import com.powerlogic.jcompany.config.domain.PlcReference;
import com.powerlogic.jcompany.domain.validation.PlcValGroupEntityList;

//@SPlcEntity
//@Entity
//@SequenceGenerator(name = "", sequenceName = "")
@Access(AccessType.FIELD)
//@NamedQueries({
//	@NamedQuery(name="VendaLivro.queryMan", query="from VendaLivro"), @NamedQuery(name = "VendaLivro.querySelLookup", query = "select id as id from VendaLivro where id = ? order by id asc") })
public class VendaLivro {

	@Id
	private Long id;

	private Livro livro;

	@Transient
	private String nomeTabela = " ";
	
	@Digits(integer = 8, fraction = 0)
	private Integer quantidade;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorUnitario;

	@Digits(integer = 10, fraction = 2)
	private BigDecimal valorTotal;

	@Transient
	private String indExcPlc = "N";

	public void setIndExcPlc(String indExcPlc) {
		this.indExcPlc = indExcPlc;
	}

	public String getIndExcPlc() {
		return indExcPlc;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNomeTabela() {
		return nomeTabela;
	}
	
	public void setNomeTabela(String nomeTabela) {
		this.nomeTabela = nomeTabela;
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

}
