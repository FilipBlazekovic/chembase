package com.filipblazekovic.chembase.util;

import java.awt.Cursor;
import java.awt.Insets;
import java.io.File;

public class Common {

  private Common() {
  }

  public static final Cursor CURSOR = new Cursor(Cursor.HAND_CURSOR);

  public static final Insets MARGIN = new Insets(5, 0, 5, 0);

  public static final String PROJECT_REPO_URL = "https://github.com/FilipBlazekovic/chembase";

  public static final String WINDOW_TITLE = "ChemBase";

  public static final String DB_PATH = System.getProperty("user.home") + File.separator + ".chembase" + File.separator + "chembase.db";

  public static void generateProjectDirectory() {
    new File(System.getProperty("user.home") + File.separator + ".chembase").mkdirs();
  }

  public static boolean getDBExists() {
    return new File(DB_PATH).exists();
  }

}
