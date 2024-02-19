package com.filipblazekovic.chembase.listener;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.exporter.CsvExporter;
import com.filipblazekovic.chembase.exporter.JsonExporter;
import com.filipblazekovic.chembase.exporter.PdfExporter;
import com.filipblazekovic.chembase.exporter.TxtExporter;
import com.filipblazekovic.chembase.exporter.XlsxExporter;
import com.filipblazekovic.chembase.gui.InformationDialog;
import com.filipblazekovic.chembase.model.shared.ExportFormat;
import com.filipblazekovic.chembase.util.Common;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import lombok.val;

public class ExportListener implements ActionListener {

  private final ExportFormat format;

  public ExportListener(ExportFormat format) {
    this.format = format;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    val fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Select output location");

    if (fileChooser.showSaveDialog(ChemBase.getChemBaseRoot()) == JFileChooser.APPROVE_OPTION) {
      val outputFile = fileChooser.getSelectedFile();

      try {
        switch (format) {
          case CSV -> new CsvExporter().export(outputFile, ChemBase.isFullExportOn());
          case JSON -> new JsonExporter().export(outputFile, ChemBase.isFullExportOn());
          case XLSX -> new XlsxExporter().export(outputFile, ChemBase.isFullExportOn());
          case PDF -> new PdfExporter().export(outputFile, false);
          default -> new TxtExporter().export(outputFile, ChemBase.isFullExportOn());
        }
        new InformationDialog("Exported to: " + outputFile.getName());
      } catch (Exception ex) {
        new InformationDialog("Something went wrong!");
      }
    }
  }
}





