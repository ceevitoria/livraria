/* Jaguar-jCompany Developer Suite. Powerlogic 2010-2014. Please read licensing information or contact Powerlogic 
 * for more information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br        */ 
package com.cee.livraria.commons;

import com.powerlogic.jcompany.commons.PlcBaseUserProfileVO;
import javax.enterprise.inject.Specializes;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;

/**
* cee-livraria. Implementar aqui atributos de personalização
* específicos do usuario.
*/
@QPlcDefault
@Specializes
public class AppUserProfileVO extends PlcBaseUserProfileVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
}
