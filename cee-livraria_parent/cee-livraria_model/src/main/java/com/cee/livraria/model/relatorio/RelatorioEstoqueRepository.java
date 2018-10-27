package com.cee.livraria.model.relatorio;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cee.livraria.entity.relatorio.RelatorioEstoque;
import com.cee.livraria.persistence.jpa.relatorio.RelatorioEstoqueDAO;
import com.powerlogic.jcompany.commons.PlcBaseContextVO;
import com.powerlogic.jcompany.commons.PlcException;
import com.powerlogic.jcompany.commons.annotation.PlcAggregationIoC;
import com.powerlogic.jcompany.commons.config.stereotypes.SPlcRepository;
import com.powerlogic.jcompany.model.PlcBaseRepository;

/**
 * Classe de Modelo gerada pelo assistente
 */

@SPlcRepository
@PlcAggregationIoC(clazz = RelatorioEstoque.class)
public class RelatorioEstoqueRepository extends PlcBaseRepository {

	@Inject
	private Logger log;
	
	private interface COL {
		int CODIGO_BARRAS		= 0;
		int TITULO				= 1;
		int TIPO_PRODUTO		= 2;
		int DATA_ULT_COMPRA		= 3;
		int QUANTIDADE			= 4;
		int PRECO_ULT_COMPRA	= 5;
		int PRECO_VENDA			= 6;
	}
	
	@Inject
	RelatorioEstoqueDAO relatorioDAO;

	@SuppressWarnings({ "rawtypes" })
	public Collection recuperaDadosRelatorio(PlcBaseContextVO context, RelatorioEstoque relatorioArg, String orderByDinamico, Integer inicio, Integer total) throws PlcException {
		// Recupera todos os projetos conforme filtro e paginação definida na tela de seleção
		List<RelatorioEstoque> relatorio = relatorioDAO.findList(context, orderByDinamico, inicio, total);

		return relatorio;
	}

