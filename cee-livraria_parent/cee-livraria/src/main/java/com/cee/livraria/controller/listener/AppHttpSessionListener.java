/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.cee.livraria.controller.listener;

import javax.servlet.http.HttpSessionEvent;

import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.controller.cache.PlcCacheSessionVO;
import com.powerlogic.jcompany.controller.listener.PlcHttpSessionListener;

/**
 * rhdemofcls. Classe que implementa o DP "Listener" para Sessão
 */
public class AppHttpSessionListener extends PlcHttpSessionListener {
	/**
	 * rhdemofcls: Realiza procedimentos no momento de encerramento de cada Sessão 
	 */
    @Override
	public void aoEncerrarSessao (HttpSessionEvent event) {
		
    	log.info( "Aplicacao: Encerrando Sessao");
	}
	
	/**
	 *  rhdemofcls: Realiza procedimentos no momento de inicialização de cada Sessão 
	 */
    @Override
	public void aoInicializarSessao (HttpSessionEvent event, PlcCacheSessionVO plcSessao) {
								
		log.info( "Aplicacao: Iniciando Sessao");
		
		// Coloca Javascript do jcompany
		event.getSession().setAttribute(PlcConstants.JAVASCRIPT.JAVASCRIPT_JCOMPANY,"/res/javascript/plc.geral.js");
		// Coloca Javascript complementar da aplicação
		event.getSession().setAttribute(PlcConstants.JAVASCRIPT.JAVASCRIPT_ESPECIFICO,"/res/javascript/app.geral.js");
		// Coloca CSS default
		event.getSession().setAttribute(PlcConstants.GUI.PELE.CSS_ESPECIFICO,"/res/css/" + plcSessao.getPele() +"/PlcPele.css");
			
		// JSF - Coloca informacoes de leiaute na sessao
		event.getSession().setAttribute(PlcConstants.SESSION_CACHE_KEY,plcSessao);
		
		// Auxiliar provisorio para manter compatibilidade
		event.getSession().setAttribute("contextPathPlc","./../");

	}
}
