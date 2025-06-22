package vn.devTool.core.utils;

import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ListUtil {

  public static boolean isEmpty(List list){
    return list == null || list.isEmpty();
  }
}
