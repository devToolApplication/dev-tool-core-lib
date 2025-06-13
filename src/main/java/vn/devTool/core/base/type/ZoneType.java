package vn.devTool.core.base.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ZoneType {
    SUPPORT("Support Zone", "rgba(34, 177, 76, 0.3)", "rgba(0, 100, 0, 1)"),         // Xanh lá cây sáng hơn - Vùng hỗ trợ
    RESISTANCE("Resistance Zone", "rgba(237, 28, 36, 0.3)", "rgba(139, 0, 0, 1)"),   // Đỏ đậm - Vùng kháng cự
    BULLISH_ORDER_BLOCK("Bull OB", "rgba(255, 165, 0, 0.3)", "rgba(180, 100, 0, 1)"),// Cam đậm - OB tăng giá
    BEARISH_ORDER_BLOCK("Bear OB", "rgba(160, 82, 45, 0.3)", "rgba(120, 50, 20, 1)"),// Nâu đất - OB giảm giá
    FVG("Fair Value Gap", "rgba(128, 0, 128, 0.3)", "rgba(75, 0, 130, 1)"),          // Tím đậm - Khoảng trống giá trị hợp lý
    BREAKER_BLOCK("Breaker Block", "rgba(0, 112, 255, 0.3)", "rgba(0, 0, 139, 1)"),  // Xanh dương đậm - Khối phá vỡ

    TAKE_PROFIT("Take Profit", "rgba(0, 255, 127, 0.4)", "rgba(0, 128, 0, 1)"),      // Màu xanh lá tươi - Take Profit
    STOP_LOSS("Stop Loss", "rgba(255, 69, 0, 0.4)", "rgba(139, 0, 0, 1)");           // Màu đỏ cam đậm - Stop Loss

    private final String name;
    private final String backgroundColor;
    private final String textColor;
}
