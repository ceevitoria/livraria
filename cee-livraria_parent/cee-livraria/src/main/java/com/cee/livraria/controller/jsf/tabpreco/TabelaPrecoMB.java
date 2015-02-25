package com.cee.livraria.controller.jsf.tabpreco;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import com.cee.livraria.controller.jsf.AppMB;
import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.LivroEntity;
import com.cee.livraria.entity.tabpreco.ItemTabela;
import com.cee.livraria.entity.tabpreco.ItemTabelaEntity;
import com.cee.livraria.entity.tabpreco.RegraPesquisaLivros;
import com.cee.livraria.entity.tabpreco.TabelaPreco;
import com.cee.livraria.entity.tabpreco.TabelaPrecoEntity;
import com.cee.livraria.entity.tabpreco.TipoArredondamento;
import com.cee.livraria.entity.tabpreco.TipoPrecificacao;
import com.cee.livraria.entity.tabpreco.TipoVariacao;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcConstants;
import com.powerlogic.jcompany.commons.annotation.PlcUriIoC;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcMB;
import com.powerlogic.jcompany.config.aggregation.PlcConfigAggregation;
import com.powerlogic.jcompany.config.collaboration.FormPattern;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm;
import com.powerlogic.jcompany.config.collaboration.PlcConfigForm.ExclusionMode;
import com.powerlogic.jcompany.config.collaboration.PlcConfigFormLayout;
import com.powerlogic.jcompany.controller.jsf.action.util.PlcConversationControl;
import com.powerlogic.jcompany.controller.jsf.annotations.PlcHandleException;
import com.powerlogic.jcompany.controller.jsf.util.PlcCreateContextUtil;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;
import com.powerlogic.jcompany.domain.validation.PlcMessage;

@PlcConfigAggregation(
	entity = com.cee.livraria.entity.tabpreco.TabelaPrecoEntity.class

	,components = {@com.powerlogic.jcompany.config.aggregation.PlcConfigComponent(
		clazz=com.cee.livraria.entity.tabpreco.RegraPesquisaLivros.class, property="regra", separate=true)}
	,details = { 		
		@com.powerlogic.jcompany.config.aggregation.PlcConfigDetail(clazz = com.cee.livraria.entity.tabpreco.ItemTabelaEntity.class,
			collectionName = "itemTabela", numNew = 2, onDemand = true, exclusionMode=ExclusionMode.LOGICAL)
	}
)

@PlcConfigForm (
	formPattern=FormPattern.Mdt,
	formLayout = @PlcConfigFormLayout(dirBase="/WEB-INF/fcls/tabpreco")
	,exclusionMode=ExclusionMode.LOGICAL
)


/**
 * Classe de Controle gerada pelo assistente
 */
 

@SPlcMB
@PlcUriIoC("tabpreco")
@PlcHandleException
public class TabelaPrecoMB extends AppMB  {
	
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
	@Produces  @Named("tabpreco")
	public TabelaPrecoEntity createEntityPlc() {
        if (this.entityPlc==null) {
              this.entityPlc = new TabelaPrecoEntity();
              this.newEntity();
        }
        return (TabelaPrecoEntity)this.entityPlc;     	
	}
		
