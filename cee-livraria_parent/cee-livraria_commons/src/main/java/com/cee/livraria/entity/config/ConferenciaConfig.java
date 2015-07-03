package com.cee.livraria.entity.config;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

import com.powerlogic.jcompany.domain.type.PlcYesNo;

@MappedSuperclass
public abstract class ConferenciaConfig extends Config {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_CONFERENCIA_CONFIG")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo alertaTrocaLocalizacaoLivros = PlcYesNo.N;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo permiteTrocaLocalizacaoLivros = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMensagemConferenciaConfig tipoMensagem = TipoMensagemConferenciaConfig.Q;

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlcYesNo getAlertaTrocaLocalizacaoLivros() {
		return alertaTrocaLocalizacaoLivros;
	}

	public void setAlertaTrocaLocalizacaoLivros(
			PlcYesNo alertaTrocaLocalizacaoLivros) {
		this.alertaTrocaLocalizacaoLivros = alertaTrocaLocalizacaoLivros;
	}

	public PlcYesNo getPermiteTrocaLocalizacaoLivros() {
		return permiteTrocaLocalizacaoLivros;
	}

	public void setPermiteTrocaLocalizacaoLivros(
			PlcYesNo permiteTrocaLocalizacaoLivros) {
		this.permiteTrocaLocalizacaoLivros = permiteTrocaLocalizacaoLivros;
	}

	public TipoMensagemConferenciaConfig getTipoMensagem() {
		return tipoMensagem;
	}
	
	public void setTipoMensagem(TipoMensagemConferenciaConfig tipoMensagem) {
		this.tipoMensagem = tipoMensagem;
	}
	

}
