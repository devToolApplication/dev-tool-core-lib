package vn.devTool.core.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Value("${pageable.default.sort:createdAt,DESC}")
    private String defaultSort;

    @Value("${pageable.default.size:10}")
    private int defaultSize;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

        // Parse sort config (ví dụ: "createdAt,DESC")
        Sort sort = parseSort(defaultSort);

        // fallback = dùng khi người dùng KHÔNG truyền sort
        pageableResolver.setFallbackPageable(
                PageRequest.of(0, defaultSize, sort)
        );

        resolvers.add(pageableResolver);
    }

    private Sort parseSort(String sortConfig) {
        if (sortConfig == null || sortConfig.isBlank()) {
            return Sort.unsorted();
        }

        String[] parts = sortConfig.split(",");
        if (parts.length == 2) {
            return Sort.by(Sort.Direction.fromString(parts[1].trim()), parts[0].trim());
        } else {
            return Sort.by(parts[0].trim());
        }
    }
}
