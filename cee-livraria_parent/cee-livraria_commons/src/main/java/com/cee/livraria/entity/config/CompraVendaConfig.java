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
public abstract class CompraVendaConfig extends Config {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "SE_COMPRA_VENDA_CONFIG")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo alertaEstoqueMinimoAtingido = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo alertaEstoqueNegativoAtingido = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo permiteVendaParaEstoqueNegativo = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo autoLimparTelaParaNovaVenda = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@Column(length = 1)
	private PlcYesNo exibirMensagemSucessoVenda = PlcYesNo.S;

	@Enumerated(EnumType.STRING)
	@NotNull
	@Column(length = 1)
	private TipoMensagemSucessoVendaConfig tipoMensagemSucessoVenda = TipoMensagemSucessoVendaConfig.V;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PlcYesNo getAlertaEstoqueMinimoAtingido() {
		return alertaEstoqueMinimoAtingido;
	}

	public void setAlertaEstoqueMinimoAtingido(
			PlcYesNo alertaEstoqueMinimoAtingido) {
		this.alertaEstoqueMinimoAtingido = alertaEstoqueMinimoAtingido;
	}

	public PlcYesNo getAlertaEstoqueNegativoAtingido() {
		return alertaEstoqueNegativoAtingido;
	}

	public void setAlertaEstoqueNegativoAtingido(
			PlcYesNo alertaEstoqueNegativoAtingido) {
		this.alertaEstoqueNegativoAtingido = alertaEstoqueNegativoAtingido;
	}

	public PlcYesNo getPermiteVendaParaEstoqueNegativo() {
		return permiteVendaParaEstoqueNegativo;
	}

	public void setPermiteVendaParaEstoqueNegativo(
			PlcYesNo permiteVendaParaEstoqueNegativo) {
		this.permiteVendaParaEstoqueNegativo = permiteVendaParaEstoqueNegativo;
	}

	public PlcYesNo getAutoLimparTelaParaNovaVenda() {
		return autoLimparTelaParaNovaVenda;
	}

	public void setAutoLimparTelaParaNovaVenda(
			PlcYesNo autoLimparTelaParaNovaVenda) {
		this.autoLimparTelaParaNovaVenda = autoLimparTelaParaNovaVenda;
	}

	public PlcYesNo getExibirMensagemSucessoVenda() {
		return exibirMensagemSucessoVenda;
	}

	public void setExibirMensagemSucessoVenda(
			PlcYesNo exibirMensagemSucessoVenda) {
		this.exibirMensagemSucessoVenda = exibirMensagemSucessoVenda;
	}

	public TipoMensagemSucessoVendaConfig getTipoMensagemSucessoVenda() {
		return tipoMensagemSucessoVenda;
	}

	public void setTipoMensagemSucessoVenda(
			TipoMensagemSucessoVendaConfig tipoMensagemSucessoVenda) {
		this.tipoMensagemSucessoVenda = tipoMensagemSucessoVenda;
	}

}
