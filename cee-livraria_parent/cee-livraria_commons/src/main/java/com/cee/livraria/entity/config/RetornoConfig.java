package com.cee.livraria.entity.config;

import java.util.List;


public class RetornoConfig {
	
	private Config config;
	private List<String> alertas;
	private List<String> mensagens;
	
	public RetornoConfig (Config config, List<String> alertas, List<String> mensagens) {
		this.config = config;
		this.alertas = alertas;
		this.mensagens = mensagens;
	}
	
	public Config getConfig() {
		return config;
	}
	public void setConfig(Config config) {
		this.config = config;
	}
	public List<String> getAlertas() {
		return alertas;
	}
	public void setAlertas(List<String> alertas) {
		this.alertas = alertas;
	}
	public List<String> getMensagens() {
		return mensagens;
	}
	public void setMensagens(List<String> mensagens) {
		this.mensagens = mensagens;
	}
	
}
