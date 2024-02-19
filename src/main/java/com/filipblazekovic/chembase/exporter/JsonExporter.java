package com.filipblazekovic.chembase.exporter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.model.out.ExportChemical;
import com.filipblazekovic.chembase.model.out.ExportChemicalFull;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.val;

public class JsonExporter extends Exporter {

  @Override
  @SneakyThrows
  public void export(File outputPath, boolean includeAllColumns) {
    val chemicals = DBHandler.getChemicals();
    val export = new ObjectMapper().writeValueAsString(
        includeAllColumns
            ? ExportChemicalFull.from(chemicals)
            : ExportChemical.from(chemicals)
    );
    try (val output = new FileOutputStream(outputPath)) {
      output.write(
          export.getBytes(StandardCharsets.UTF_8)
      );
    }
  }
}
