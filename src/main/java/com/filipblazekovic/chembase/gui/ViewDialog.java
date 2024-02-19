package com.filipblazekovic.chembase.gui;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.util.Common;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import lombok.val;

public class ViewDialog extends JDialog {

  public ViewDialog(int id) {

    val chemical = DBHandler.getChemical(id);

    val nameLabel = new JLabel("Name: ");
    val bruttoFormulaLabel = new JLabel("Brutto formula: ");
    val molarMassLabel = new JLabel("Molar mass: ");
    val quantityLabel = new JLabel("Quantity: ");
    val storageLocationLabel = new JLabel("Storage location: ");
    val manufacturerLabel = new JLabel("Manufacturer: ");
    val supplierLabel = new JLabel("Supplier: ");
    val additionalInfoLabel = new JLabel("Additional info: ");
    val dateOfEntryLabel = new JLabel("Date:");

    val nameField = new JTextField(30);
    val bruttoFormulaField = new JTextField(30);
    val molarMassField = new JTextField(30);
    val quantityField = new JTextField(30);
    val storageLocationField = new JTextField(30);
    val manufacturerField = new JTextField(30);
    val supplierField = new JTextField(30);
    val dateOfEntryField = new JTextField(30);
    val additionalInfoArea = new JTextArea(10, 50);

    if (chemical.isPresent()) {
      val c = chemical.get();
      nameField.setText(c.name());
      bruttoFormulaField.setText(c.bruttoFormula());
      molarMassField.setText(String.valueOf(c.molarMass()));
      quantityField.setText(c.amount() + " " + c.unit().name());
      storageLocationField.setText(c.storageLocation());
      manufacturerField.setText(c.manufacturer() == null ? "" : c.manufacturer());
      supplierField.setText(c.supplier() == null ? "" : c.supplier());
      dateOfEntryField.setText(c.dateOfEntry().toString());
      additionalInfoArea.setText(c.additionalInfo() == null ? "" : c.additionalInfo());
    }

    nameField.setEditable(false);
    bruttoFormulaField.setEditable(false);
    molarMassField.setEditable(false);
    quantityField.setEditable(false);
    storageLocationField.setEditable(false);
    manufacturerField.setEditable(false);
    supplierField.setEditable(false);
    dateOfEntryField.setEditable(false);
    additionalInfoArea.setEditable(false);

    val gridPanel = new JPanel(new GridLayout(10, 2, 0, 5));
    gridPanel.add(nameLabel);
    gridPanel.add(nameField);
    gridPanel.add(bruttoFormulaLabel);
    gridPanel.add(bruttoFormulaField);
    gridPanel.add(molarMassLabel);
    gridPanel.add(molarMassField);
    gridPanel.add(quantityLabel);
    gridPanel.add(quantityField);
    gridPanel.add(storageLocationLabel);
    gridPanel.add(storageLocationField);
    gridPanel.add(manufacturerLabel);
    gridPanel.add(manufacturerField);
    gridPanel.add(supplierLabel);
    gridPanel.add(supplierField);
    gridPanel.add(dateOfEntryLabel);
    gridPanel.add(dateOfEntryField);
    gridPanel.add(additionalInfoLabel);

    val infoPane = new JScrollPane(
        additionalInfoArea,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    val rootPane = new JPanel(new BorderLayout());
    rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    rootPane.add(gridPanel, BorderLayout.NORTH);
    rootPane.add(infoPane, BorderLayout.CENTER);

    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setTitle(Common.WINDOW_TITLE);
    this.setContentPane(rootPane);
    this.setSize(550, 550);
    this.setResizable(false);
    this.setModal(true);
    this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
    this.setVisible(true);
  }

}
