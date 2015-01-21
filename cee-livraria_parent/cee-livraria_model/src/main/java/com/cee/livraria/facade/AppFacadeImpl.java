package com.cee.livraria.facade;

import java.math.BigDecimal;
import java.util.List;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import com.cee.livraria.entity.caixa.Caixa;
import com.cee.livraria.entity.caixa.CaixaEntity;
import com.cee.livraria.entity.caixa.TipoMovimentoCaixa;
import com.cee.livraria.entity.config.RetornoConfig;
import com.cee.livraria.entity.pagamento.PagamentoList;
import com.cee.livraria.entity.tabpreco.apoio.PrecoTabela;
import com.cee.livraria.model.CaixaRepository;
import com.cee.livraria.model.VendaLivroRepository;
import com.cee.livraria.persistence.jpa.livro.LivroDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcFacade;
import com.powerlogic.jcompany.facade.PlcFacadeImpl;
import com.powerlogic.jcompany.model.annotation.PlcTransactional;

@QPlcDefault
@SPlcFacade
public class AppFacadeImpl extends PlcFacadeImpl implements IAppFacade {

	@Inject
	private LivroDAO jpaDAO;
	
	@Inject
	private VendaLivroRepository vendaLivroRepository;
	
	@Inject
	private CaixaRepository caixaRepository;
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public PrecoTabela findPrecoTabela(PlcBaseContextVO context, Long idLivro) throws PlcException {
		return jpaDAO.obterPrecoTabela(context, idLivro);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarVendaLivros(PlcBaseContextVO context, List entityList, List pagtoList) throws PlcException {
		return vendaLivroRepository.registrarVendaLivros(context, entityList, pagtoList);
	}
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public BigDecimal buscarDadosVendaLivros(PlcBaseContextVO context, List entityList) throws PlcException {
		return vendaLivroRepository.buscarDadosVendaLivros(context, entityList);
	}

	@PlcTransactional(commit=true)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.REQUIRED)
	@Override
	public RetornoConfig registrarOperacaoCaixa(PlcBaseContextVO context, TipoMovimentoCaixa tipo,  CaixaEntity caixa) throws PlcException {
		return caixaRepository.registrarOperacaoCaixa(context, tipo, caixa);
	}
	
	@PlcTransactional(commit=false)
	@TransactionAttribute(javax.ejb.TransactionAttributeType.NOT_SUPPORTED)
	@Override
	public PagamentoList obterPagamentosCaixa(PlcBaseContextVO context, Caixa caixa) throws PlcException {
		return caixaRepository.obterPagamentosCaixa(context, caixa);
	}
	
}
