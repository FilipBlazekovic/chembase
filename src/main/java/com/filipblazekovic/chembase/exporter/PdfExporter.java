package com.filipblazekovic.chembase.exporter;

import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.model.internal.Chemical;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.val;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

public class PdfExporter extends Exporter {

  private static final int fontSize = 8;
  private static final int rowsPerPage = 32;

  private static final int[] fieldWidths = {
      70,
      150,
      140,
      75,
      85,
      75,
      150,
      65,
      60,
      60,
      130
  };

  @Override
  @SneakyThrows
  public void export(File outputPath, boolean includeAllColumns) {
    val chemicals = DBHandler.getChemicals();
    var singlePageData = new ArrayList<Chemical>();

    try (
        val document = new PDDocument();
        val fontInput = PdfExporter.class.getClassLoader().getResourceAsStream("OpenSans-Regular.ttf");
        val fontBoldInput = PdfExporter.class.getClassLoader().getResourceAsStream("OpenSans-Bold.ttf")
    ) {

      val font = PDType0Font.load(document, fontInput);
      val fontBold = PDType0Font.load(document, fontBoldInput);

      for (int i = 0; i < chemicals.size(); i++) {
        singlePageData.add(chemicals.get(i));
        if ((i+1) % rowsPerPage == 0) {
          generatePDFPage(document, singlePageData, includeAllColumns, font, fontBold);
          singlePageData = new ArrayList<>();
        }
      }

      if (!singlePageData.isEmpty()) {
        generatePDFPage(document, singlePageData, includeAllColumns, font, fontBold);
      }
      document.save(outputPath);
    }
  }

  @SneakyThrows
  private static void generatePDFPage(PDDocument document, List<Chemical> chemicals, boolean includeAllColumns, PDType0Font font, PDType0Font fontBold) {
    val page = new PDPage(new PDRectangle(PDRectangle.A4.getHeight(), PDRectangle.A4.getWidth()));
    document.addPage(page);
    val contents = new PDPageContentStream(document, page);

    int currentXPosition = 30;
    int currentYPosition = 550;

    // Write title row
    val maxElementIndex = includeAllColumns
        ? (columnTitles.length)
        : (columnTitles.length - 4);

    contents.beginText();
    contents.setFont(fontBold, fontSize);
    contents.newLineAtOffset(currentXPosition, currentYPosition);

    int totalXOffset = 0;
    for (int i = 0; i < maxElementIndex; i++) {
      contents.showText(columnTitles[i]);
      contents.newLineAtOffset(fieldWidths[i], 0);
      totalXOffset += fieldWidths[i];
    }
    contents.endText();

    // Write data rows
    currentYPosition = currentYPosition - (int) (2.5 * fontSize);
    contents.beginText();
    contents.setFont(font, fontSize);
    contents.newLineAtOffset(30, currentYPosition);

    for (Chemical c : chemicals) {

      contents.showText(String.valueOf(c.id()));
      contents.newLineAtOffset(fieldWidths[0], 0);
      contents.showText(c.name());
      contents.newLineAtOffset(fieldWidths[1], 0);
      contents.showText(c.bruttoFormula());
      contents.newLineAtOffset(fieldWidths[2], 0);
      contents.showText(String.valueOf(c.molarMass()));
      contents.newLineAtOffset(fieldWidths[3], 0);
      contents.showText(String.valueOf(c.amount()));
      contents.newLineAtOffset(fieldWidths[4], 0);
      contents.showText(c.unit().name());
      contents.newLineAtOffset(fieldWidths[5], 0);
      contents.showText(c.storageLocation());
      contents.newLineAtOffset(fieldWidths[6], 0);

      if (includeAllColumns) {
        contents.showText(c.manufacturer() == null ? "" : c.manufacturer());
        contents.newLineAtOffset(fieldWidths[7], 0);
        contents.showText(c.supplier() == null ? "" : c.supplier());
        contents.newLineAtOffset(fieldWidths[8], 0);
        contents.showText(c.dateOfEntry().toString());
        contents.newLineAtOffset(fieldWidths[9], 0);
        contents.showText(c.additionalInfo() == null ? "" : c.additionalInfo());
        contents.newLineAtOffset(fieldWidths[10], 0);
      }

      contents.newLineAtOffset(-totalXOffset, -(float) (2 * fontSize));
    }

    contents.endText();
    contents.close();
  }


}
