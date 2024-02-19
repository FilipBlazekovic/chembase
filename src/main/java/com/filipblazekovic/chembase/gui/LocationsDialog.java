package com.filipblazekovic.chembase.gui;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.db.DBConnection;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.util.Common;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import lombok.val;

public class LocationsDialog extends JDialog {

  private final LocationsDialog referenceToThis;

  public LocationsDialog() {
    referenceToThis = this;

    DBConnection.setAutocommit(false);

    val locationNames = DBHandler.getLocations();
    val locationField = new JTextField(30);
    val locationsList = new JList<>(locationNames.toArray(new String[0]));
    val addButton = new JButton("ADD");
    val deleteButton = new JButton("DELETE");
    val commitButton = new JButton("Save changes");
    val rollbackButton = new JButton("Cancel change");

    addButton.setCursor(Common.CURSOR);
    deleteButton.setCursor(Common.CURSOR);
    commitButton.setCursor(Common.CURSOR);
    rollbackButton.setCursor(Common.CURSOR);

    locationsList.addListSelectionListener(e -> {
      val selectedLocation = locationsList.getSelectedValue();
      locationField.setText(selectedLocation);
    });

    locationsList.setVisibleRowCount(10);
    locationsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    val locationsPane = new JScrollPane(
        locationsList,
        JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );

    val topPanel = new JPanel(new GridLayout(2,1));
    topPanel.add(new JLabel("Enter location:"));
    topPanel.add(locationField);

    val bottomPanel = new JPanel(new GridLayout(2,1));
    bottomPanel.add(addButton);
    bottomPanel.add(deleteButton);

    val contentPanel = new JPanel(new BorderLayout(0,2));
    contentPanel.add(topPanel, BorderLayout.NORTH);
    contentPanel.add(locationsPane, BorderLayout.CENTER);
    contentPanel.add(bottomPanel, BorderLayout.SOUTH);
    val innerBorder = new EmptyBorder(10,10,10,10);
    val outerBorder = BorderFactory.createSoftBevelBorder(BevelBorder.LOWERED);
    contentPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

    val commitRollbackButtonsPanel = new JPanel(new GridLayout(1,2));
    commitRollbackButtonsPanel.setBorder(new EmptyBorder(20,0,0,0));
    commitRollbackButtonsPanel.add(commitButton);
    commitRollbackButtonsPanel.add(rollbackButton);

    val rootPanel = new JPanel(new BorderLayout());
    rootPanel.setBorder(new EmptyBorder(10,10,10,10));
    rootPanel.add(contentPanel, BorderLayout.CENTER);
    rootPanel.add(commitRollbackButtonsPanel, BorderLayout.SOUTH);

    addButton.addActionListener(e -> {
      try {
        val currentLocation = locationField.getText().trim();
        if (currentLocation.isBlank()) {
          new InformationDialog("Enter location first!");
          return;
        }
        if (locationNames.contains(currentLocation)) {
          new InformationDialog("Entered location already exists in the database!");
          return;
        }

        DBHandler.addLocation(currentLocation);
        locationNames.add(currentLocation);
        locationsList.setListData(locationNames.toArray(new String[0]));

      } catch (Exception ex) {
        new InformationDialog("Something went wrong!");
      }
    });

    deleteButton.addActionListener(e -> {
      try {

        val selectedLocation = locationsList.getSelectedValue();
        if (selectedLocation == null || selectedLocation.trim().isEmpty()) {
          new InformationDialog("Location must be selected first!");
          return;
        }

        DBHandler.deleteLocation(selectedLocation);
        locationNames.remove(selectedLocation);
        locationsList.setListData(locationNames.toArray(new String[0]));

      } catch (Exception ex) {
        new InformationDialog("Something went wrong!");
      }
    });

    commitButton.addActionListener(e -> {
      DBConnection.commit();
      DBConnection.setAutocommit(true);
      referenceToThis.setVisible(false);
      referenceToThis.dispose();
    });

    rollbackButton.addActionListener(e -> {
      DBConnection.rollback();
      DBConnection.setAutocommit(true);
      referenceToThis.setVisible(false);
      referenceToThis.dispose();
    });

    this.addWindowListener(new WindowAdapter() {
      @Override
      public void windowClosed(WindowEvent e) {
        DBConnection.rollback();
        DBConnection.setAutocommit(true);
      }
    });

    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setTitle(Common.WINDOW_TITLE);
    this.setContentPane(rootPanel);
    this.pack();
    this.setResizable(false);
    this.setModal(true);
    this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
    this.setVisible(true);
  }
}
