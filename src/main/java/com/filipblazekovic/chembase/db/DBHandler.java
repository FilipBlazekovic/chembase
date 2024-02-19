package com.filipblazekovic.chembase.db;

import com.filipblazekovic.chembase.model.internal.Chemical;
import com.filipblazekovic.chembase.model.shared.SearchByOption;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.SneakyThrows;
import lombok.val;

public class DBHandler {

  private static String LAST_QUERY_SQL = SQLStatements.INITIAL_SQL;

  private DBHandler() {
  }

  @SneakyThrows
  public static void constructTables() {
    try (val s = DBConnection.get().createStatement()) {
      s.execute(SQLStatements.TABLE_CREATE_LOCATIONS_SQL);
      s.execute(SQLStatements.TABLE_CREATE_CHEMICALS_SQL);
      s.execute(SQLStatements.CREATE_IX_CHEMICAL_NAME_SQL);
      s.execute(SQLStatements.CREATE_IX_BRUTTO_FORMULA_SQL);
      s.execute(SQLStatements.CREATE_IX_MOLAR_MASS_SQL);
      s.execute(SQLStatements.CREATE_IX_STORAGE_LOCATION_SQL);
      s.execute(SQLStatements.CREATE_IX_MANUFACTURER_SQL);
      s.execute(SQLStatements.CREATE_IX_SUPPLIER_SQL);
      s.execute(SQLStatements.CREATE_IX_STORAGE_LOCATION_NAME_SQL);
    }
  }

  @SneakyThrows
  public static void clearAll() {
    try (val s = DBConnection.get().createStatement()) {
      s.execute(SQLStatements.TABLE_TRUNCATE_LOCATIONS_SQL);
      s.execute(SQLStatements.TABLE_TRUNCATE_CHEMICALS_SQL);
    }
  }

  @SneakyThrows
  public static List<String> getLocations() {
    try (
        val s = DBConnection.get().prepareStatement(SQLStatements.GET_LOCATIONS_SQL);
        val r = s.executeQuery()
    ) {
      val ls = new ArrayList<String>();
      while (r.next()) {
        ls.add(r.getString(1));
      }
      return ls;
    }
  }

  @SneakyThrows
  public static void addLocation(String name) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.ADD_LOCATION_SQL)) {
      s.setString(1, name);
      s.execute();
    }
  }

  @SneakyThrows
  public static void deleteLocation(String name) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.DELETE_LOCATION_SQL)) {
      s.setString(1, name);
      s.execute();
    }
  }

  @SneakyThrows
  public static Optional<Chemical> getChemical(int id) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.VIEW_CHEMICAL_SQL)) {
      s.setInt(1, id);
      try (val r = s.executeQuery()) {
        if (r.next()) {
          return Optional.of(Chemical.from(r));
        }
        return Optional.empty();
      }
    }
  }

  @SneakyThrows
  public static List<Chemical> getChemicals() {
    try (
        val s = DBConnection.get().prepareStatement(LAST_QUERY_SQL);
        val r = s.executeQuery()
    ) {
      val cs = new ArrayList<Chemical>();
      while (r.next()) {
        cs.add(Chemical.from(r));
      }
      return cs;
    }
  }

  @SneakyThrows
  public static List<Chemical> getChemicals(String searchPhrase, SearchByOption searchBy, boolean regexSearchOn) {
    var sql = SQLStatements.CORE_SEARCH_SQL;
    if (!searchPhrase.isBlank()) {
      sql += constructSQLStatement(searchPhrase, searchBy, regexSearchOn);
    }
    sql += " ORDER BY x.id";
    LAST_QUERY_SQL = sql;
    try (
        val s = DBConnection.get().prepareStatement(sql);
        val r = s.executeQuery()
    ) {
      val cs = new ArrayList<Chemical>();
      while (r.next()) {
        cs.add(Chemical.from(r));
      }
      return cs;
    }
  }

  private static String constructSQLStatement(String searchPhrase, SearchByOption searchBy, boolean regexSearchOn) {
    return switch (searchBy) {
      case BRUTTO_FORMULA -> {
        searchPhrase = searchPhrase.toUpperCase();
        yield regexSearchOn
            ? (" WHERE x.brutto_formula LIKE '%" + searchPhrase + "%'")
            : (" WHERE x.brutto_formula = " + "'" + searchPhrase + "'");
      }
      case MOLAR_MASS -> {
        searchPhrase = searchPhrase.replaceAll(",", ".");
        yield (" WHERE x.molar_mass = " + searchPhrase);
      }
      case STORAGE_LOCATION -> regexSearchOn
          ? (" WHERE x.storage_location IN (SELECT id FROM locations WHERE name LIKE '%" + searchPhrase + "%')")
          : (" WHERE x.storage_location = (SELECT id FROM locations WHERE name = " + "'" + searchPhrase + "')");
      case MANUFACTURER -> regexSearchOn
          ? (" WHERE x.manufacturer LIKE '%" + searchPhrase + "%'")
          : (" WHERE x.manufacturer = " + "'" + searchPhrase + "'");
      case SUPPLIER -> regexSearchOn
          ? (" WHERE x.supplier LIKE '%" + searchPhrase + "%'")
          : (" WHERE x.supplier = " + "'" + searchPhrase + "'");
      default -> regexSearchOn
          ? (" WHERE x.chemical_name LIKE '%" + searchPhrase + "%'")
          : (" WHERE x.chemical_name = " + "'" + searchPhrase + "'");
    };
  }

  @SneakyThrows
  public static void addChemical(Chemical chemical) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.ADD_CHEMICAL_SQL)) {
      s.setString(1, chemical.name());
      s.setString(2, chemical.bruttoFormula());
      s.setDouble(3, chemical.molarMass());
      s.setDouble(4, chemical.amount());
      s.setString(5, chemical.unit().name());
      s.setString(6, chemical.storageLocation());
      s.setObject(7, chemical.manufacturer(), Types.VARCHAR);
      s.setObject(8, chemical.supplier(), Types.VARCHAR);
      s.setString(9, chemical.dateOfEntry().toString());
      s.setObject(10, chemical.additionalInfo(), Types.VARCHAR);
      s.execute();
    }
  }

  @SneakyThrows
  public static void editChemical(Chemical chemical) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.EDIT_CHEMICAL_SQL)) {
      s.setString(1, chemical.name());
      s.setString(2, chemical.bruttoFormula());
      s.setDouble(3, chemical.molarMass());
      s.setDouble(4, chemical.amount());
      s.setString(5, chemical.unit().name());
      s.setString(6, chemical.storageLocation());
      s.setObject(7, chemical.manufacturer(), Types.VARCHAR);
      s.setObject(8, chemical.supplier(), Types.VARCHAR);
      s.setString(9, chemical.dateOfEntry().toString());
      s.setObject(10, chemical.additionalInfo(), Types.VARCHAR);
      s.setInt(11, chemical.id());
      s.execute();
    }
  }

  @SneakyThrows
  public static void deleteChemical(int id) {
    try (val s = DBConnection.get().prepareStatement(SQLStatements.DELETE_CHEMICAL_SQL)) {
      s.setInt(1, id);
      s.execute();
    }
  }

}
