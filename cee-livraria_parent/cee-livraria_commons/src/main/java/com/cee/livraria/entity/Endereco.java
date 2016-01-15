package com.cee.livraria.entity;

import java.io.Serializable;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

@Embeddable
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "Endereco.querySelLookup", query = "select logradouro as logradouro from Endereco where id = ? order by logradouro asc") })
public class Endereco implements Serializable {
	private static final long serialVersionUID = 8179571396808032690L;

	@Size(max = 150)
	private String logradouro;

	@Size(max = 30)
	private String complemento;

	@Size(max = 10)
	private String numero;

	@Size(max = 150)
	private String bairro;

	@Size(max = 8)
	private String cep;

	@ManyToOne(targetEntity = Cidade.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ENDERECO_CIDADE")
	private Cidade cidade;

	@ManyToOne(targetEntity = Uf.class, fetch = FetchType.LAZY)
	@ForeignKey(name = "FK_ENDERECO_UF")
	private Uf uf;

	public Endereco() {
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public Uf getUf() {
		return uf;
	}

	public void setUf(Uf uf) {
		this.uf = uf;
	}

	@Override
	public String toString() {
		return getLogradouro();
	}

}
