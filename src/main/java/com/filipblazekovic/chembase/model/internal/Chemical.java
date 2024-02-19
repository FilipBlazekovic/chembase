package com.filipblazekovic.chembase.model.internal;

import com.filipblazekovic.chembase.model.shared.Unit;
import java.sql.ResultSet;
import java.time.LocalDate;
import lombok.SneakyThrows;

public record Chemical(
    Integer id,
    String name,
    String bruttoFormula,
    Double molarMass,
    Double amount,
    Unit unit,
    String storageLocation,
    String manufacturer,
    String supplier,
    LocalDate dateOfEntry,
    String additionalInfo
) {

  @SneakyThrows
  public static Chemical from(ResultSet r) {
    return new Chemical(
        r.getInt(1),
        r.getString(2),
        r.getString(3),
        r.getDouble(4),
        r.getDouble(5),
        Unit.from(r.getString(6)),
        r.getString(7),
        r.getString(8),
        r.getString(9),
        LocalDate.parse(r.getString(10)),
        r.getString(11)
    );
  }

  public Chemical merge(Chemical c) {
    return new Chemical(
        id,
        c.name,
        c.bruttoFormula,
        c.molarMass,
        c.amount,
        c.unit,
        c.storageLocation,
        c.manufacturer,
        c.supplier,
        c.dateOfEntry,
        c.additionalInfo
    );
  }

}
