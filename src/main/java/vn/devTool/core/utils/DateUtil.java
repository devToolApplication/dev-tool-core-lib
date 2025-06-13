package vn.devTool.core.utils;

import lombok.experimental.UtilityClass;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Utility class cung cấp các phương thức tiện ích liên quan đến thời gian.
 * Các phương thức này chủ yếu làm việc với các đối tượng LocalDate, LocalDateTime và mili giây (epoch millis).
 */
@UtilityClass
public class DateUtil {

  /**
   * Múi giờ UTC được sử dụng để chuyển đổi hoặc xử lý thời gian.
   */
  public static final ZoneId UTC_ZONE = ZoneId.of("UTC");

  /**
   * Lấy thời gian hiện tại tính bằng mili giây kể từ thời điểm epoch (1970-01-01T00:00:00Z).
   * Phương thức này trả về thời gian theo múi giờ UTC.
   *
   * @return Thời gian hiện tại tính bằng mili giây (ms) từ epoch (1970-01-01T00:00:00Z)
   */
  public static long getCurrentMillis() {
    return Instant.now().toEpochMilli();
  }

  /**
   * Lấy thời gian hiện tại tính bằng mili giây kể từ epoch (1970-01-01T00:00:00Z).
   * Phương thức này đảm bảo thời gian theo múi giờ UTC.
   *
   * @return Thời gian hiện tại tính bằng mili giây (ms) từ epoch (1970-01-01T00:00:00Z).
   */
  public static long getCurrentUtcMillis() {
    return ZonedDateTime.now(UTC_ZONE).toInstant().toEpochMilli();
  }

  /**
   * Lấy thời gian hiện tại tính bằng giây kể từ epoch (1970-01-01T00:00:00Z).
   * Phương thức này đảm bảo thời gian theo múi giờ UTC.
   *
   * @return Thời gian hiện tại tính bằng mili giây (ms) từ epoch (1970-01-01T00:00:00Z).
   */
  public static long getCurrentUtcSecond() {
    return ZonedDateTime.now(UTC_ZONE).toInstant().toEpochMilli() / 1000;
  }

  /**
   * Chuyển đổi thời gian từ mili giây (epoch millis) sang đối tượng {@link LocalDateTime} theo múi giờ UTC.
   *
   * @param millis Thời gian tính bằng mili giây từ epoch (1970-01-01T00:00:00Z)
   * @return Đối tượng {@link LocalDateTime} đại diện cho thời gian đó ở múi giờ UTC
   */
  public static LocalDateTime millisToLocalDateTime(long millis) {
    Instant instant = Instant.ofEpochMilli(millis);
    return instant.atZone(UTC_ZONE).toLocalDateTime();
  }

  /**
   * Chuyển đổi thời gian từ mili giây (epoch millis) sang đối tượng {@link LocalDate} theo múi giờ UTC.
   *
   * @param millis Thời gian tính bằng mili giây từ epoch (1970-01-01T00:00:00Z)
   * @return Đối tượng {@link LocalDate} đại diện cho thời gian đó ở múi giờ UTC
   */
  public static LocalDate millisToLocalDate(long millis) {
    Instant instant = Instant.ofEpochMilli(millis);
    return instant.atZone(UTC_ZONE).toLocalDate();
  }

  /**
   * Chuyển đổi một đối tượng {@link LocalDateTime} sang mili giây tính từ epoch (1970-01-01T00:00:00Z).
   *
   * @param dateTime Thời gian cần chuyển đổi sang mili giây
   * @return Thời gian tính bằng mili giây từ epoch
   */
  public static long localDateTimeToMillis(LocalDateTime dateTime) {
    return dateTime.atZone(UTC_ZONE).toInstant().toEpochMilli();
  }

  /**
   * Chuyển đổi một đối tượng {@link LocalDate} sang mili giây tính từ epoch (1970-01-01T00:00:00Z).
   *
   * @param date Thời gian cần chuyển đổi sang mili giây
   * @return Thời gian tính bằng mili giây từ epoch
   */
  public static long localDateToMillis(LocalDate date) {
    return date.atStartOfDay(UTC_ZONE).toInstant().toEpochMilli();
  }

  /**
   * Chuyển đổi một đối tượng {@link ZonedDateTime} sang mili giây tính từ epoch (1970-01-01T00:00:00Z).
   *
   * @param dateTime Thời gian cần chuyển đổi sang mili giây
   * @return Thời gian tính bằng mili giây từ epoch (UTC)
   */
  public static long zonedDateTimeToMillis(ZonedDateTime dateTime) {
    return dateTime.withZoneSameInstant(UTC_ZONE).toInstant().toEpochMilli();
  }

