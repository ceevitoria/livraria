package com.cee.livraria.controller.jsf.produto;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.cee.livraria.entity.produto.Produto;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("produto")
public class ProdutoGridController<E, I> extends PlcBaseGridController<E, I> {

	@Inject @QPlcDefault 
	protected PlcIocControllerFacadeUtil iocControleFacadeUtil;

	private HttpServletRequest request;

	private Integer page;

	private Integer rows;

	private String orderBy;

	private String order;

	private Long total;

	public HttpServletRequest getRequest() {
		return request;
	}

	public Integer getPage() {
		return page;
	}

	public Integer getRows() {
		return rows;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public String getOrder() {
		return order;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Inject
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	@Inject
	public void setPage(@QueryParam("page") @DefaultValue("1") Integer page) {
		this.page = page;
	}

	@Inject
	public void setRows(@QueryParam("rows") @DefaultValue("90") Integer rows) {
		this.rows = rows;
	}

	@Inject
	public void setOrderBy(@QueryParam("sidx") String orderBy) {
		this.orderBy = orderBy;
	}

	@Inject
	public void setOrder(@QueryParam("sord") String order) {
		this.order = order;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public void retrieveCollection() {

		try {
			Object instancia = getEntityType().newInstance();
			
			if (instancia instanceof Produto) {
				Produto produtoArg = (Produto) instancia;
				
				PlcConfigSelection configSelecao = getConfigCollaboration().selection();
				String querySel = null;
				
				if (configSelecao != null) {
					querySel = configSelecao.apiQuerySel();
				}

				PlcBaseContextVO context = getContext();

				context.setApiQuerySel(StringUtils.isNotBlank(querySel) ? querySel : null);

				String orderByDinamico = null;
				if (StringUtils.isNotEmpty(orderBy)) {
					orderByDinamico = orderBy + " " + StringUtils.defaultIfEmpty(order, "asc");
				}
				
				this.beanPopulateUtil.transferMapToBean(request.getParameterMap(), produtoArg);
				this.retrieveCollectionBefore();
				
				this.setTotal(getFacade().findCount(getContext(), produtoArg));
				List<E> lista = null;
				
//				if (produtoArg.getLocalizacao() == null) {
					lista = (List<E>) iocControleFacadeUtil.getFacade(IAppFacade.class).recuperarProdutos(getContext(), produtoArg, orderByDinamico, ((page - 1) * rows), (rows));
//				} else {
//					lista = (List<E>) iocControleFacadeUtil.getFacade(IAppFacade.class).buscarProdutosEstoquePorLocalizacao(getContext(), produtoArg, produtoArg.getLocalizacao()); 					
//				}
				
				this.setEntityCollection(lista);
				this.retrieveCollectionAfter();
			}
			
		} catch (Exception e) {
			handleExceptions(e);
		}
	}

}
