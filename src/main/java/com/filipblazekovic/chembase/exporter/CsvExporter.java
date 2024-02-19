package com.filipblazekovic.chembase.exporter;

import com.filipblazekovic.chembase.db.DBHandler;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import lombok.SneakyThrows;
import lombok.val;

public class CsvExporter extends Exporter {

  @Override
  @SneakyThrows
  public void export(File outputPath, boolean includeAllColumns) {

    try (val output = new BufferedWriter(new FileWriter(outputPath))) {

      // Write title row
      val titleLineBuilder = new StringBuilder();
      val maxElementIndex = includeAllColumns
          ? (columnTitles.length)
          : (columnTitles.length - 4);

      int i;
      for (i = 0; i < (maxElementIndex-1); i++) {
        titleLineBuilder
            .append(columnTitles[i])
            .append(",");
      }

      titleLineBuilder.append(columnTitles[i]);
      output.write(titleLineBuilder + "\n");

      // Write data rows
      DBHandler
          .getChemicals()
          .forEach(c -> {

            val rowBuilder = new StringBuilder();

            rowBuilder
                .append(c.id()).append(",")
                .append(c.name()).append(",")
                .append(c.bruttoFormula()).append(",")
                .append(c.molarMass()).append(",")
                .append(c.amount()).append(",")
                .append(c.unit().name()).append(",")
                .append(c.storageLocation());

            if (includeAllColumns) {
              rowBuilder
                  .append(",")
                  .append(c.manufacturer() == null ? "" : c.manufacturer()).append(",")
                  .append(c.supplier() == null ? "" : c.supplier()).append(",")
                  .append(c.dateOfEntry()).append(",")
                  .append(c.additionalInfo() == null ? "" : c.additionalInfo());
            }

            try {
              output.write(rowBuilder + "\n");
            } catch (Exception ignored) {
            }
          });
    }
  }

}
