package com.cee.livraria.model.conferencia;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.Livro;
import com.cee.livraria.entity.config.ConferenciaConfig;
import com.cee.livraria.entity.config.ConferenciaConfigEntity;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.config.TipoMensagemConferenciaConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.EstoqueEntity;
import com.cee.livraria.entity.estoque.conferencia.Conferencia;
import com.cee.livraria.entity.estoque.conferencia.ConferenciaEntity;
import com.cee.livraria.entity.estoque.conferencia.ItemConferencia;
import com.cee.livraria.entity.estoque.conferencia.ResultadoConferencia;
import com.cee.livraria.entity.estoque.conferencia.StatusConferencia;
import com.cee.livraria.persistence.jpa.conferencia.ConferenciaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.domain.type.PlcYesNo;
import com.powerlogic.jcompany.model.PlcBaseRepository;
import com.powerlogic.jcompany.model.bindingtype.PlcUpdateBefore;

/**
 * Classe de Modelo gerada pelo assistente
 */
 
@SPlcRepository 
@PlcAggregationIoC(clazz=ConferenciaEntity.class)
public class ConferenciaRepository extends PlcBaseRepository {

	@Inject
	private ConferenciaDAO dao;

	@Inject
	protected transient Logger log;

	private ConferenciaConfig config;

	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();

	public void beforeUpdate(@Observes @PlcUpdateBefore PlcBaseContextVO context) throws PlcException {
		
		if (context.getUrl().equalsIgnoreCase("conferencia")) {
			Conferencia conferencia = (Conferencia)context.getEntityForExtension();
	
			if (conferencia != null && StatusConferencia.C.equals(conferencia.getStatus())) {
				List<ItemConferencia> itens = conferencia.getItemConferencia();
				
				if (itens.size() == 0) {
					throw new PlcException("{conferencia.err.sem.itens}");
				}
				
				for (ItemConferencia item: itens) {
					
					if (item.getLocalizacao() == null || item.getLocalizacao().getId() == null) {
						throw new PlcException("{conferencia.err.item.sem.localizacao}");
					}
	
					if (item.getQuantidade() == null ) {
						throw new PlcException("{conferencia.err.item.sem.quantidade}");
					}
				}
			}
		}
	}

	public RetornoConfig concluirConferencia(PlcBaseContextVO context, Conferencia conferencia) throws PlcException {
		Date dataConferencia = new Date();
		boolean autorizaGravacao = true;
		int totalItensQuantidadeDivergente=0;
		int totalItensLocalizacaoDivergente=0;

		mensagens.clear();
		alertas.clear();
		
		try {
			carregaConfiguracao(context);
			List<ItemConferencia> itensConferencia = conferencia.getItemConferencia();
			
			for (ItemConferencia itemConferencia : itensConferencia) {
				Livro livro = itemConferencia.getLivro();
				List<Estoque> estoqueList = dao.findByFields(context, EstoqueEntity.class, "querySelByLivro", new String[] {"livro"}, new Object[] {livro});
				
				if (estoqueList == null || estoqueList.size() == 0) {
					//TODO: 
				} else if (estoqueList.size() == 1) {
					Estoque itemEstoque = estoqueList.get(0);
					
					if (itemConferencia.getLocalizacao().getId().compareTo(itemEstoque.getLocalizacao().getId())!=0) {
						
						if (PlcYesNo.S.equals(config.getAlertaTrocaLocalizacaoLivros())) {
							
							if (config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.D) ) {
								alertas.add(String.format("O livro '%s' teve sua localização trocada de '%s' para '%s'", 
										new Object[]{itemConferencia.getLivro().getTitulo(),
										itemEstoque.getLocalizacao().getDescricao(),
										itemConferencia.getLocalizacao().getDescricao()}));
							}
							
						}
						
						if (PlcYesNo.N.equals(config.getPermiteTrocaLocalizacaoLivros())) {

							if (config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.D) ) {
								alertas.add("A troca da localização não está permitida nas configurações do sistema!");
							}
							
							autorizaGravacao = false;
						}
						
						totalItensLocalizacaoDivergente++;
					}

					if (itemConferencia.getQuantidade().compareTo(itemEstoque.getQuantidade())!=0) {

						if (config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.D) ) {
							alertas.add(String.format("Quantidade divergente para o livro '%s'. Registrado: '%d'. Informado: '%d'", 
									new Object[]{itemConferencia.getLivro().getTitulo(),
									itemEstoque.getQuantidade(),
									itemConferencia.getQuantidade()}));
						}
						
						totalItensQuantidadeDivergente++;
					}
				}
			}

			if (!config.getTipoMensagem() .equals(TipoMensagemConferenciaConfig.B) ) {
				mensagens.add(String.format("Total de itens com divergência na localização: %d", new Object[]{totalItensLocalizacaoDivergente})); 
				mensagens.add(String.format("Total de itens com divergência na quantidade: %d", new Object[]{totalItensQuantidadeDivergente}));
			}

			if (autorizaGravacao) {

				mensagens.add("     "); 
				if (totalItensQuantidadeDivergente == 0) {
					conferencia.setResultado(ResultadoConferencia.S);
					mensagens.add("Conferência gravada com sucesso sem divergência!"); 
				} else {
					conferencia.setResultado(ResultadoConferencia.D);
					mensagens.add("Conferência gravada com sucesso com divergência!"); 
				}
				
				conferencia.setStatus(StatusConferencia.C);
				conferencia.setDataUltAlteracao(dataConferencia);
				conferencia.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
				dao.update(context, conferencia);

			} else {
				alertas.add("Não está autorizado a conclusão da conferência com divergência de localização!"); 
				alertas.add("Favor gravar a conferência e depois alterar a configuração do sistema caso necessário!"); 
			}
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("ConferenciaRepository", "concluirConferencia", e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}
	
	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
		List listaConfig = (List)dao.findAll(context, ConferenciaConfigEntity.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{conferencia.err.configuracao.inexistente}");
		}
		
		config = (ConferenciaConfig)listaConfig.get(0);
	}
	
}
