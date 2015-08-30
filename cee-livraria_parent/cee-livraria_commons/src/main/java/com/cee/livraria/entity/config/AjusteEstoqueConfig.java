package com.cee.livraria.entity.config;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.powerlogic.jcompany.commons.config.stereotypes.SPlcEntity;
import com.powerlogic.jcompany.domain.type.PlcYesNo;

@SPlcEntity
@Entity
@Table(name = "AJUSTEESTOQUE_CONFIG")
@SequenceGenerator(name = "SE_AJUSTEESTOQUE_CONFIG", sequenceName = "SE_AJUSTEESTOQUE_CONFIG")
@Access(AccessType.FIELD)
@NamedQueries({ @NamedQuery(name = "AjusteEstoqueConfig.querySelLookup", query = "select id as id, utilizaLocalizacaoLivros as utilizaLocalizacaoLivros from AjusteEstoqueConfig where id = ? order by id asc") })
public class AjusteEstoqueConfig extends Config {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_AJUSTEESTOQUE_CONFIG")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo utilizaLocalizacaoLivros = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo ajusteAutomaticoLocalizacaoLivros = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMensagemAjusteEstoqueConfig tipoMensagem = TipoMensagemAjusteEstoqueConfig.Q;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlcYesNo getUtilizaLocalizacaoLivros() {
		return utilizaLocalizacaoLivros;
	}

	public void setUtilizaLocalizacaoLivros(PlcYesNo utilizaLocalizacaoLivros) {
		this.utilizaLocalizacaoLivros = utilizaLocalizacaoLivros;
	}

	public PlcYesNo getAjusteAutomaticoLocalizacaoLivros() {
		return ajusteAutomaticoLocalizacaoLivros;
	}

	public void setAjusteAutomaticoLocalizacaoLivros(
			PlcYesNo ajusteAutomaticoLocalizacaoLivros) {
		this.ajusteAutomaticoLocalizacaoLivros = ajusteAutomaticoLocalizacaoLivros;
	}

	public TipoMensagemAjusteEstoqueConfig getTipoMensagem() {
		return tipoMensagem;
	}

	public void setTipoMensagem(TipoMensagemAjusteEstoqueConfig tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}

	/*
	 * Construtor padrao
	 */
	public AjusteEstoqueConfig() {
	}

	@Override
	public String toString() {
		return getUtilizaLocalizacaoLivros().toString();
	}
	
}
