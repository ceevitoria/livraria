package com.cee.livraria.model.ajuste;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.cee.livraria.entity.config.AjusteEstoqueConfig;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.estoque.Estoque;
import com.cee.livraria.entity.estoque.ItemMovimento;
import com.cee.livraria.entity.estoque.ItemMovimentoEntity;
import com.cee.livraria.entity.estoque.ModoMovimento;
import com.cee.livraria.entity.estoque.Movimento;
import com.cee.livraria.entity.estoque.MovimentoEntity;
import com.cee.livraria.entity.estoque.TipoMovimento;
import com.cee.livraria.entity.estoque.ajuste.AjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.ItemAjusteEstoque;
import com.cee.livraria.entity.estoque.ajuste.StatusAjuste;
import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.persistence.jpa.ajuste.AjusteEstoqueDAO;
import com.cee.livraria.persistence.jpa.produto.ProdutoDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

/**
 * Classe de Modelo gerada pelo assistente
 */
 
@SPlcRepository 
@PlcAggregationIoC(clazz=AjusteEstoque.class)
public class AjusteEstoqueRepository extends PlcBaseRepository {

	@Inject
	private AjusteEstoqueDAO dao;
	
	@Inject
	private ProdutoDAO produtoDAO;

	@Inject
	protected transient Logger log;

	private AjusteEstoqueConfig config;

	private List<String> alertas = new ArrayList<String>();
	private List<String> mensagens = new ArrayList<String>();

	public RetornoConfig concluirAjusteEstoque(PlcBaseContextVO context, AjusteEstoque ajusteEstoque) throws PlcException {
		Date dataAjusteEstoque = new Date();
		mensagens.clear();
		alertas.clear();
		
		try {
			carregaConfiguracao(context);

			registraMovimento(context, ajusteEstoque, dataAjusteEstoque);
			atualizaEstoque(context, ajusteEstoque, dataAjusteEstoque);
			
			mensagens.add("Ajuste de Estoque realizado com sucesso!"); 
			
			ajusteEstoque.setStatusAjuste(StatusAjuste.C);
			ajusteEstoque.setDataUltAlteracao(dataAjusteEstoque);
			ajusteEstoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			dao.update(context, ajusteEstoque);
			
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("AjusteEstoqueRepository", "concluirAjusteEstoque", e, log, "");
		}
		
		return new RetornoConfig(null, config, alertas, mensagens);
	}
	
	@SuppressWarnings("rawtypes")
	private void carregaConfiguracao(PlcBaseContextVO context) throws PlcException {
		List listaConfig = (List)dao.findAll(context, AjusteEstoqueConfig.class, null);
		
		if (listaConfig == null || listaConfig.size() != 1) {
			throw new PlcException("{ajusteEstoque.err.configuracao.inexistente}");
		}
		
		config = (AjusteEstoqueConfig)listaConfig.get(0);
	}
	
	/**
	 * Registra o movimento de ajuste de estoque para os produtos informados
	 * @param relacaoProdutos Relação dos produtos sendo vendidos
	 */
	private void registraMovimento(PlcBaseContextVO context, AjusteEstoque ajusteEstoque, Date dataMovimento) throws PlcException {
		Movimento mov = new MovimentoEntity();
		List<ItemMovimento> itens = new ArrayList<ItemMovimento>();
		double valorMovimento = 0.0;
		int qtdEntrada = 0;
		int qtdSaida = 0;
		
		List<ItemAjusteEstoque> itensAjusteEstoque = ajusteEstoque.getItemAjusteEstoque();
		
		for (ItemAjusteEstoque itemAjuste : itensAjusteEstoque) {
			
			// Evita os itens em branco
			if (itemAjuste.getProduto() != null && itemAjuste.getProduto().getId() != null) {
				
				if (itemAjuste.getQuantidadeInformada() != null) {
					
					if (itemAjuste.getQuantidadeEstoque() == null) {
						insereEstoque(context, itemAjuste, dataMovimento);
						itemAjuste.setQuantidadeEstoque(itemAjuste.getQuantidadeInformada());
					}
					
					int qtdExistente = itemAjuste.getQuantidadeEstoque();
					int qtdInformada = itemAjuste.getQuantidadeInformada();
					
					int qtdSaldo = 0;
					int sinal = 1;
					
					TipoMovimento tipoMovimento = null;
					
					if (qtdInformada > qtdExistente) {
						tipoMovimento = TipoMovimento.E;
						qtdSaldo = qtdInformada - qtdExistente;
						sinal = 1;
						qtdEntrada++;
					}
					
					if (qtdInformada < qtdExistente) {
						tipoMovimento = TipoMovimento.S;
						qtdSaldo = qtdExistente - qtdInformada;
						sinal = -1;
						qtdSaida++;
					}
					
					if (qtdSaldo != 0) {
						ItemMovimento itemMovimento = new ItemMovimentoEntity();
						
						Produto produto = (Produto)dao.findById(context, Produto.class, itemAjuste.getProduto().getId());
						
						itemMovimento.setProduto(produto);
						itemMovimento.setTipo(tipoMovimento);
						itemMovimento.setQuantidade(qtdSaldo);
						itemMovimento.setValorUnitario(produto.getPrecoUltCompra());
						itemMovimento.setValorTotal(produto.getPrecoUltCompra().multiply(new BigDecimal(qtdSaldo)));
						itemMovimento.setMovimento(mov);
						
						itemMovimento.setDataUltAlteracao(dataMovimento);
						itemMovimento.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
						
						itens.add(itemMovimento);
						
						valorMovimento += (produto.getPrecoUltCompra().doubleValue() * qtdSaldo * sinal);
					}
				}
			}
		}

		TipoMovimento tipoMovimento = null;
		
		if (qtdEntrada > 0 && qtdSaida > 0) {
			tipoMovimento = TipoMovimento.A;
		} else if (qtdEntrada > 0) {
			tipoMovimento = TipoMovimento.E;
		} else if (qtdSaida > 0) {
			tipoMovimento = TipoMovimento.S;
		}
		
		if (tipoMovimento != null) {
			mov.setData(dataMovimento);
			mov.setTipo(tipoMovimento);
			mov.setModo(ModoMovimento.A);
			
			// Arredonda o valor em duas casa decimais
			valorMovimento *= 100;
			valorMovimento = Math.round(valorMovimento);
			valorMovimento /= 100;
			
			mov.setValor(BigDecimal.valueOf(valorMovimento));
			mov.setItemMovimento(itens);
			
			mov.setDataUltAlteracao(dataMovimento);
			mov.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
			
			dao.insert(context, mov);
		}
	}
	
