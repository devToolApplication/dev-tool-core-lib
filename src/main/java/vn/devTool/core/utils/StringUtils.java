package vn.devTool.core.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public class StringUtils {

  public static String extractCurrencyPair(String symbol) {
    return symbol.replaceAll("/", "");
  }

  public static boolean isBlank(String url) {
    return org.apache.commons.lang3.StringUtils.isBlank(url);
  }
}