	@SuppressWarnings("unchecked")
	public byte[] gerarRelatorioEstoque(PlcBaseContextVO context, RelatorioEstoque relatorioArg, String sheetName) throws PlcException  {
		Integer quantidadeCompraTotal = 0;
		Integer quantidadeAtualTotal = 0;
		BigDecimal valorTotalUltCompra = new BigDecimal("0.00");
		BigDecimal valorTotalVenda = new BigDecimal("0.00");
		XSSFWorkbook wb = new XSSFWorkbook();
		
		List<RelatorioEstoque> relatorio = (List<RelatorioEstoque>) recuperaDadosRelatorio(context, relatorioArg, null, null, null);
		CreationHelper factory = wb.getCreationHelper();
		
		Map<String, XSSFCellStyle> styles = createStyles(wb, factory);

		Date dataRel = relatorioArg.getDataRelatorio();
		String tituloAba = String.format("Estoque-%tY%tm%td", dataRel, dataRel, dataRel, dataRel);
		
		XSSFSheet sheet = wb.createSheet(tituloAba); 
		sheet.setMargin(Sheet.TopMargin, 0.3 /* inches */ );
		sheet.setMargin(Sheet.BottomMargin, 0.3 /* inches */ );
		sheet.setMargin(Sheet.LeftMargin, 0.2 /* inches */ );
		sheet.setMargin(Sheet.RightMargin, 0.2 /* inches */ );
		
		configuraTitulo(styles, factory, sheet, relatorioArg);
		
		int linhaExcel = 1;
		XSSFRow row = sheet.createRow(linhaExcel++);
		row = sheet.createRow(linhaExcel++);
		
		configuraCabecalho(styles, sheet, row);
		
		for (RelatorioEstoque linha: relatorio) {
			row = sheet.createRow(linhaExcel++);
			populaDados(linha, styles, factory, wb, sheet, row);
		
			quantidadeAtualTotal += linha.getEstoque().getQuantidade();
			valorTotalUltCompra = valorTotalUltCompra.add(linha.getPrecoUltCompra() != null ? linha.getPrecoUltCompra() : new BigDecimal("0.0"));
			valorTotalVenda = valorTotalUltCompra.add(linha.getPrecoVendaSugerido());
		}		

		row = sheet.createRow(linhaExcel++);
		configuraRodape(styles, sheet, row, quantidadeCompraTotal, quantidadeAtualTotal, valorTotalUltCompra, valorTotalVenda);
		
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
			throw new PlcException("RelatorioEstoqueRepository", "gerarRelatorioEstoque", e, log, "");
		}

	}

	private void configuraRodape(Map<String, XSSFCellStyle> styles, XSSFSheet sheet, XSSFRow row, Integer quantidadeCompraTotal, Integer quantidadeAtualTotal, BigDecimal valorTotalUltCompra, BigDecimal valorTotalVenda) {
		XSSFCell cell;
		
		cell = row.createCell(COL.QUANTIDADE-1);
		cell.setCellValue("TOTAIS");
		cell.setCellStyle(styles.get("rodape"));
		
		cell = row.createCell(COL.QUANTIDADE);
		cell.setCellValue(quantidadeCompraTotal);
		cell.setCellStyle(styles.get("rodapeQuantidade"));

		cell = row.createCell(COL.QUANTIDADE);
		cell.setCellValue(quantidadeAtualTotal);
		cell.setCellStyle(styles.get("rodapeQuantidade"));
		
		cell = row.createCell(COL.PRECO_ULT_COMPRA);
		cell.setCellValue(valorTotalUltCompra.doubleValue());
		cell.setCellStyle(styles.get("rodapeValor"));

		cell = row.createCell(COL.PRECO_VENDA);
		cell.setCellValue(valorTotalVenda.doubleValue());
		cell.setCellStyle(styles.get("rodapeValor"));
	}

	private void configuraTitulo(Map<String, XSSFCellStyle> styles, CreationHelper factory, XSSFSheet sheet, RelatorioEstoque relatorioArg) {
		XSSFRow row;
		XSSFCell cell;
		
		Date dataRel = relatorioArg.getDataRelatorio();
		String titulo = String.format("RELATÓRIO DO ESTOQUE em %td/%tm/%tY", dataRel, dataRel, dataRel, dataRel);
		
		row = sheet.createRow(0);
		cell = row.createCell(0);
		cell.setCellValue(titulo);
		
		CellRangeAddress region = new CellRangeAddress(0, 0, 0, 6);
		cleanBeforeMergeOnValidCells(row.getSheet(), region, styles.get("titulo"));
		row.getSheet().addMergedRegion(region);// merging cells that has a title name		
	}

	private void populaDados(RelatorioEstoque relatorio, Map<String, XSSFCellStyle> styles, CreationHelper factory, XSSFWorkbook wb, XSSFSheet sheet, XSSFRow row) {
		XSSFCell cell;
		
		cell = row.createCell(COL.CODIGO_BARRAS);
		cell.setCellValue(relatorio.getCodigoBarras());
		cell.setCellStyle(styles.get("centeredTextStyle"));
		cell.setCellType(Cell.CELL_TYPE_STRING);  

		cell = row.createCell(COL.TITULO);
		cell.setCellValue(relatorio.getTitulo());

		cell = row.createCell(COL.TIPO_PRODUTO);
		cell.setCellValue(relatorio.getTipoProduto().getName());
		cell.setCellStyle(styles.get("centeredTextStyle"));

		cell = row.createCell(COL.DATA_ULT_COMPRA);
		cell.setCellValue(relatorio.getNotaFiscal().getDataEmissao());
		cell.setCellStyle(styles.get("dateStyle"));
		
		cell = row.createCell(COL.QUANTIDADE);
		cell.setCellValue(relatorio.getEstoque().getQuantidade());
		
		cell = row.createCell(COL.PRECO_ULT_COMPRA);
		cell.setCellValue(relatorio.getPrecoUltCompra() != null ? relatorio.getPrecoUltCompra().doubleValue() : 0.0);
		cell.setCellStyle(styles.get("valorMonetario"));

		cell = row.createCell(COL.PRECO_VENDA);
		cell.setCellValue(relatorio.getPrecoVendaSugerido() != null ? relatorio.getPrecoVendaSugerido().doubleValue() : 0.0);
		cell.setCellStyle(styles.get("valorMonetario"));
	}

	private void configuraCabecalho(Map<String, XSSFCellStyle> styles, XSSFSheet sheet, XSSFRow row) {
		XSSFCell cell;
		cell = row.createCell(COL.CODIGO_BARRAS);
		cell.setCellValue("Código");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.CODIGO_BARRAS, 256 * 17);

		cell = row.createCell(COL.TITULO);
		cell.setCellValue("Título");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.TITULO, 256 * 70);

		cell = row.createCell(COL.TIPO_PRODUTO);
		cell.setCellValue("Tipo");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.TIPO_PRODUTO, 256 * 10);
		
		cell = row.createCell(COL.DATA_ULT_COMPRA);
		cell.setCellValue("Data Ult Compra");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.DATA_ULT_COMPRA, 256 * 16);
		
		cell = row.createCell(COL.QUANTIDADE);
		cell.setCellValue("Quantidade");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.QUANTIDADE, 256 * 13);

		cell = row.createCell(COL.PRECO_ULT_COMPRA);
		cell.setCellValue("Preço Ult Compra");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.PRECO_ULT_COMPRA, 256 * 18);

		cell = row.createCell(COL.PRECO_VENDA);
		cell.setCellValue("Preço Venda");
		cell.setCellStyle(styles.get("cabecalho"));
		sheet.setColumnWidth(COL.PRECO_VENDA, 256 * 14);
	}

	/**
	 * Checking if every row and cell in merging region exists, and create those which are not    
	 * @param sheet in which check is performed
	 * @param region to check
	 * @param cellStyle cell style to apply for whole region
	 */
	private void cleanBeforeMergeOnValidCells(XSSFSheet sheet,CellRangeAddress region, XSSFCellStyle cellStyle ) {
		
	    for(int rowNum =region.getFirstRow();rowNum<=region.getLastRow();rowNum++) {
	        XSSFRow row= sheet.getRow(rowNum);
	        
	        if(row==null) {
	            sheet.createRow(rowNum);
	        }
	        
	        for(int colNum=region.getFirstColumn();colNum<=region.getLastColumn();colNum++) {
	           XSSFCell currentCell = row.getCell(colNum); 
	            
	           if(currentCell==null) {
	               currentCell = row.createCell(colNum);
	           }    

	           currentCell.setCellStyle(cellStyle);
	        }
	    }
	}
	
	/**
     * cell styles used for formatting calendar sheets
     */
    private static Map<String, XSSFCellStyle> createStyles(XSSFWorkbook wb, CreationHelper factory){
        Map<String, XSSFCellStyle> styles = new HashMap<String, XSSFCellStyle>();

        XSSFCellStyle style;
        XSSFFont titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
        style.setFont(titleFont);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styles.put("cabecalho", style);

        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        
        style = wb.createCellStyle();
		style.setDataFormat(factory.createDataFormat().getFormat("dd/mm/yyyy"));
		style.setAlignment(XSSFCellStyle.ALIGN_LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("dateParameterStyle", style);
        
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(false);

        style = wb.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("centeredTextStyle", style);

        style = wb.createCellStyle();
		style.setDataFormat(factory.createDataFormat().getFormat("R$ #,##0.00"));
		style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("valorMonetario", style);

        style = wb.createCellStyle();
		style.setDataFormat(factory.createDataFormat().getFormat("dd/mm/yyyy"));
        style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("dateStyle", style);
        
        titleFont = wb.createFont();
        titleFont.setFontHeightInPoints((short)11);
        titleFont.setBold(true);
        titleFont.setColor(new XSSFColor(new java.awt.Color(39, 51, 89)));
        style = wb.createCellStyle();
        style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        styles.put("titulo", style);

        style = wb.createCellStyle();
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("rodape", style);

        style = wb.createCellStyle();
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFont(titleFont);
        styles.put("rodapeQuantidade", style);

        style = wb.createCellStyle();
        style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setDataFormat(factory.createDataFormat().getFormat("R$ #,##0.00"));
		style.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
        style.setFont(titleFont);
        styles.put("rodapeValor", style);

        return styles;
    }	

}
