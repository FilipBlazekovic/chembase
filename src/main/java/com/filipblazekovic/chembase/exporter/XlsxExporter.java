package com.filipblazekovic.chembase.exporter;

import com.filipblazekovic.chembase.db.DBHandler;
import java.io.File;
import java.io.FileOutputStream;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;

public class XlsxExporter extends Exporter {

  @Override
  @SneakyThrows
  public void export(File outputPath, boolean includeAllColumns) {

    try (
        val output = new FileOutputStream(outputPath);
        val workbook = new SXSSFWorkbook(100)
    ) {

      // Creating cell style for title columns
      val titlesFont = (XSSFFont) workbook.createFont();
      titlesFont.setBold(true);

      val titlesStyle = (XSSFCellStyle) workbook.createCellStyle();
      titlesStyle.setFont(titlesFont);
      titlesStyle.setAlignment(HorizontalAlignment.LEFT);
      titlesStyle.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
      titlesStyle.setFillBackgroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
      titlesStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
      titlesStyle.setBorderTop(BorderStyle.THIN);
      titlesStyle.setBorderBottom(BorderStyle.THIN);
      titlesStyle.setBorderLeft(BorderStyle.THIN);
      titlesStyle.setBorderRight(BorderStyle.THIN);
      titlesStyle.setWrapText(true);

      // Creating cell style for data columns
      val dataStyle = (XSSFCellStyle) workbook.createCellStyle();
      dataStyle.setAlignment(HorizontalAlignment.LEFT);
      dataStyle.setBorderBottom(BorderStyle.THIN);
      dataStyle.setBorderLeft(BorderStyle.THIN);
      dataStyle.setBorderRight(BorderStyle.THIN);

      // Write title row
      val sheet = workbook.createSheet("ChemBase");
      val sheetTitleRow = sheet.createRow(0);

      val maxElementIndex = includeAllColumns
          ? (columnTitles.length)
          : (columnTitles.length - 4);

      for (int i = 0; i < maxElementIndex; i++) {
        val cell = sheetTitleRow.createCell(i);
        cell.setCellStyle(titlesStyle);
        cell.setCellValue(columnTitles[i]);
      }

      // Write data rows
      val chemicals = DBHandler.getChemicals();
      for (int i = 0; i < chemicals.size(); i++) {

        val c = chemicals.get(i);

        val currentRow = sheet.createRow(i + 1);

        val idCell = currentRow.createCell(0);
        val nameCell = currentRow.createCell(1);
        val bruttoFormulaCell = currentRow.createCell(2);
        val molarMassCell = currentRow.createCell(3);
        val quantityCell = currentRow.createCell(4);
        val quantityUnitCell = currentRow.createCell(5);
        val storageLocationCell = currentRow.createCell(6);

        idCell.setCellStyle(dataStyle);
        nameCell.setCellStyle(dataStyle);
        bruttoFormulaCell.setCellStyle(dataStyle);
        molarMassCell.setCellStyle(dataStyle);
        quantityCell.setCellStyle(dataStyle);
        quantityUnitCell.setCellStyle(dataStyle);
        storageLocationCell.setCellStyle(dataStyle);

        idCell.setCellValue(c.id());
        nameCell.setCellValue(c.name());
        bruttoFormulaCell.setCellValue(c.bruttoFormula());
        molarMassCell.setCellValue(c.molarMass());
        quantityCell.setCellValue(c.amount());
        quantityUnitCell.setCellValue(c.unit().name());
        storageLocationCell.setCellValue(c.storageLocation());

        if (includeAllColumns) {

          val manufacturerCell = currentRow.createCell(7);
          val supplierCell = currentRow.createCell(8);
          val dateOfEntryCell = currentRow.createCell(9);
          val additionalInfoCell = currentRow.createCell(10);

          manufacturerCell.setCellStyle(dataStyle);
          supplierCell.setCellStyle(dataStyle);
          dateOfEntryCell.setCellStyle(dataStyle);
          additionalInfoCell.setCellStyle(dataStyle);

          manufacturerCell.setCellValue(c.manufacturer() == null ? "" : c.manufacturer());
          supplierCell.setCellValue(c.supplier() == null ? "" : c.supplier());
          dateOfEntryCell.setCellValue(c.dateOfEntry().toString());
          additionalInfoCell.setCellValue(c.additionalInfo() == null ? "" : c.additionalInfo());

        }
      }

      sheet.setColumnWidth(0, 20 * 256);
      sheet.setColumnWidth(1, 35 * 256);
      sheet.setColumnWidth(2, 25 * 256);
      sheet.setColumnWidth(3, 20 * 256);
      sheet.setColumnWidth(4, 20 * 256);
      sheet.setColumnWidth(5, 10 * 256);
      sheet.setColumnWidth(6, 50 * 256);

      if (includeAllColumns) {
        sheet.setColumnWidth(7, 35 * 256);
        sheet.setColumnWidth(8, 35 * 256);
        sheet.setColumnWidth(9, 20 * 256);
        sheet.setColumnWidth(10, 50 * 256);
      }

      workbook.write(output);
    }
  }

}
