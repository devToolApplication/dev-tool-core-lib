package vn.devTool.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasePageResponse<T> {

    private List<T> data;
    private PageMetadata metadata;

    public static <T> BasePageResponse<T> fromPage(Page<T> page) {
        return BasePageResponse.<T>builder()
            .data(page.getContent())
            .metadata(PageMetadata.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .size(page.getSize())
                .currentPage(page.getNumber())
                .build())
            .build();
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageMetadata {
        private int totalPages;
        private long totalElements;
        private int size;
        private int currentPage;
    }
}
