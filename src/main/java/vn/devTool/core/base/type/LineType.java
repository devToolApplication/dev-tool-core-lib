package vn.devTool.core.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LineType {
  BOS("Break of Structure", "rgba(255, 0, 0, 1.0)"),        // Đỏ (Bearish)
  CHOCH("Change of Character", "rgba(0, 255, 0, 1.0)"),     // Xanh lá (Bullish)
  IDM("Internal Daily Marker", "rgba(0, 0, 255, 1.0)"),     // Xanh dương
  TOP("Top", "rgba(255, 165, 0, 1.0)"),                     // Cam
  BOTTOM("Bottom", "rgba(0, 191, 255, 1.0)"),               // Xanh da trời
  EQH("Equal High", "rgba(255, 99, 71, 1.0)"),              // Đỏ cam sáng
  EQL("Equal Low", "rgba(50, 205, 50, 1.0)"),               // Xanh lá tươi
  STRONG_HIGH("Strong High", "rgba(178, 34, 34, 1.0)"),     // Đỏ đậm
  WEAK_LOW("Weak Low", "rgba(34, 139, 34, 1.0)"),           // Xanh đậm

  STRONG_TOP("Strong Top", "rgba(255, 69, 0, 1.0)"),        // Cam đỏ
  STRONG_BOTTOM("Strong Bottom", "rgba(30, 144, 255, 1.0)"); // Xanh dương sáng

  private final String name;
  private final String color;
}
