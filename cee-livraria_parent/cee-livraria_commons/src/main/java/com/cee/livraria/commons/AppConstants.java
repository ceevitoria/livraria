/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.cee.livraria.commons;

import com.powerlogic.jcompany.commons.PlcConstants;

/**
 * Implementar aqui constantes específicas da Aplicação
 * 
 */
public interface AppConstants extends PlcConstants {
    
    interface ACAO {
    	String EXIBE_BT_LIMPAR = "exibeBtnLimpar";
    	String EXIBE_BT_NOVO = "exibeBtnNovo";
    	String EXIBE_BT_BUSCAR_VENDA = "exibeBtnBuscarDadosVenda";
    	String EXIBE_BT_REGISTRAR_VENDA = "exibeBtnRegistrarVenda";
    	String EXIBE_BT_REGISTRAR_ABERTURA_CAIXA = "exibeBtnRegistrarAberturaCaixa";
    	String EXIBE_BT_REGISTRAR_SANGRIA_CAIXA = "exibeBtnRegistrarSangriaCaixa";
    	String EXIBE_BT_REGISTRAR_SUPRIMENTO_CAIXA = "exibeBtnRegistrarSuprimentoCaixa";
    	String EXIBE_BT_REGISTRAR_FECHAMENTO_CAIXA = "exibeBtnRegistrarFechamentoCaixa";
    	
    	String EXIBE_BT_ABRIR_CONFERENCIA = "exibeBtnAbrirConferencia";
    	String EXIBE_BT_CONCLUIR_CONFERENCIA = "exibeBtnConcluirConferencia";
    }
    
    interface TELA_CONFEERENCIA {
    	String UTILIZA_lOCALIZACAO_CONFERENCIA = "utilizaLocalizacaoConferencia";
    	
    }
    
}
