package net.csdcodes.util;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

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
    private List<ProcurementRequisitionMain> listPrms;

    public PrmExcelExporter(List<ProcurementRequisitionMain> listPrms) {
        this.listPrms = listPrms;
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
        createCell(row, 6, "PO Code", style);
        createCell(row, 7, "Apply Date", style);
        createCell(row, 8, "Project Name", style);
        createCell(row, 9, "Flow Type", style);
        createCell(row, 10, "Finished", style);
        createCell(row, 11, "Approved", style);
        createCell(row, 12, "Submitted", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
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

        for (ProcurementRequisitionMain prm : listPrms) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, prm.getPrTitle(), style);
            createCell(row, columnCount++, prm.getPoCode(), style);
            createCell(row, columnCount++, prm.getAplUserName(), style);
            createCell(row, columnCount++, prm.getAplUserSsn(), style);
            createCell(row, columnCount++, prm.getCostCenter(), style);
            createCell(row, columnCount++, prm.getAplDept(), style);
            createCell(row, columnCount++, prm.getPrNo(), style);
            createCell(row, columnCount++, prm.getPoCode(), style);
            createCell(row, columnCount++, prm.getPrAplDate().toString(), style);
            createCell(row, columnCount++, prm.getProjectName(), style);
            createCell(row, columnCount++, prm.getFlowType(), style);
            createCell(row, columnCount++, prm.getFinished(), style);
            createCell(row, columnCount++, prm.getApproved(), style);
            createCell(row, columnCount++, prm.getSubmitted(), style);
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