	/**
	 * buscarItensPorRegraPrecificacao
	 */
	public String buscarItensPorRegra()  {

		if (this.entityPlc!=null) {
			TabelaPreco tabPreco = (TabelaPreco)this.entityPlc;
			List<ItemTabela> listaItens = tabPreco.getItemTabela();
			boolean possuiItens = false;
			boolean ok = true;
			
			if (listaItens != null && !listaItens.isEmpty()) {
				for (ItemTabela item : listaItens) {
					
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
				RegraPesquisaLivros regra = tabPreco.getRegra();
				
				Livro livroArg = criaArgumentoPesquisaLivro(regra);

				PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

				Long contalivros = (Long)iocControleFacadeUtil.getFacade().findCount(context, livroArg);
				
				if (contalivros.longValue() > 400) {
					msgUtil.msg("{tabpreco.err.buscar.muitosItensExistentes}", new Object[] {contalivros}, PlcMessage.Cor.msgVermelhoPlc.name());
					ok = false;
				} else {
					List<Livro> livros = (List<Livro>)iocControleFacadeUtil.getFacade().findList(context, livroArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
					
					for (Livro livro : livros) {
						ItemTabela item = criaNovoItem(tabPreco, livro);
						
						//Definir o ajuste de preço
						double preco = calcularAjustePrecoLivro(tabPreco, livro);
						
						item.setPreco(new BigDecimal(preco) );
						
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

	private double calcularAjustePrecoLivro(TabelaPreco tabPreco, Livro livro) {
		double variacao = 0;
		double preco = 0;
		double fator = 0;
		
		variacao = tabPreco.getVariacao().doubleValue();
		preco = livro.getPrecoUltCompra().doubleValue();
		
		if (tabPreco.getTipoPrecificacao().equals(TipoPrecificacao.A)) {
			
			if (tabPreco.getTipoVariacao().equals(TipoVariacao.V)) {
				preco += variacao;
			} else {
				preco *= (1 + (variacao / 100.0));
			}
		} else {
			
			if (tabPreco.getTipoVariacao().equals(TipoVariacao.V)) {
				preco -= variacao;
			} else {
				preco *= (1 - (variacao / 100.0));
			}
		}

		//Arredondamentos
		if (TipoArredondamento.DC.equals(tabPreco.getTipoArredondamento())) {
			fator = 10;
		} else  if (TipoArredondamento.UN.equals(tabPreco.getTipoArredondamento())) {
			fator = 1;
		} else  if (TipoArredondamento.DE.equals(tabPreco.getTipoArredondamento())) {
			fator = 0.1;
		}
		
		preco *= fator;
		preco = Math.round(preco);
		preco /= fator;
		return preco;
	}

	/**
	 * atualizarItensPorPrecificacao
	 */
	public String atualizarItensPorPrecificacao()  {
		TabelaPreco tabPreco = (TabelaPreco)this.entityPlc;
		List<ItemTabela> listaItens = tabPreco.getItemTabela();

		//Busca os livros que satisfazem às regras de pesquisa e os adiciona na listaItens
		RegraPesquisaLivros regra = tabPreco.getRegra();
		
		PlcBaseContextVO context = contextMontaUtil.createContextParam(plcControleConversacao);

		Livro livroArg = criaArgumentoPesquisaLivro(regra);

		Long contalivros = (Long)iocControleFacadeUtil.getFacade().findCount(context, livroArg);
		
		if (contalivros.longValue() > 400) {
			msgUtil.msg("{tabpreco.err.buscar.muitosItensExistentes}", new Object[] {contalivros}, PlcMessage.Cor.msgVermelhoPlc.name());
		} else {
			
			if (contalivros.longValue() > listaItens.size()) {
				long numNovos = contalivros.longValue() - listaItens.size();

				Comparator<ItemTabela> comparator = new Comparator<ItemTabela>() {
					  public int compare(ItemTabela o1, ItemTabela o2) {
					    if (o1.getCodigoBarras().equals(o2.getCodigoBarras())) {
					      return o1.getLivro().getId().compareTo(o2.getLivro().getId());
					    }
					    return o1.getCodigoBarras().compareTo(o2.getCodigoBarras());
					  }
					};	
				
				Collections.sort(listaItens, comparator);
				
				List<Livro> livros = (List<Livro>)iocControleFacadeUtil.getFacade().findList(context, livroArg, plcControleConversacao.getOrdenacaoPlc(), 0, 0);
				
				for (Livro livro : livros) {
					ItemTabela item = criaNovoItem(tabPreco, livro);
					
					double preco = calcularAjustePrecoLivro(tabPreco, livro);
					
					item.setPreco(new BigDecimal(preco) );
					
					int i = Collections.binarySearch(listaItens, item, comparator);
					
					if (i < 0) {
						listaItens.add(item);
					}
				}
				
				msgUtil.msg("{tabpreco.ok.atualizar.inserir}", new Object[] {listaItens.size(), numNovos}, PlcMessage.Cor.msgAzulPlc.name());
				msgUtil.msg("{tabpreco.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
				plcControleConversacao.setAlertaAlteracaoPlc("S");
			} else {
				for (ItemTabela item : listaItens) {
					Livro livro = (Livro)iocControleFacadeUtil.getFacade().edit(context, LivroEntity.class, item.getLivro().getId())[0] ;
					
					item.setTitulo(livro.getTitulo());
					item.setAutor(livro.getAutor());
					item.setCodigoBarras(livro.getCodigoBarras());
					item.setColecao(livro.getColecao());
					item.setEdicao(livro.getEdicao());
					item.setEditora(livro.getEditora());
					item.setEspirito(livro.getEspirito());
					
					//TODO: Definir a localizacao
					item.setLocalizacao(null);
					
					double preco = calcularAjustePrecoLivro(tabPreco, livro);
					
					item.setPreco(new BigDecimal(preco) );
				}
				
				msgUtil.msg("{tabpreco.ok.atualizar}", new Object[] {listaItens.size()}, PlcMessage.Cor.msgAzulPlc.name());
				msgUtil.msg("{tabpreco.lembrar.gravar}", PlcMessage.Cor.msgAmareloPlc.name());
			}
				
		}

		return baseEditMB.getDefaultNavigationFlow(); 
	}

	private ItemTabela criaNovoItem(TabelaPreco tabPreco, Livro livro) {
		ItemTabela item = new ItemTabelaEntity();
		
		item.setTabelaPreco(tabPreco);
		item.setLivro(livro);
		item.setTitulo(livro.getTitulo());
		item.setAutor(livro.getAutor());
		item.setCodigoBarras(livro.getCodigoBarras());
		
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
			TabelaPreco tabPreco = (TabelaPreco)this.entityPlc;
			List<ItemTabela> listaItens = tabPreco.getItemTabela();
			
			if (listaItens == null ) {
				msgUtil.msg("{tabpreco.err.buscar.clicarAbaItens}", PlcMessage.Cor.msgAmareloPlc.name());
				return baseEditMB.getDefaultNavigationFlow(); 
			}
		}
		
		return baseDeleteMB.delete(entityPlc);
	}
	
	public void handleButtonsAccordingFormPattern() {
		String nomeAction = (String) contextUtil.getRequestAttribute(PlcConstants.PlcJsfConstantes.URL_COM_BARRA);
		
		if (nomeAction.contains("mdt")) {
			boolean possuiItens = false;
			
			if (this.entityPlc!=null) {
				TabelaPreco tabPreco = (TabelaPreco)this.entityPlc;
				
				if (tabPreco.getId() != null) {
					List<ItemTabela> listaItens = tabPreco.getItemTabela();
					
					if (listaItens != null && !listaItens.isEmpty()) {
						for (ItemTabela item : listaItens) {
							
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
				contextUtil.getRequest().setAttribute("atualizarItensPorPrecificacao", PlcConstants.SIM);
			} else {
				contextUtil.getRequest().setAttribute("exibeBuscarItensPorRegra", PlcConstants.SIM);
				contextUtil.getRequest().setAttribute("atualizarItensPorPrecificacao", PlcConstants.NAO);
			}
		}

		
		super.handleButtonsAccordingFormPattern();
	}
	
}
