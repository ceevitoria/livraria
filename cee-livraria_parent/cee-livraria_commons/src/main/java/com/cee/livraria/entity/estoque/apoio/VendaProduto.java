package com.cee.livraria.entity.estoque.apoio;

import java.math.BigDecimal;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;

import com.cee.livraria.entity.produto.Livro;

@Access(AccessType.FIELD)
public class VendaProduto {

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
