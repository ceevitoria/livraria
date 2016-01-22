package com.cee.livraria.model.relatorio;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cee.livraria.entity.caixa.CaixaFormaPagto;
import com.cee.livraria.entity.caixa.CaixaFormaPagtoEntity;
import com.cee.livraria.entity.relatorio.RelatorioFechamentoCaixa;
import com.cee.livraria.persistence.jpa.relatorio.RelatorioFechamentoCaixaDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

/**
 * Classe de Modelo gerada pelo assistente
 */

@SPlcRepository
@PlcAggregationIoC(clazz = RelatorioFechamentoCaixa.class)
public class RelatorioFechamentoCaixaRepository extends PlcBaseRepository {

	@Inject
	private Logger log;
	
	private interface COL {
		int DATA_MOVIMENTO		= 0;
		int TIPO_MOVIMENTO		= 1;
		int VALOR_MOVIMENTO		= 2;
		int SALDO_INICIAL		= 3;
		int SALDO_FINAL			= 4;
		int OBSERVACAO			= 5;
	}
	
	@Inject
	RelatorioFechamentoCaixaDAO relatorioDAO;

	@SuppressWarnings({ "rawtypes"})
	public Collection recuperaDadosFechamentoCaixa(PlcBaseContextVO context, String orderByDinamico, Integer inicio, Integer total) throws PlcException {
		RelatorioFechamentoCaixa movimentoAbertura = relatorioDAO.obterMovimentoAberturaCaixa(context);

		if (movimentoAbertura == null) {
			throw new PlcException("{atencao.relatorio.fechamentoCaixa.caixa.fechado}");
		}
		
		// Recupera todos os projetos conforme filtro e paginação definida na tela de seleção
		List<RelatorioFechamentoCaixa> dadosFechamentoCaixa = relatorioDAO.findList(context, orderByDinamico, inicio, total, movimentoAbertura.getData());

		if (dadosFechamentoCaixa.size() > 0) {
			Collections.reverse(dadosFechamentoCaixa);
			BigDecimal saldoFinal = dadosFechamentoCaixa.get(0).getCaixa().getSaldo();
			
			for (RelatorioFechamentoCaixa movimentoCaixa : dadosFechamentoCaixa) {
				saldoFinal = saldoFinal.subtract(movimentoCaixa.getValor());
				movimentoCaixa.setSaldo(saldoFinal);				
			}
			
			Collections.reverse(dadosFechamentoCaixa);
		} else {
			throw new PlcException("{atencao.relatorio.fechamentoCaixa.semMovimentos}");
		}
		
		return dadosFechamentoCaixa;
	}

	private List<CaixaFormaPagto> recuperaDadosFormaPagamento(PlcBaseContextVO context) throws PlcException {
		// Recupera todos os projetos conforme filtro e paginação definida na tela de seleção
		List<CaixaFormaPagto> dadosFormaPagamento = relatorioDAO.findAll(context, CaixaFormaPagtoEntity.class, null);
		
		return dadosFormaPagamento ;
	}
	
	@SuppressWarnings("unchecked")
	public byte[] gerarRelatorioFechamentoCaixa(PlcBaseContextVO context, String sheetName) throws PlcException  {
		List<RelatorioFechamentoCaixa> dadosRelatorioMovimento = (List<RelatorioFechamentoCaixa>) recuperaDadosFechamentoCaixa(context, null, null, null);
		
		XSSFWorkbook wb = new XSSFWorkbook();
		Map<String, XSSFCellStyle> styles = createStyles(wb);
		
		XSSFSheet sheet = wb.createSheet("Relatório Fechamento Caixa"); 
		int linhaExcel = 0;

		XSSFRow row = sheet.createRow(linhaExcel++);
		
		configuraCabecalho(styles, sheet, row);
		
        CreationHelper factory = wb.getCreationHelper();
        
		for (RelatorioFechamentoCaixa dadoMovimento: dadosRelatorioMovimento) {
			row = sheet.createRow(linhaExcel++);
			populaDadosRelatorioFechamentoCaixa(dadoMovimento, factory, wb, sheet, row, styles);
		}		
		
		sheet.createRow(linhaExcel++);
		sheet.createRow(linhaExcel++);
		row = sheet.createRow(linhaExcel++);

		XSSFCell cell;
		cell = row.createCell(0);
		cell.setCellValue("Formas de Pagamento");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(0, 256 * 20);
		sheet.addMergedRegion(new CellRangeAddress(cell.getRowIndex(),cell.getRowIndex(),cell.getColumnIndex(),cell.getColumnIndex()+1));
		
		List<CaixaFormaPagto> formasPagamento = recuperaDadosFormaPagamento(context);
		BigDecimal totalPagamentos = new BigDecimal("0.00");
		
		for (CaixaFormaPagto formaPagamento : formasPagamento) {
			row = sheet.createRow(linhaExcel++);
			
			cell = row.createCell(0);
			
			if (formaPagamento.getFormaPagto() != null && formaPagamento.getFormaPagto().getNome() != null) {
				cell.setCellValue(formaPagamento.getFormaPagto().getNome());
			}
			
			cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
			
			if (formaPagamento.getValor() != null) {
				cell.setCellStyle(styles.get("valor"));
				cell.setCellValue(formaPagamento.getValor().doubleValue());
				totalPagamentos = totalPagamentos.add(formaPagamento.getValor());
			}
		}

		row = sheet.createRow(linhaExcel++);
		
		cell = row.createCell(0);
		cell.setCellStyle(styles.get("total"));
		cell.setCellValue("Total");
		
		cell = row.createCell(1, Cell.CELL_TYPE_NUMERIC);
		
		if (totalPagamentos != null) {
			cell.setCellValue(totalPagamentos.doubleValue());
			cell.setCellStyle(styles.get("valorTotal"));
		}
		
		try {
			ByteArrayOutputStream  fileOut = new ByteArrayOutputStream ();
			wb.write(fileOut);
			fileOut.flush();
			fileOut.close();
			wb.close();
			
			return fileOut.toByteArray();
		} catch (PlcException plcE) {
			throw plcE;
		} catch (Exception e) {
			throw new PlcException("RelatorioFechamentoCaixaRepository", "gerarRelatorioRelatorioFechamentoCaixa", e, log, "");
		}

	}

