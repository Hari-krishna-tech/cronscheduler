package com.ms.cronscheduler.service;


import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public byte[] writeDataToExcelUsingFastExcel(List<List<Map<String, Object>>> data) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Workbook workbook = new Workbook(outputStream, "DemoExcel", "1.0");

        for (int i = 0; i < data.size(); i++) {
            List<Map<String, Object>> sheetData = data.get(i);
            Worksheet sheet = workbook.newWorksheet("Sheet " + (i + 1));

            if (!sheetData.isEmpty()) {
                // Creating Header Row
                Map<String, Object> firstRow = sheetData.get(0);
                List<String> keys = new ArrayList<>(firstRow.keySet());

                for (int col = 0; col < keys.size(); col++) {
                    sheet.value(0, col, keys.get(col));
                }

                // Creating data rows
                for (int rowNum = 0; rowNum < sheetData.size(); rowNum++) {
                    Map<String, Object> rowData = sheetData.get(rowNum);
                    for (int colNum = 0; colNum < keys.size(); colNum++) {
                        Object value = rowData.get(keys.get(colNum));
                        if (value != null) {
                            sheet.value(rowNum + 1, colNum, value.toString());
                        }
                    }
                }
            }
        }

        workbook.finish();
        return outputStream.toByteArray();
    }


    public byte[] writeDataToExcel(List<List<Map<String, Object>>> data) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        for (int i = 0; i < data.size(); i++) {
            List<Map<String, Object>> sheetData = data.get(i);
            Sheet sheet = workbook.createSheet("Sheet " + (i + 1));

            if (!sheetData.isEmpty()) {
                // Creating Header Row
                Row headerRow = sheet.createRow(0);
                Map<String, Object> firstRow = sheetData.get(0);

                int headerCol = 0;

                // Create a bold font style for the header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                CellStyle headerCellStyle = workbook.createCellStyle();
                headerCellStyle.setFont(headerFont);

                // Store keys in a list for reuse
                List<String> keys = new ArrayList<>(firstRow.keySet());

                for (String key : keys) {
                    Cell cell = headerRow.createCell(headerCol);
                    cell.setCellValue(key);
                    cell.setCellStyle(headerCellStyle);
                    headerCol++;
                }

                // Creating data rows
                int rowNum = 1;
                for (Map<String, Object> rowData : sheetData) {
                    Row row = sheet.createRow(rowNum++);
                    int colNum = 0;
                    for (String key : keys) {
                        Cell cell = row.createCell(colNum);
                        Object value = rowData.get(key);
                        if (value != null) {
                            cell.setCellValue(value.toString());
                        }
                        colNum++;
                    }
                }

                // Auto-size columns after all rows are written
                for (int col = 0; col < keys.size(); col++) {
                    sheet.autoSizeColumn(col);
                }
            }
        }

        // Writing the workbook to the output stream
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}
