package com.filipblazekovic.chembase.listener;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.gui.InformationDialog;
import com.filipblazekovic.chembase.model.shared.SearchByOption;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import lombok.val;

public class SearchListener implements ActionListener {

  private final JTable table;

  private final DefaultTableModel tableModel;

  public SearchListener(JTable table, DefaultTableModel tableModel) {
    this.table = table;
    this.tableModel = tableModel;
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    try {

      val searchPhrase = ChemBase.getSearchPhrase();
      val searchBy = ChemBase.getSelectedSearchByOption();
      val regexSearchOn = ChemBase.isRegexSearchOn();

      if (searchBy == SearchByOption.MOLAR_MASS && !searchPhrase.replaceAll(",", ".").matches("\\d+(\\.\\d{1,8})?")) {
        new InformationDialog("Invalid molar mass format!");
        return;
      }

      tableModel.setRowCount(0);
      DBHandler
          .getChemicals(
              searchPhrase,
              searchBy,
              regexSearchOn
          )
          .forEach(c -> tableModel.addRow(
              new Object[]{
                  c.id(),
                  c.name(),
                  c.bruttoFormula(),
                  c.molarMass(),
                  c.amount(),
                  c.unit().name(),
                  c.storageLocation()
              })
          );
      table.doLayout();
    } catch (Exception ex) {
      new InformationDialog("Something went wrong!");
    }
  }
}
