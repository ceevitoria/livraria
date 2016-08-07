package com.cee.livraria.entity.estoque;

import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;

import com.cee.livraria.entity.AppBaseEntity;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;

@SPlcEntity
@Entity
@Table(name = "ESTOQUE")
@SequenceGenerator(name = "SE_ESTOQUE", sequenceName = "SE_ESTOQUE")
@Access(AccessType.FIELD)
@NamedQueries({ 
	@NamedQuery(name = "Estoque.querySel", query = "select obj.id, obj.quantidade, obj.dataConferencia from Estoque obj order by obj.quantidade asc"),
	@NamedQuery(name = "Estoque.querySelLookup", query = "select id as id, quantidade as quantidade from Estoque where id = ? order by id asc")})
public class Estoque extends AppBaseEntity {
	private static final long serialVersionUID = -1730467297686228589L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_ESTOQUE")
	private Long id;

	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidade;

	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeMinima;
	
	@NotNull
	@Digits(integer = 5, fraction = 0)
	private Integer quantidadeMaxima;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataConferencia;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}

	public Integer getQuantidadeMinima() {
		return quantidadeMinima;
	}
	
	public void setQuantidadeMinima(Integer quantidadeMinima) {
		this.quantidadeMinima = quantidadeMinima;
	}
	
	public Integer getQuantidadeMaxima() {
		return quantidadeMaxima;
	}
	
	public void setQuantidadeMaxima(Integer quantidadeMaxima) {
		this.quantidadeMaxima = quantidadeMaxima;
	}
	
	public Date getDataConferencia() {
		return dataConferencia;
	}
	
	public void setDataConferencia(Date dataConferencia) {
		this.dataConferencia = dataConferencia;
	}

}


