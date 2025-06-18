package vn.devTool.core.utils;

import feign.Response;
import lombok.SneakyThrows;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MapperUtil {
    private final ModelMapper modelMapper;

    public MapperUtil(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * Converts an object from one type to another.
     */
    public <T, V> V map(T source, Class<V> targetClass) {
        return modelMapper.map(source, targetClass);
    }

    /**
     * Map từ source → destination (cập nhật object có sẵn)
     */
    public <S, D> void mapTo(S source, D destination) {
        modelMapper.map(source, destination);
    }

    /**
     * Converts a List<T> to List<V>.
     */
    public <T, V> List<V> mapList(List<T> sourceList, Class<V> targetClass) {
        return sourceList.stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    /**
     * Converts a Page<T> to Page<V>.
     */
    public <T, V> Page<V> mapPage(Page<T> sourcePage, Class<V> targetClass) {
        if (sourcePage == null) {
            return new PageImpl<>(Collections.emptyList(), Pageable.unpaged(), 0);
        } else {
          sourcePage.getContent();
        }

      List<V> mappedList = sourcePage.getContent().stream()
            .map(element -> modelMapper.map(element, targetClass))
            .collect(Collectors.toList());

        return new PageImpl<>(mappedList, sourcePage.getPageable(), sourcePage.getTotalElements());
    }


    /**
     * Converts a Feign Response to target object using Jackson.
     */
    @SneakyThrows
    public <T> T toResponseBody(Response response, Class<T> clazz) {
        if (response.body() == null) {
            return null;
        }

        try (InputStream inputStream = response.body().asInputStream()) {
            return JsonUtils.fromInputStreamJson(inputStream, clazz);
        }
    }
}
