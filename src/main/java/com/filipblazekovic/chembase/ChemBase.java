package com.filipblazekovic.chembase;

import com.filipblazekovic.chembase.db.DBConnection;
import com.filipblazekovic.chembase.db.DBHandler;
import com.filipblazekovic.chembase.gui.AddEditDialog;
import com.filipblazekovic.chembase.gui.InformationDialog;
import com.filipblazekovic.chembase.gui.LocationsDialog;
import com.filipblazekovic.chembase.gui.ViewDialog;
import com.filipblazekovic.chembase.listener.ExportListener;
import com.filipblazekovic.chembase.listener.SearchListener;
import com.filipblazekovic.chembase.model.shared.ExportFormat;
import com.filipblazekovic.chembase.model.shared.SearchByOption;
import com.filipblazekovic.chembase.util.Common;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import lombok.val;

public class ChemBase extends JFrame {

  private static ChemBase chemBase;

  private static final String[] TABLE_COLUMN_NAMES = {
      "ID",
      "Chemical Name",
      "Brutto Formula",
      "Molar Mass",
      "Quantity",
      "Unit",
      "Storage Location"
  };

  private final DefaultTableModel tableModel;
  private final JTable table;

  private final JTextField searchField;

  private final JRadioButton nameRadioButton;
  private final JRadioButton bruttoFormulaRadioButton;
  private final JRadioButton molarMassRadioButton;
  private final JRadioButton storageLocationRadioButton;
  private final JRadioButton manufacturerRadioButton;
  private final JRadioButton supplierRadioButton;

  private final JCheckBoxMenuItem searchWithRegexOn;
  private final JCheckBoxMenuItem exportFullData;