	private void populaDadosRelatorioFechamentoCaixa(RelatorioFechamentoCaixa dadosMovimento, CreationHelper factory, XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row, Map<String, XSSFCellStyle> styles) {
		XSSFCell cell;
		
		cell = row.createCell(COL.DATA_MOVIMENTO);
		
		if (dadosMovimento.getData() != null) {
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(factory.createDataFormat().getFormat("dd/mm/yyyy HH:mm:ss"));
			style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			cell.setCellStyle(style);
			cell.setCellValue(dadosMovimento.getData());
		}

		cell = row.createCell(COL.TIPO_MOVIMENTO);
		cell.setCellValue(dadosMovimento.getTipo().getName());

		
		cell = row.createCell(COL.VALOR_MOVIMENTO, Cell.CELL_TYPE_NUMERIC);
		
		if (dadosMovimento.getValor() != null) {
			cell.setCellStyle(styles.get("valor"));
			cell.setCellValue(dadosMovimento.getValor().doubleValue());
		}
		
		cell = row.createCell(COL.SALDO_INICIAL, Cell.CELL_TYPE_NUMERIC);
		
		if (dadosMovimento.getSaldo() != null) {
			cell.setCellStyle(styles.get("valor"));
			cell.setCellValue(dadosMovimento.getSaldo().doubleValue());
		}
		

		cell = row.createCell(COL.SALDO_FINAL, Cell.CELL_TYPE_NUMERIC);
		
		if (dadosMovimento.getValor() != null) {
			cell.setCellStyle(styles.get("valor"));
			cell.setCellValue(dadosMovimento.getValor().doubleValue() + dadosMovimento.getSaldo().doubleValue());
		}

		cell = row.createCell(COL.OBSERVACAO);
		
		if (dadosMovimento.getObservacao() != null) {
			cell.setCellValue(dadosMovimento.getObservacao());
		}

	}

	private void configuraCabecalho(Map<String, XSSFCellStyle> styles, XSSFSheet sheet, XSSFRow row) {
		XSSFCell cell;
		cell = row.createCell(COL.DATA_MOVIMENTO);
		cell.setCellValue("Data Hora");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.DATA_MOVIMENTO, 256 * 20);

		cell = row.createCell(COL.TIPO_MOVIMENTO);
		cell.setCellValue("Tipo Movimento");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.TIPO_MOVIMENTO, 256 * 16);

		cell = row.createCell(COL.VALOR_MOVIMENTO);
		cell.setCellValue("Valor Movimento");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.VALOR_MOVIMENTO, 256 * 18);

		cell = row.createCell(COL.SALDO_INICIAL);
		cell.setCellValue("Saldo Inicial");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.SALDO_INICIAL, 256 * 15);

		cell = row.createCell(COL.SALDO_FINAL);
		cell.setCellValue("Saldo Final");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.SALDO_FINAL, 256 * 15);

		cell = row.createCell(COL.OBSERVACAO);
		cell.setCellValue("Observação");
		cell.setCellStyle(styles.get("title"));
		sheet.setColumnWidth(COL.OBSERVACAO, 256 * 80);
	}
	
	 /**
     * cell styles used for formatting calendar sheets
     */
    private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb){
        Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();

        XSSFCellStyle style;
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styles.put("title", style);

        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("total", style);

        style = wb.createCellStyle();
        style.setDataFormat((short)8);  //8 = "($#,##0.00_);[Red]($#,##0.00)"
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        styles.put("valor", style);
        
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style = wb.createCellStyle();
        style.setDataFormat((short)8);  //8 = "($#,##0.00_);[Red]($#,##0.00)"
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("valorTotal", style);
        
        return styles;
    }	

}
