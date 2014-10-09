/*  																													
	    				   jCompany Developer Suite																		
			    		Copyright (C) 2008  Powerlogic																	
	 																													
	    Este programa é licenciado com todos os seus códigos fontes. Você pode modificá-los e							
	    utilizá-los livremente, inclusive distribuí-los para terceiros quando fizerem parte de algum aplicativo 		
	    sendo cedido, segundo os termos de licenciamento gerenciado de código aberto da Powerlogic, definidos			
	    na licença 'Powerlogic Open-Source Licence 2.0 (POSL 2.0)'.    													
	  																													
	    A Powerlogic garante o acerto de erros eventualmente encontrados neste código, para os clientes licenciados, 	
	    desde que todos os códigos do programa sejam mantidos intactos, sem modificações por parte do licenciado. 		
	 																													
	    Você deve ter recebido uma cópia da licença POSL 2.0 juntamente com este programa.								
	    Se não recebeu, veja em <http://www.powerlogic.com.br/licencas/posl20/>.										
	 																													
	    Contatos: plc@powerlogic.com.br - www.powerlogic.com.br 														
																														
 */ 
package com.cee.livraria.jcompanyqa;

//import com.powerlogic.jcompany.qa.junit.SuiteFuncional;
import junit.framework.Test;

/**
 * 
 * Suite de testes funcionais
 * Para rodar os testes funcionais, é necessário que esteja no classpath do aplicativo referencia Ã  biblioteca
 * jCompany QA: jcompany_qa_funcional, jcompany_qa_funcional_xml e jcompany_qa_funcional_modelo.
 * Ã necessário também que seja adicionada a dependencia para o projeto jcompany_qa_funcional no pom.xml do projeto.
 * Caso tenha sido preenchido o campo de "Amostra Teste" junto com a criação do caso de uso, as açoes acima já
 * terão sido automaticamente executadas, caso contrario, terão que ser feitas manualmente.
 * Para maiores informações cosulte o manual jCompany QA Functional Tests.
 * 
 * Para rodar os testes funcionais em uma suite de testes, utilize o código comentado no metodo estatico suite.
 * Cada teste funcional terá que ser adicionado a suite utilizando o método addTestSuite.
 * 
 */
public class AppGeralWebTestSuite {

	public static Test suite() {
		
		//SuiteFuncional suite = new SuiteFuncional();

		//suite.addTestSuite(MeuWebTest1.class);
		//suite.addTestSuite(MeuWebTest2.class);
		
		//return suite;
		return null;
	}
}
