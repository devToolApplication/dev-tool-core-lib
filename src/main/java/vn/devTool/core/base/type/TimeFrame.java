package vn.devTool.core.base.type;

import lombok.Getter;

import java.time.Duration;
import java.util.Arrays;

@Getter
public enum TimeFrame {
    M1("1m", Duration.ofMinutes(1)),
    M5("5m", Duration.ofMinutes(5)),
    M15("15m", Duration.ofMinutes(15)),
    H1("1h", Duration.ofHours(1)),
    H4("4h", Duration.ofHours(4)),
    D1("1d", Duration.ofDays(1))
    ;

    private final String interval;
    private final Duration duration;

    TimeFrame(String interval, Duration duration) {
        this.interval = interval;
        this.duration = duration;
    }

    /**
     * Chuyển từ chuỗi (ví dụ: "5m", "15m") sang Enum TimeFrame.
     *
     * @param interval Chuỗi khung thời gian
     * @return TimeFrame tương ứng, hoặc throw IllegalArgumentException nếu không hợp lệ
     */
    public static TimeFrame fromInterval(String interval) {
        return Arrays.stream(values())
            .filter(tf -> tf.getInterval().equals(interval))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("Khung thời gian không hợp lệ: " + interval));
    }

    public long toMinutes() {
        return duration.toMinutes();
    }
}
