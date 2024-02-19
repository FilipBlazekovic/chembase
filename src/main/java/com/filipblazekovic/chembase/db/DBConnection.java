package com.filipblazekovic.chembase.db;

import com.filipblazekovic.chembase.util.Common;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import lombok.SneakyThrows;

public class DBConnection {

  private static Connection connection = null;

  private DBConnection() {
  }

  @SneakyThrows
  public static Connection get() {
    if (connection == null) {
      Class.forName("org.sqlite.JDBC");
      connection = DriverManager.getConnection("jdbc:sqlite:" + Common.DB_PATH);
      connection.setAutoCommit(true);
    }
    return connection;
  }

  @SneakyThrows
  public static void close() {
    if (connection != null) {
      connection.close();
    }
  }

  public static void setAutocommit(boolean autocommit) {
    try {
      connection.setAutoCommit(autocommit);
    } catch (Exception ignored) {
    }
  }

  public static void commit() {
    try {
      connection.commit();
    } catch (SQLException ignored) {
    }
  }

  public static void rollback() {
    try {
      connection.rollback();
    } catch (SQLException ignored) {
    }
  }

}