	private void insereEstoque(PlcBaseContextVO context, ItemAjusteEstoque itemAjuste, Date data)  throws PlcException {
		Estoque estoque = new Estoque();
		
		estoque.setQuantidadeMinima(1);
		estoque.setQuantidade(itemAjuste.getQuantidadeInformada());
		estoque.setQuantidadeMaxima(20);
		estoque.setDataConferencia(data);
		
		estoque.setDataUltAlteracao(data);
		estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
		
		dao.insert(context, estoque);
	}

	/**
	 * Atualiza o saldo dos produtos sendo vendidos no estoque
	 * OBS: Não faz crítica para saldo negativo.
	 * @param relacaoProdutos Relação dos produtos sendo vendidos
	 * @param dataVenda Data em que a venda foi realizada
	 * @return quantidade vendida de produtos
	 * @throws PlcException
	 */
	private void atualizaEstoque(PlcBaseContextVO context, AjusteEstoque ajusteEstoque, Date dataAjuste) throws PlcException {
		List<ItemAjusteEstoque> itensAjusteEstoque = ajusteEstoque.getItemAjusteEstoque();
		
		for (ItemAjusteEstoque itemAjuste : itensAjusteEstoque) {

			if (itemAjuste.getProduto() != null && itemAjuste.getProduto().getId() != null) {
				int qtdSaldo = 0;
				
				if (itemAjuste.getQuantidadeInformada() != null) {
					
					int qtdInformada = itemAjuste.getQuantidadeInformada();
					int qtdExistente = itemAjuste.getQuantidadeEstoque();
					
					if (qtdInformada > qtdExistente) {
						qtdSaldo = qtdInformada - qtdExistente;
					}
					
					if (qtdInformada < qtdExistente) {
						qtdSaldo = qtdExistente - qtdInformada;
					}
				}
				
				if (qtdSaldo != 0 || (itemAjuste.getLocalizacao() != null && itemAjuste.getLocalizacao().getId() != null)) {
					boolean precisaAtualizaProduto = false;
					Produto produto = (Produto)dao.findById(context, Produto.class, itemAjuste.getProduto().getId());
//					List<Estoque> estoqueList = dao.findByFields(context, Estoque.class, "querySelByProduto", new String[] {"produto"}, new Object[] {produto});
					
					if (produto != null && produto.getEstoque() != null) {
						Estoque estoque = produtoDAO.obterEstoqueProduto(context, produto);
						
						if (itemAjuste.getQuantidadeInformada() != null) {
							estoque.setQuantidade(itemAjuste.getQuantidadeInformada());
						}
						
						estoque.setDataConferencia(dataAjuste);
						estoque.setDataUltAlteracao(dataAjuste);
						estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
	
						dao.update(context, estoque);
						
					} else if (produto != null && produto.getEstoque() == null) {
						Estoque estoque = new Estoque();
						
						estoque.setQuantidade(itemAjuste.getQuantidadeInformada());
						estoque.setQuantidadeMinima(1);
						estoque.setQuantidadeMaxima(50);
						estoque.setDataConferencia(dataAjuste);
						estoque.setDataUltAlteracao(dataAjuste);
						estoque.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
	
						dao.insert(context, estoque);
						
						produto.setEstoque(estoque);
						precisaAtualizaProduto = true;
					}
					
					if (itemAjuste.getLocalizacao() != null && produto.getLocalizacao() == null) {
						produto.setLocalizacao(itemAjuste.getLocalizacao());
						precisaAtualizaProduto = true;
					} else if (itemAjuste.getLocalizacao() != null && produto.getLocalizacao().getId().compareTo(itemAjuste.getLocalizacao().getId()) != 0) {
						produto.setLocalizacao(itemAjuste.getLocalizacao());
						precisaAtualizaProduto = true;
					}

					if (precisaAtualizaProduto) {
						produto.setDataUltAlteracao(dataAjuste);
						produto.setUsuarioUltAlteracao(context.getUserProfile().getLogin());
						dao.update(context, produto);
					}
				}
			}
		}
	}
	
}
