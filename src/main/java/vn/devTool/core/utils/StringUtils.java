package vn.devTool.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static String extractCurrencyPair(String symbol) {
    return symbol.replaceAll("/", "");
  }
}
