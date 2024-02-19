package com.filipblazekovic.chembase.gui;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.model.internal.Chemical;
import com.filipblazekovic.chembase.model.shared.Unit;
import com.filipblazekovic.chembase.util.Common;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import lombok.val;

public class AddEditDialog extends JDialog {

  private final AddEditDialog referenceToThis;

  public AddEditDialog(Integer id) {
    referenceToThis = this;

    final Optional<Chemical> chemical = (id == null)
        ? Optional.empty()
        : DBHandler.getChemical(id);

    val locations = DBHandler.getLocations();

    if (locations.isEmpty()) {
      new InformationDialog("Set-up locations first in the Settings toolbar!");
      referenceToThis.setVisible(false);
      referenceToThis.dispose();
      return;
    }

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
    val quantityAmountField = new JTextField(30);
    val quantityUnitField = new JComboBox<>(
        Arrays
            .stream(Unit.values())
            .map(Enum::name)
            .toList()
            .toArray(new String[0])
    );
    val storageLocationField = new JComboBox<>(locations.toArray(new String[0]));
    val manufacturerField = new JTextField(30);
    val supplierField = new JTextField(30);
    val dateOfEntryField = new JTextField(30);
    val additionalInfoArea = new JTextArea(10, 50);

    val addEditButton = new JButton("Add Record");
    val cancelButton = new JButton("Cancel");

    addEditButton.setCursor(Common.CURSOR);
    cancelButton.setCursor(Common.CURSOR);

    val date = LocalDate.now();

    nameField.setEditable(true);
    bruttoFormulaField.setEditable(true);
    molarMassField.setEditable(true);
    quantityAmountField.setEditable(true);
    storageLocationField.setEditable(true);
    manufacturerField.setEditable(true);
    supplierField.setEditable(true);
    dateOfEntryField.setEditable(true);
    additionalInfoArea.setEditable(true);

    if (chemical.isPresent()) {
      val c = chemical.get();

      nameField.setText(c.name());
      bruttoFormulaField.setText(c.bruttoFormula());
      molarMassField.setText(String.valueOf(c.molarMass()));
      quantityAmountField.setText(String.valueOf(c.amount()));
      quantityUnitField.setSelectedItem(c.unit().name());
      storageLocationField.setSelectedItem(c.storageLocation());
      manufacturerField.setText(c.manufacturer() == null ? "" : c.manufacturer());
      supplierField.setText(c.supplier() == null ? "" : c.supplier());
      dateOfEntryField.setText(c.dateOfEntry().toString());
      additionalInfoArea.setText(c.additionalInfo() == null ? "" : c.additionalInfo());

      addEditButton.setText("Edit Record");
      cancelButton.setText("Cancel");

    } else {
      nameField.setText("");
      bruttoFormulaField.setText("");
      molarMassField.setText("");
      quantityAmountField.setText("");
      quantityUnitField.setSelectedIndex(-1);
      storageLocationField.setSelectedIndex(-1);
      manufacturerField.setText("");
      supplierField.setText("");
      dateOfEntryField.setText(date.toString());
      additionalInfoArea.setText("");
    }

    dateOfEntryField.setEditable(false);

    val gridPanel = new JPanel(new GridLayout(11, 2, 0, 5));
    gridPanel.add(nameLabel);
    gridPanel.add(nameField);
    gridPanel.add(bruttoFormulaLabel);
    gridPanel.add(bruttoFormulaField);
    gridPanel.add(molarMassLabel);
    gridPanel.add(molarMassField);
    gridPanel.add(quantityLabel);
    gridPanel.add(quantityAmountField);
    gridPanel.add(new JLabel());
    gridPanel.add(quantityUnitField);
    gridPanel.add(storageLocationLabel);
    gridPanel.add(storageLocationField);
    gridPanel.add(manufacturerLabel);
    gridPanel.add(manufacturerField);
    gridPanel.add(supplierLabel);
    gridPanel.add(supplierField);
    gridPanel.add(dateOfEntryLabel);
    gridPanel.add(dateOfEntryField);
    gridPanel.add(additionalInfoLabel);

    cancelButton.addActionListener(e -> {
      referenceToThis.setVisible(false);
      referenceToThis.dispose();
    });

    addEditButton.addActionListener(e -> {
      val chemicalName = nameField.getText().trim();
      val bruttoFormula = bruttoFormulaField.getText().trim().toUpperCase();
      val molarMass = molarMassField.getText().trim().replaceAll(",", ".");
      val quantityAmount = quantityAmountField.getText().trim().replaceAll(",", ".");
      val manufacturer = manufacturerField.getText().trim();
      val supplier = supplierField.getText().trim();
      val additionalInfo = additionalInfoArea.getText().trim();
      val quantityUnit = (String) quantityUnitField.getSelectedItem();
      val storageLocation = (String) storageLocationField.getSelectedItem();

      if (chemicalName.isEmpty()) {
        new InformationDialog("Chemical name must be specified!");
      } else if (quantityAmount.isEmpty()) {
        new InformationDialog("Quantity must be specified!");
      } else if (!quantityAmount.matches("\\d+(\\.\\d{1,8})?")) {
        new InformationDialog("Invalid quantity format! Valid format examples: 500, 10.12, 10.1");
      } else if (quantityUnit == null) {
        new InformationDialog("Quantity unit must be selected!");
      } else if (storageLocation == null) {
        new InformationDialog("Storage location must be selected!");
      } else if (bruttoFormula.isEmpty()) {
        new InformationDialog("Brutto formula must be specified!");
      } else if (molarMass.isEmpty()) {
        new InformationDialog("Molar mass must be specified!");
      } else if (!bruttoFormula.matches("[A-Z0-9]+")) {
        new InformationDialog("Invalid brutto formula format! Valid characters are: A-Z, 0-9");
      } else if (!molarMass.matches("\\d+(\\.\\d{1,8})?")) {
        new InformationDialog("Invalid molar mass format! Valid format examples: 64.04, 10, 10.1");
      } else {

        try {

          val newChemical = new Chemical(
              id,
              chemicalName,
              bruttoFormula,
              Double.valueOf(molarMass),
              Double.valueOf(quantityAmount),
              Unit.from(quantityUnit),
              storageLocation,
              manufacturer,
              supplier,
              date,
              additionalInfo
          );
          if (chemical.isPresent()) {
            DBHandler.editChemical(chemical.get().merge(newChemical));
          } else {
            DBHandler.addChemical(newChemical);
          }
          ChemBase.refresh();

          referenceToThis.setVisible(false);
          referenceToThis.dispose();

        } catch (Exception ex) {
          new InformationDialog("Something went wrong!");
        }
      }
    });

    val infoPane = new JScrollPane(
        additionalInfoArea,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    val buttonsPanel = new JPanel(new GridLayout(1, 2));
    buttonsPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
    buttonsPanel.add(addEditButton);
    buttonsPanel.add(cancelButton);

    val rootPane = new JPanel(new BorderLayout(0, 0));
    rootPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    rootPane.add(gridPanel, BorderLayout.NORTH);
    rootPane.add(infoPane, BorderLayout.CENTER);
    rootPane.add(buttonsPanel, BorderLayout.SOUTH);

    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setTitle(Common.WINDOW_TITLE);
    this.setContentPane(rootPane);
    this.setSize(550, 600);
    this.setResizable(false);
    this.setModal(true);
    this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
    this.setVisible(true);
  }

}
