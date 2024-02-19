package com.filipblazekovic.chembase.gui;

import com.filipblazekovic.chembase.ChemBase;
import com.filipblazekovic.chembase.util.Common;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import lombok.val;

public class InformationDialog extends JDialog {

  private final InformationDialog referenceToThis;

  public InformationDialog(String message) {
    referenceToThis = this;

    val rootPanel = new JPanel(new BorderLayout());
    val label = new JLabel("<html>" + message + "</html>", SwingConstants.CENTER);
    val okButton = new JButton("OK");

    okButton.setCursor(Common.CURSOR);
    okButton.addActionListener(e -> {
      referenceToThis.setVisible(false);
      referenceToThis.dispose();
    });

    okButton.setPreferredSize(new Dimension(30, 30));
    okButton.setMaximumSize(new Dimension(50, 30));
    rootPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
    rootPanel.add(label, BorderLayout.CENTER);
    rootPane.add(new JLabel(), BorderLayout.CENTER);
    rootPanel.add(okButton, BorderLayout.SOUTH);

    this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    this.setTitle(Common.WINDOW_TITLE);
    this.setContentPane(rootPanel);
    this.setSize(370, 150);
    this.setModal(true);
    this.setLocationRelativeTo(ChemBase.getChemBaseRoot());
    this.setVisible(true);
  }

}
