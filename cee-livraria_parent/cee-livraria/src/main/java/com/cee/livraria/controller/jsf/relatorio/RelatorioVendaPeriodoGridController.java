package com.cee.livraria.controller.jsf.relatorio;

import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

import org.apache.commons.lang.StringUtils;

import com.cee.livraria.entity.relatorio.RelatorioVendaPeriodo;
import com.cee.livraria.facade.IAppFacade;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.config.qualifiers.QPlcDefault;
import com.powerlogic.jcompany.config.collaboration.PlcConfigSelection;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerName;
import com.powerlogic.jcompany.controller.rest.api.qualifiers.QPlcControllerQualifier;
import com.powerlogic.jcompany.controller.rest.api.stereotypes.SPlcController;
import com.powerlogic.jcompany.controller.rest.controllers.PlcBaseGridController;
import com.powerlogic.jcompany.controller.util.PlcIocControllerFacadeUtil;

@SPlcController
@QPlcControllerName("grid")
@QPlcControllerQualifier("relatoriovendaperiodocon")
public class RelatorioVendaPeriodoGridController<E, I> extends PlcBaseGridController<E, I> {

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
			
			RelatorioVendaPeriodo relatorioArg = (RelatorioVendaPeriodo) instancia;
			
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
			
			this.beanPopulateUtil.transferMapToBean(request.getParameterMap(), relatorioArg);
			this.retrieveCollectionBefore();
			
			if (relatorioArg.getDataFim() == null || relatorioArg.getDataInicio() == null) {
				throw new PlcException("{relatorio.vendaPeriodo.erro.peridoInvalido}");
			}
			
			if (relatorioArg.getDataFim().before(relatorioArg.getDataInicio())) {
				throw new PlcException("{relatorio.vendaPeriodo.erro.dataFimMenorDataInicio}");
			}
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(relatorioArg.getDataFim());
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.SECOND, -1); // Obtem o último horário do período final. Ex: 23:59:59
			relatorioArg.setDataFim(cal.getTime());
			
//			this.setTotal(getFacade().findCount(context, relatorioArg)); 
			List<E> lista = (List<E>) iocControleFacadeUtil.getFacade(IAppFacade.class).recuperaVendaPeriodo(context, relatorioArg, orderByDinamico, ((page - 1) * rows), (rows));
			this.setTotal((long)lista.size()); 
			this.setEntityCollection(lista);
			this.retrieveCollectionAfter();
		} catch (Exception e) {
			handleExceptions(e);
		}
	}

}