  private ChemBase() {

    searchField = new JTextField(40);
    val searchButton = new JButton(" SEARCH ");

    searchField.setMargin(Common.MARGIN);
    searchButton.setMargin(Common.MARGIN);
    searchButton.setCursor(Common.CURSOR);

    val viewEntryButton = new JButton("VIEW ENTRY");
    val addEntryButton = new JButton("ADD ENTRY");
    val editEntryButton = new JButton("EDIT ENTRY");
    val removeEntryButton = new JButton("REMOVE ENTRY");

    viewEntryButton.setCursor(Common.CURSOR);
    addEntryButton.setCursor(Common.CURSOR);
    editEntryButton.setCursor(Common.CURSOR);
    removeEntryButton.setCursor(Common.CURSOR);

    val searchOptionsLabel = new JLabel("Search options: ");
    val border = new EmptyBorder(10, 3, 20, 0);
    searchOptionsLabel.setBorder(border);
    val font = searchOptionsLabel.getFont();
    searchOptionsLabel.setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));

    nameRadioButton = new JRadioButton("Search by name");
    bruttoFormulaRadioButton = new JRadioButton("Search by brutto formula");
    molarMassRadioButton = new JRadioButton("Search by molar mass");
    storageLocationRadioButton = new JRadioButton("Search by storage location");
    manufacturerRadioButton = new JRadioButton("Search by manufacturer");
    supplierRadioButton = new JRadioButton("Search by supplier");

    nameRadioButton.setCursor(Common.CURSOR);
    bruttoFormulaRadioButton.setCursor(Common.CURSOR);
    molarMassRadioButton.setCursor(Common.CURSOR);
    storageLocationRadioButton.setCursor(Common.CURSOR);
    manufacturerRadioButton.setCursor(Common.CURSOR);
    supplierRadioButton.setCursor(Common.CURSOR);

    val buttonGroup = new ButtonGroup();
    buttonGroup.add(nameRadioButton);
    buttonGroup.add(bruttoFormulaRadioButton);
    buttonGroup.add(molarMassRadioButton);
    buttonGroup.add(storageLocationRadioButton);
    buttonGroup.add(manufacturerRadioButton);
    buttonGroup.add(supplierRadioButton);
    nameRadioButton.setSelected(true);

    val exportAsTxt = new JMenuItem("Export as TXT");
    val exportAsCsv = new JMenuItem("Export as CSV");
    val exportAsJson = new JMenuItem("Export as JSON");
    val exportAsXlsx = new JMenuItem("Export as XLSX");
    val exportAsPdf = new JMenuItem("Export as PDF");
    val clearDatabase = new JMenuItem("Clear database");
    val about = new JMenuItem("About");
    val exit = new JMenuItem("Exit");

    exportAsTxt.setCursor(Common.CURSOR);
    exportAsCsv.setCursor(Common.CURSOR);
    exportAsJson.setCursor(Common.CURSOR);
    exportAsXlsx.setCursor(Common.CURSOR);
    exportAsPdf.setCursor(Common.CURSOR);
    clearDatabase.setCursor(Common.CURSOR);
    about.setCursor(Common.CURSOR);
    exit.setCursor(Common.CURSOR);

    exportAsTxt.setMargin(Common.MARGIN);
    exportAsCsv.setMargin(Common.MARGIN);
    exportAsJson.setMargin(Common.MARGIN);
    exportAsXlsx.setMargin(Common.MARGIN);
    exportAsPdf.setMargin(Common.MARGIN);
    clearDatabase.setMargin(Common.MARGIN);
    about.setMargin(Common.MARGIN);
    exit.setMargin(Common.MARGIN);

    val setupLocations = new JMenuItem("Setup locations");
    searchWithRegexOn = new JCheckBoxMenuItem("Search with regex ON/OFF");
    exportFullData = new JCheckBoxMenuItem("Export full data ON/OFF");

    setupLocations.setCursor(Common.CURSOR);
    searchWithRegexOn.setCursor(Common.CURSOR);
    exportFullData.setCursor(Common.CURSOR);

    searchWithRegexOn.setSelected(true);
    exportFullData.setSelected(true);

    setupLocations.setMargin(Common.MARGIN);
    searchWithRegexOn.setMargin(Common.MARGIN);
    exportFullData.setMargin(Common.MARGIN);

    val fileMenu = new JMenu("File");
    fileMenu.setCursor(Common.CURSOR);
    fileMenu.add(exportAsTxt);
    fileMenu.add(exportAsCsv);
    fileMenu.add(exportAsJson);
    fileMenu.add(exportAsXlsx);
    fileMenu.add(exportAsPdf);
    fileMenu.add(new JSeparator());
    fileMenu.add(clearDatabase);
    fileMenu.add(new JSeparator());
    fileMenu.add(about);
    fileMenu.add(new JSeparator());
    fileMenu.add(exit);

    val settingsMenu = new JMenu("Settings");
    settingsMenu.setCursor(Common.CURSOR);
    settingsMenu.add(setupLocations);
    settingsMenu.add(new JSeparator());
    settingsMenu.add(searchWithRegexOn);
    settingsMenu.add(exportFullData);

    val menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(settingsMenu);

    menuBar.setMargin(Common.MARGIN);

    val mainPanel = new JPanel(new BorderLayout());
    val subPanel = new JPanel(new BorderLayout());
    val searchPanel = new JPanel(new FlowLayout());
    val searchOptionsPanel = new JPanel(new GridLayout(7, 1));
    val buttonsPanel = new JPanel(new GridLayout(4, 1));

    searchPanel.add(searchField);
    searchPanel.add(searchButton);

    searchOptionsPanel.add(searchOptionsLabel);
    searchOptionsPanel.add(nameRadioButton);
    searchOptionsPanel.add(bruttoFormulaRadioButton);
    searchOptionsPanel.add(molarMassRadioButton);
    searchOptionsPanel.add(storageLocationRadioButton);
    searchOptionsPanel.add(manufacturerRadioButton);
    searchOptionsPanel.add(supplierRadioButton);

    buttonsPanel.add(viewEntryButton);
    buttonsPanel.add(addEntryButton);
    buttonsPanel.add(editEntryButton);
    buttonsPanel.add(removeEntryButton);

    subPanel.add(searchOptionsPanel, BorderLayout.WEST);
    subPanel.add(buttonsPanel, BorderLayout.EAST);

    val cellRenderer = new DefaultTableCellRenderer();
    cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);

    tableModel = new DefaultTableModel(null, TABLE_COLUMN_NAMES);
    table = new JTable(tableModel);
    for (int i = 0; i <= 5; i++) {
      table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
    }

    val headerRenderer = table.getTableHeader().getDefaultRenderer();
    val headerLabel = (JLabel) headerRenderer;
    headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

    table.setRowHeight(30);
    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    table.setRowSelectionAllowed(true);

    val tablePanel = new JScrollPane(
        table,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS
    );

    mainPanel.add(searchPanel, BorderLayout.NORTH);
    mainPanel.add(subPanel, BorderLayout.CENTER);
    mainPanel.add(tablePanel, BorderLayout.SOUTH);

    tableModel.setRowCount(0);
    DBHandler.getChemicals().forEach(c -> tableModel.addRow(
            new Object[]{
                c.id(),
                c.name(),
                c.bruttoFormula(),
                c.molarMass(),
                c.amount(),
                c.unit().name(),
                c.storageLocation()
            }
        )
    );
    table.doLayout();

    setupLocations.addActionListener(e -> new LocationsDialog());

    exportAsTxt.addActionListener(new ExportListener(ExportFormat.TXT));
    exportAsCsv.addActionListener(new ExportListener(ExportFormat.CSV));
    exportAsJson.addActionListener(new ExportListener(ExportFormat.JSON));
    exportAsXlsx.addActionListener(new ExportListener(ExportFormat.XLSX));
    exportAsPdf.addActionListener(new ExportListener(ExportFormat.PDF));

    clearDatabase.addActionListener(e -> DBHandler.clearAll());
    about.addActionListener(e -> new InformationDialog(Common.PROJECT_REPO_URL));
    exit.addActionListener(e -> {
      DBConnection.close();
      System.exit(0);
    });

    val searchListener = new SearchListener(table, tableModel);
    searchField.addActionListener(searchListener);
    searchButton.addActionListener(searchListener);

    viewEntryButton.addActionListener(e -> {
      val id = getSelectedChemicalId();
      if (id == -1) {
        new InformationDialog("Row must be selected first!");
        return;
      }
      new ViewDialog(id);
    });

    addEntryButton.addActionListener(e -> new AddEditDialog(null));

    editEntryButton.addActionListener(e -> {
      val id = getSelectedChemicalId();
      if (id == -1) {
        new InformationDialog("Row must be selected first!");
        return;
      }
      new AddEditDialog(id);
    });

    removeEntryButton.addActionListener(e -> {
      val id = getSelectedChemicalId();
      if (id == -1) {
        new InformationDialog("Row must be selected first!");
        return;
      }
      int selectedOption = JOptionPane.showConfirmDialog(
          chemBase,
          "Are you sure you want to remove the selected record?",
          Common.WINDOW_TITLE,
          JOptionPane.YES_NO_OPTION
      );
      if (selectedOption == JOptionPane.YES_OPTION) {
        DBHandler.deleteChemical(id);
        refresh();
      }
    });

    this.setJMenuBar(menuBar);
    this.add(mainPanel);
    this.setTitle(Common.WINDOW_TITLE);
    this.setSize(1200, 750);
    this.setResizable(false);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setVisible(true);
  }

  public static void main(String[] args) {
    try {
      if (!Common.getDBExists()) {
        Common.generateProjectDirectory();
        DBHandler.constructTables();
      }
      setupUIProperties();
      UIManager.setLookAndFeel(new FlatLightLaf());
    }
    catch (Exception ignored) {
    }
    SwingUtilities.invokeLater(() -> chemBase = new ChemBase());
  }

  private static void setupUIProperties() {
    UIManager.put("ScrollBar.width", 16);
    UIManager.put("Component.arrowType", "triangle");
    UIManager.put("Button.arc", 5);
    UIManager.put("Component.arc", 5);
    UIManager.put("ProgressBar.arc", 5);
    UIManager.put("TextComponent.arc", 5);
  }

  public static void refresh() {
    chemBase.tableModel.setRowCount(0);
    DBHandler.getChemicals().forEach(c -> chemBase.tableModel.addRow(
            new Object[]{
                c.id(),
                c.name(),
                c.bruttoFormula(),
                c.molarMass(),
                c.amount(),
                c.unit().name(),
                c.storageLocation()
            }
        )
    );
    chemBase.table.doLayout();
  }

  public static String getSearchPhrase() {
    return chemBase.searchField.getText().trim();
  }

  public static int getSelectedChemicalId() {
    val selectedRow = chemBase.table.getSelectedRow();
    return (selectedRow == -1)
        ? selectedRow
        : (Integer) chemBase.table.getValueAt(selectedRow, 0);
  }

  public static SearchByOption getSelectedSearchByOption() {
    if (chemBase.nameRadioButton.isSelected()) {
      return SearchByOption.NAME;
    }
    if (chemBase.bruttoFormulaRadioButton.isSelected()) {
      return SearchByOption.BRUTTO_FORMULA;
    }
    if (chemBase.molarMassRadioButton.isSelected()) {
      return SearchByOption.MOLAR_MASS;
    }
    if (chemBase.storageLocationRadioButton.isSelected()) {
      return SearchByOption.STORAGE_LOCATION;
    }
    if (chemBase.manufacturerRadioButton.isSelected()) {
      return SearchByOption.MANUFACTURER;
    }
    return SearchByOption.SUPPLIER;
  }

  public static boolean isRegexSearchOn() {
    return chemBase.searchWithRegexOn.isSelected();
  }

  public static boolean isFullExportOn() {
    return chemBase.exportFullData.isSelected();
  }

  public static ChemBase getChemBaseRoot() {
    return chemBase;
  }
}
