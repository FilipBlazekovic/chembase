package com.filipblazekovic.chembase.db;

public class SQLStatements {

  private SQLStatements() {
  }

  static final String TABLE_TRUNCATE_LOCATIONS_SQL = "DELETE FROM locations";
  static final String TABLE_TRUNCATE_CHEMICALS_SQL = "DELETE FROM chemicals";

  static final String TABLE_CREATE_LOCATIONS_SQL = "CREATE TABLE locations(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT)";
  static final String TABLE_CREATE_CHEMICALS_SQL = """
      CREATE TABLE chemicals(
      id INTEGER PRIMARY KEY AUTOINCREMENT,
      chemical_name TEXT,
      brutto_formula TEXT,
      molar_mass REAL,
      quantity_amount REAL,
      quantity_unit TEXT,
      storage_location INTEGER,
      manufacturer TEXT,
      supplier TEXT,
      date_of_entry TEXT,
      additional_info TEXT,
      FOREIGN KEY(storage_location) REFERENCES locations(id))
      """;

  static final String CREATE_IX_CHEMICAL_NAME_SQL = "CREATE INDEX ix_chemical_name ON chemicals(chemical_name)";
  static final String CREATE_IX_BRUTTO_FORMULA_SQL = "CREATE INDEX ix_brutto_formula ON chemicals(brutto_formula)";
  static final String CREATE_IX_MOLAR_MASS_SQL = "CREATE INDEX ix_molar_mass ON chemicals(molar_mass)";
  static final String CREATE_IX_STORAGE_LOCATION_SQL = "CREATE INDEX ix_storage_location ON chemicals(storage_location)";
  static final String CREATE_IX_MANUFACTURER_SQL = "CREATE INDEX ix_manufacturer ON chemicals(manufacturer)";
  static final String CREATE_IX_SUPPLIER_SQL = "CREATE INDEX ix_supplier ON chemicals(supplier)";
  static final String CREATE_IX_STORAGE_LOCATION_NAME_SQL = "CREATE INDEX ix_storage_location_name ON locations(name)";

  static final String GET_LOCATIONS_SQL = "SELECT name FROM locations ORDER BY name";
  static final String ADD_LOCATION_SQL = "INSERT INTO locations(name) VALUES (?)";
  static final String DELETE_LOCATION_SQL = "DELETE FROM locations WHERE name = ?";

  static final String INITIAL_SQL = """
      SELECT x.id,
      x.chemical_name,
      x.brutto_formula,
      x.molar_mass,
      x.quantity_amount,
      x.quantity_unit,
      y.name,
      x.manufacturer,
      x.supplier,
      x.date_of_entry,
      x.additional_info FROM chemicals x INNER JOIN locations y ON x.storage_location = y.id ORDER BY x.id
      """;

  static final String CORE_SEARCH_SQL = """
      SELECT
      x.id,
      x.chemical_name,
      x.brutto_formula,
      x.molar_mass,
      x.quantity_amount,
      x.quantity_unit,
      y.name,
      x.manufacturer,
      x.supplier,
      x.date_of_entry,
      x.additional_info FROM chemicals x INNER JOIN locations y ON x.storage_location = y.id
      """;

  static final String VIEW_CHEMICAL_SQL = """
      SELECT
      x.id,
      x.chemical_name,
      x.brutto_formula,
      x.molar_mass,
      x.quantity_amount,
      x.quantity_unit,
      y.name,
      x.manufacturer,
      x.supplier,
      x.date_of_entry,
      x.additional_info FROM chemicals x INNER JOIN locations y ON x.storage_location = y.id WHERE x.id = ?
      """;

  static final String DELETE_CHEMICAL_SQL = "DELETE FROM chemicals WHERE id = ?";

  static final String ADD_CHEMICAL_SQL = """
      INSERT INTO chemicals(
      chemical_name,
      brutto_formula,
      molar_mass,
      quantity_amount,
      quantity_unit,
      storage_location,
      manufacturer,
      supplier,
      date_of_entry,
      additional_info) VALUES (?,?,?,?,?,(SELECT id FROM locations WHERE name = ?),?,?,?,?)
      """;

  static final String EDIT_CHEMICAL_SQL = """
      UPDATE chemicals SET chemical_name = ?,
      brutto_formula = ?,
      molar_mass = ?,
      quantity_amount = ?,
      quantity_unit = ?,
      storage_location = (SELECT id FROM locations WHERE name = ?),
      manufacturer = ?,
      supplier = ?,
      date_of_entry = ?,
      additional_info = ? WHERE id = ?
      """;

}
