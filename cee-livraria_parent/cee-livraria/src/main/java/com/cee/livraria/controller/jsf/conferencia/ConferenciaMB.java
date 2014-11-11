package com.cee.livraria.controller.jsf.conferencia;

import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.LivroEntity;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.ItemConferencia;
import com.cee.livraria.entity.estoque.conferencia.ItemConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.RegraPesquisaLivros;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(entity = com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity.class, 
	components = { @com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(
		clazz = com.cee.livraria.entity.estoque.conferencia.RegraPesquisaLivros.class, property = "regra", separate = true) }, 
		details = { @com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(
				clazz = com.cee.livraria.entity.estoque.conferencia.ItemConferenciaEntity.class, 
				collectionName = "itemConferencia", numNew = 4, onDemand = false, 
				navigation = @com.powerlogic.jcompany.config.aggregation.PlcConfigPagedDetail(numberByPage = 30))
})

@PlcConfigForm(
	formPattern = FormPattern.Mdt, formLayout = @PlcConfigFormLayout(dirBase = "/WEB-INF/fcls/estoque.conferencia")
)
/**
 * Classe de Controle gerada pelo assistente
 */
@SPlcMB
@PlcUriIoC("conferencia")
@PlcHandleException
public class ConferenciaMB extends AppMB {

	@Inject @QPlcDefault 
	protected PlcCreateContextUtil contextMontaUtil;

	@Inject @Named(PlcConstants.PlcJsfConstantes.PLC_CONTROLE_CONVERSACAO)
	protected PlcConversationControl plcControleConversacao;	

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;
	
	private static final long serialVersionUID = 1L;

	/**
	 * Entidade da ação injetado pela CDI
	 */
	@Produces
	@Named("conferencia")
	public ConferenciaEntity createEntityPlc() {
		if (this.entityPlc == null) {
			this.entityPlc = new ConferenciaEntity();
			this.newEntity();
		}
		return (ConferenciaEntity) this.entityPlc;
	}

	private Livro criaArgumentoPesquisaLivro(RegraPesquisaLivros regra) {
		Livro livroArg = (Livro)new LivroEntity();
		livroArg.setTitulo(regra.getTitulo());
		livroArg.setAutor(regra.getAutor());
		livroArg.setCodigoBarras(regra.getCodigoBarras());
		livroArg.setColecao(regra.getColecao());
		livroArg.setEdicao(regra.getEdicao());
		livroArg.setEditora(regra.getEditora());
		livroArg.setEspirito(regra.getEspirito());
		return livroArg;
	}

	/**
	 * buscarItensPorRegraPrecificacao
	 */
	public String buscarItensPorRegra()  {

		if (this.entityPlc!=null) {
			Conferencia conferencia = (Conferencia)this.entityPlc;
			List<ItemConferencia> listaItens = conferencia.getItemConferencia();
			boolean possuiItens = false;
			boolean ok = true;
			
			if (listaItens != null && !listaItens.isEmpty()) {
				for (ItemConferencia item : listaItens) {
					
					if (item.getId() != null) {
						possuiItens = true;
						break;
					}
				}
				
				if (possuiItens) {
					msgUtil.msg("{tabpreco.err.buscar.itensExistentes}", PlcMessage.Cor.msgVermelhoPlc.name());
					ok = false;
				}
			} else {
				msgUtil.msg("{tabpreco.err.buscar.clicarAbaItens}", PlcMessage.Cor.msgAmareloPlc.name());
				ok = false;
			}
			
			if (ok) {
				listaItens.clear();

				//Busca os livros que satisfazem às regras de pesquisa e os adiciona na listaItens
				RegraPesquisaLivros regra = conferencia.getRegra();
				
				Livro livroArg = criaArgumentoPesquisaLivro(regra);

				PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

				Long contalivros = (Long)iocControleFacadeUtil.getFacade().findCount(context, livroArg);
				
				if (contalivros.longValue() > 400) {
					msgUtil.msg("{tabpreco.err.buscar.muitosItensExistentes}", new Object[] {contalivros}, PlcMessage.Cor.msgVermelhoPlc.name());
					ok = false;
				} else {
					List<Livro> livros = (List<Livro>)iocControleFacadeUtil.getFacade().findList(context, livroArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
					
					for (Livro livro : livros) {
						ItemConferencia item = criaNovoItem(conferencia, livro);
						listaItens.add(item);
					}
					
					msgUtil.msg("{tabpreco.ok.buscar}", new Object[] {livros.size()}, PlcMessage.Cor.msgAzulPlc.name());
					msgUtil.msg("{tabpreco.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
					
					plcControleConversacao.setAlertaAlteracaoPlc("S");
				}
			}
		}
		
		return baseEditMB.getDefaultNavigationFlow(); 
	}
	
	private ItemConferencia criaNovoItem(Conferencia conferencia, Livro livro) {
		ItemConferencia item = new ItemConferenciaEntity();
		
		item.setConferencia(conferencia);
		item.setLivro(livro);
		item.setAutor(livro.getAutor());
		
		if (livro.getColecao() != null && livro.getColecao().getId() != null) {
			item.setColecao(livro.getColecao());
		} else {
			item.setColecao(null);
		}
		
		item.setEdicao(livro.getEdicao());
		item.setEditora(livro.getEditora());
		
		if (livro.getEspirito() != null && livro.getEspirito().getId() != null) {
			item.setEspirito(livro.getEspirito());
		} else {
			item.setEspirito(null);
		}
		
		//TODO: Definir a localizacao
		item.setLocalizacao(null);
		return item;
	}
	
	/**
	 * Exclui grafo do Value Object principal (ie. Mestre, seus Detalhes e
	 * Sub-Detalhe eventuais). Mas somente faz isso se já tiver recuperado os detalhes
	 */
	@Override
	public String delete()  {
		
		if (this.entityPlc!=null) {
			Conferencia conferencia = (Conferencia)this.entityPlc;
			List<ItemConferencia> listaItens = conferencia.getItemConferencia();
			
			if (listaItens == null ) {
				msgUtil.msg("{tabpreco.err.buscar.clicarAbaItens}", PlcMessage.Cor.msgAmareloPlc.name());
				return baseEditMB.getDefaultNavigationFlow(); 
			}
		}
		
		return baseDeleteMB.delete(entityPlc);
	}
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA);
		
		if (nomeAction.contains("conferencia")) {
			boolean possuiItens = false;
			
			if (this.entityPlc!=null) {
				Conferencia conferencia = (Conferencia)this.entityPlc;
				
				if (conferencia.getId() != null) {
					List<ItemConferencia> listaItens = conferencia.getItemConferencia();
					
					if (listaItens != null && !listaItens.isEmpty()) {
						for (ItemConferencia item : listaItens) {
							
							if (item.getId() != null) {
								possuiItens = true;
								break;
							}
						}
					}
				}
			}
			
			if (possuiItens) {
				contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.NAO);
			} else {
				contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.SIM);
			}
		}
		
		super.handleButtonsAccordingFormPattern();
	}
	
}
