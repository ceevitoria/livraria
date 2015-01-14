/* ************************* META-DADOS GLOBAIS DA APLICAÇÃO ******************************
  ********************** Configurações padrão para toda a aplicação *************************
  ************ Obs: configurações corporativas devem estar no nível anterior,****************
  ************              preferencialmente na camada Bridge               ****************
  *******************************************************************************************/

@PlcConfigApplication(
	definition=@PlcConfigApplicationDefinition(name="Controle da Livraria",acronym="Livraria",version=1,release=0),
	classesDiscreteDomain={
		com.powerlogic.jcompany.domain.type.PlcYesNo.class,
		com.cee.livraria.entity.estoque.conferencia.StatusConferencia.class,
		com.cee.livraria.entity.caixa.StatusCaixa.class,
		com.cee.livraria.entity.config.TipoMensagemSucessoVendaConfig.class,
		com.cee.livraria.entity.tabpreco.TipoPrecificacao.class,
		com.cee.livraria.entity.tabpreco.TipoVariacao.class,
		com.cee.livraria.entity.tabpreco.TipoArredondamento.class},
	classesLookup={
		com.cee.livraria.entity.LocalizacaoEntity.class,
		com.cee.livraria.entity.EspiritoEntity.class,
		com.cee.livraria.entity.pagamento.FormaPagtoEntity.class,
		com.cee.livraria.entity.pagamento.FormaPagLivroEntity.class,
		com.cee.livraria.entity.AutorEntity.class,
		com.cee.livraria.entity.EditoraEntity.class,
		com.cee.livraria.entity.ColecaoEntity.class}
)

package com.powerlogic.jcompany.config.app;

import com.powerlogic.jcompany.config.application.PlcConfigApplication;
import com.powerlogic.jcompany.config.application.PlcConfigApplicationDefinition;