  public static long zonedDateTimeToSeconds(ZonedDateTime dateTime) {
    return dateTime.withZoneSameInstant(UTC_ZONE).toInstant().toEpochMilli() / 1000;
  }


  /**
   * Chuyển đổi một {@link LocalDateTime} sang chuỗi định dạng theo mẫu yyyy-MM-dd HH:mm:ss.
   *
   * @param dateTime Thời gian cần chuyển đổi
   * @return Chuỗi thời gian theo định dạng yyyy-MM-dd HH:mm:ss
   */
  public static String formatLocalDateTime(LocalDateTime dateTime) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return dateTime.format(formatter);
  }

  /**
   * Chuyển đổi một {@link LocalDate} sang chuỗi định dạng theo mẫu yyyy-MM-dd.
   *
   * @param date Thời gian cần chuyển đổi
   * @return Chuỗi thời gian theo định dạng yyyy-MM-dd
   */
  public static String formatLocalDate(LocalDate date) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return date.format(formatter);
  }

  /**
   * Chuyển đổi một chuỗi thời gian theo định dạng yyyy-MM-dd HH:mm:ss thành đối tượng {@link LocalDateTime}.
   *
   * @param dateTimeStr Chuỗi thời gian theo định dạng yyyy-MM-dd HH:mm:ss
   * @return Đối tượng {@link LocalDateTime} đại diện cho thời gian đã chuyển đổi
   */
  public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(dateTimeStr, formatter);
  }

  /**
   * Chuyển đổi một chuỗi thời gian theo định dạng yyyy-MM-dd thành đối tượng {@link LocalDate}.
   *
   * @param dateStr Chuỗi thời gian theo định dạng yyyy-MM-dd
   * @return Đối tượng {@link LocalDate} đại diện cho thời gian đã chuyển đổi
   */
  public static LocalDate parseLocalDate(String dateStr) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    return LocalDate.parse(dateStr, formatter);
  }

  /**
   * Tính toán khoảng cách thời gian giữa hai đối tượng {@link LocalDateTime}.
   *
   * @param start Thời gian bắt đầu
   * @param end Thời gian kết thúc
   * @return Khoảng cách thời gian tính bằng mili giây
   */
  public static long calculateDuration(LocalDateTime start, LocalDateTime end) {
    return ChronoUnit.MILLIS.between(start, end);
  }

  /**
   * Tính toán khoảng cách thời gian giữa hai đối tượng {@link LocalDate}.
   *
   * @param start Thời gian bắt đầu
   * @param end Thời gian kết thúc
   * @return Khoảng cách thời gian tính bằng ngày
   */
  public static long calculateDaysBetween(LocalDate start, LocalDate end) {
    return ChronoUnit.DAYS.between(start, end);
  }

  /**
   * Kiểm tra xem một {@link LocalDateTime} có phải là thời gian trong quá khứ hay không.
   *
   * @param dateTime Thời gian cần kiểm tra
   * @return true nếu thời gian là trong quá khứ, false nếu không
   */
  public static boolean isPast(LocalDateTime dateTime) {
    return dateTime.isBefore(LocalDateTime.now());
  }

  /**
   * Kiểm tra xem một {@link LocalDateTime} có phải là thời gian trong tương lai hay không.
   *
   * @param dateTime Thời gian cần kiểm tra
   * @return true nếu thời gian là trong tương lai, false nếu không
   */
  public static boolean isFuture(LocalDateTime dateTime) {
    return dateTime.isAfter(LocalDateTime.now());
  }

  /**
   * Lấy thời gian hiện tại ở múi giờ UTC.
   *
   * @return Đối tượng {@link LocalDateTime} đại diện cho thời gian hiện tại ở UTC
   */
  public static LocalDateTime getCurrentUtcDateTime() {
    return LocalDateTime.now(UTC_ZONE);
  }

  /**
   * Lấy thời gian hiện tại ở múi giờ hệ thống.
   *
   * @return Đối tượng {@link LocalDateTime} đại diện cho thời gian hiện tại ở múi giờ hệ thống
   */
  public static LocalDateTime getCurrentSystemDateTime() {
    return LocalDateTime.now();
  }

  /**
   * Chuyển đổi một {@link ZonedDateTime} sang chuỗi theo định dạng tùy chỉnh.
   *
   * @param dateTime Thời gian cần chuyển đổi
   * @param format Định dạng mong muốn, ví dụ: "yyyy-MM-dd HH:mm:ss"
   * @return Chuỗi thời gian theo định dạng đã cho
   */
  public static String formatZonedDateTime(ZonedDateTime dateTime, String format) {
    if (dateTime == null || format == null || format.isEmpty()) {
      return "N/A";
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
    return dateTime.format(formatter);
  }

}
