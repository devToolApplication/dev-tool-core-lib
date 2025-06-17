package vn.devTool.core.base.type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeignResponse<T> {
    private int status;
    private HttpHeaders headers;
    private T body;
    private String rawBody; // JSON thô
}
