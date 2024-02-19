package com.filipblazekovic.chembase.exporter;

import com.filipblazekovic.chembase.db.DBHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import lombok.SneakyThrows;
import lombok.val;

public class TxtExporter extends Exporter {

  private static final String[] formatSpecifiers = {
      "%-10s",
      "%-35s",
      "%-25s",
      "%-20s",
      "%-20s",
      "%-10s",
      "%-50s",
      "%-35s",
      "%-35s",
      "%-20s",
      "%-100s"
  };

  @Override
  @SneakyThrows
  public void export(File outputPath, boolean includeAllColumns) {

    try (val output = new BufferedWriter(new FileWriter(outputPath))) {

      // Write title row
      val titleLineBuilder = new StringBuilder();
      val maxElementIndex = includeAllColumns
          ? (columnTitles.length)
          : (columnTitles.length - 4);

      for (int i = 0; i < maxElementIndex; i++) {
        titleLineBuilder.append(String.format(formatSpecifiers[i], columnTitles[i]));
      }

      output.write(titleLineBuilder + "\n");

      // Write data rows
      DBHandler
          .getChemicals()
          .forEach(c -> {

            val rowBuilder = new StringBuilder();

            rowBuilder.append(String.format(formatSpecifiers[0], c.id()));
            rowBuilder.append(String.format(formatSpecifiers[1], c.name()));
            rowBuilder.append(String.format(formatSpecifiers[2], c.bruttoFormula()));
            rowBuilder.append(String.format(formatSpecifiers[3], c.molarMass()));
            rowBuilder.append(String.format(formatSpecifiers[4], c.amount()));
            rowBuilder.append(String.format(formatSpecifiers[5], c.unit().name()));
            rowBuilder.append(String.format(formatSpecifiers[6], c.storageLocation()));

            if (includeAllColumns) {
              rowBuilder.append(String.format(formatSpecifiers[7], c.manufacturer() == null ? "" : c.manufacturer()));
              rowBuilder.append(String.format(formatSpecifiers[8], c.supplier() == null ? "" : c.supplier()));
              rowBuilder.append(String.format(formatSpecifiers[9], c.dateOfEntry().toString()));
              rowBuilder.append(String.format(formatSpecifiers[10], c.additionalInfo() == null ? "" : c.additionalInfo()));
            }

            try {
              output.write(rowBuilder + "\n");
            } catch (Exception ignored) {
            }

          });
    }
  }

}
