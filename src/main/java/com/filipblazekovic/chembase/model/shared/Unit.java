package com.filipblazekovic.chembase.model.shared;

public enum Unit {

  g,
  kg,
  ml,
  l,
  cm3,
  dm3,
  mol,
  mmol,
  unknown;

  public static Unit from(String unit) {
    try {
      return Unit.valueOf(unit);
    } catch (Exception e) {
      return unknown;
    }
  }

}
