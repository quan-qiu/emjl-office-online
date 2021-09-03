package net.csdcodes.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import net.csdcodes.model.PrMainDetail;
import net.csdcodes.model.ProcurementRequisitionMain;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class PrmExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<PrMainDetail> listPrmds;

    public PrmExcelExporter(List<PrMainDetail> listPrmds) {
        this.listPrmds = listPrmds;
        workbook = new XSSFWorkbook();
    }


    private void writeHeaderLine() {
        sheet = workbook.createSheet("ProcurementRequisitionMain");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "PR Title", style);
        createCell(row, 1, "Apply User Name", style);
        createCell(row, 2, "Apply User SSN", style);
        createCell(row, 3, "Cost Center", style);
        createCell(row, 4, "Apply Dept", style);
        createCell(row, 5, "PR NO.", style);
        createCell(row, 6, "Apply Date", style);
        createCell(row, 7, "Project Name", style);
        createCell(row, 8, "Flow Type", style);
        createCell(row, 9, "Finished", style);
        createCell(row, 10, "Approved", style);
        createCell(row, 11, "Submitted", style);
        createCell(row, 12, "ERP Code", style);
        createCell(row, 13, "ERP desc", style);
        createCell(row, 14, "ERP brand size", style);
        createCell(row, 15, "Quantity", style);
        createCell(row, 16, "ERP unit", style);
        createCell(row, 17, "Cost", style);
        createCell(row, 18, "Memo", style);
        createCell(row, 19, "PO Code", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else if (value instanceof Float) {
            cell.setCellValue((Float) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (PrMainDetail prmd : listPrmds) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, prmd.getPrm().getPrTitle(), style);
            createCell(row, columnCount++, prmd.getPrm().getPoCode(), style);
            createCell(row, columnCount++, prmd.getPrm().getAplUserName(), style);
            createCell(row, columnCount++, prmd.getPrm().getAplUserSsn(), style);
            createCell(row, columnCount++, prmd.getPrm().getCostCenter(), style);
            createCell(row, columnCount++, prmd.getPrm().getAplDept(), style);
            createCell(row, columnCount++, prmd.getPrm().getPrNo(), style);
            createCell(row, columnCount++, prmd.getPrm().getPrAplDate().toString(), style);
            createCell(row, columnCount++, prmd.getPrm().getProjectName(), style);
            createCell(row, columnCount++, prmd.getPrm().getFlowType(), style);
            createCell(row, columnCount++, prmd.getPrm().getFinished(), style);
            createCell(row, columnCount++, prmd.getPrm().getApproved(), style);

            createCell(row, columnCount++, prmd.getPrd().getItemErpCode(), style);
            createCell(row, columnCount++, prmd.getPrd().getItemErpDesc(), style);
            createCell(row, columnCount++, prmd.getPrd().getItemErpBrandSize(), style);
            createCell(row, columnCount++, prmd.getPrd().getQty(), style);
            createCell(row, columnCount++, prmd.getPrd().getItemErpUnit(), style);
            createCell(row, columnCount++, prmd.getPrd().getEstCost(), style);
            createCell(row, columnCount++, prmd.getPrd().getMemo(), style);
            createCell(row, columnCount++, prmd.getPrd().getPoCode(), style);
        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
