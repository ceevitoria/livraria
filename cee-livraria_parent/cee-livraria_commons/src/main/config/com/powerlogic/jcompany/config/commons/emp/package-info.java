/* ******************* REDEFINIÇÃES DE META-DADOS GLOBAIS DA EMPRESA **********************
  ********************** Configurações default para toda a empresa *************************
  *******************************************************************************************/
			
@PlcConfigCompany(name = "Powerlogic SA", domain = "www.powerlogic.com.br", acronym = "PLC", 
			logo = "/recursos/midia/login-logo-empresa.png", address = "Av. Barão Homem de Melo, 4.554 / 2Âº andar. CEP:30.494-270, Estoril - Belo Horizonte/MG",
			supportEmail = "suporte@powerlogic.com.br",supportPhone = "55 31 3555-0050")
			
@PlcConfigSuffixClass(entity="Entity",repository="Repository")

@PlcConfigPackage (entity=".entity.", application="com.cee.livraria")
			    	 
package com.powerlogic.jcompany.config.commons.emp;

import com.powerlogic.jcompany.config.application.PlcConfigCompany;
import com.powerlogic.jcompany.config.application.PlcConfigPackage;
import com.powerlogic.jcompany.config.application.PlcConfigSuffixClass;

