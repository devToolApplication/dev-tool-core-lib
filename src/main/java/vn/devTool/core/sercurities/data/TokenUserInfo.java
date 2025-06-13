package vn.devTool.core.sercurities.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.devTool.core.base.type.UserRole;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenUserInfo {
  private String username;
  private String userId;
  private Long managerId;
  private UserRole userRole;
}
