package com.filipblazekovic.chembase.exporter;

import java.io.File;

public abstract class Exporter {

  protected static final String[] columnTitles = {
      "ID",
      "Chemical Name",
      "Brutto Formula",
      "Molar Mass",
      "Quantity",
      "Unit",
      "Storage Location",
      "Manufacturer",
      "Supplier",
      "Date of Entry",
      "Additional Info"
  };

  public abstract void export(File outputPath, boolean includeAllColumns);

}
