package vn.devTool.core.base;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

/**
 * Lớp cơ sở cho tất cả các Document trong hệ thống, cung cấp thông tin về ngày tạo, ngày cập nhật và người thực hiện.
 */
@Getter
@Setter
public abstract class BaseEntity {

  /**
   * ID của document, sử dụng String thay vì ObjectId.
   */
  @Id
  @Field(targetType = FieldType.OBJECT_ID)
  private String id;

  /**
   * Ngày tạo document.
   * Được set tự động khi document được tạo.
   */
  @CreatedDate
  @Field(name = "created_at")
  private LocalDateTime createdAt;

  /**
   * Ngày cập nhật document.
   * Được cập nhật tự động mỗi khi document thay đổi.
   */
  @LastModifiedDate
  @Field(name = "updated_at")
  private LocalDateTime updatedAt;

  /**
   * Người tạo document.
   * Cần có AuditorAware để tự động set giá trị.
   */
  @CreatedBy
  @Field(name = "created_by")
  private String createdBy;

  /**
   * Người cập nhật document.
   * Cần có AuditorAware để tự động set giá trị.
   */
  @LastModifiedBy
  @Field(name = "updated_by")
  private String updatedBy;
}
