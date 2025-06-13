package vn.devTool.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import vn.devTool.core.base.type.BusinessErrorCode;
import vn.devTool.core.exceptions.BusinessException;

import java.util.Collections;
import java.util.List;

@Log4j2
public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper()
        .registerModule(new JavaTimeModule())
        .setSerializationInclusion(JsonInclude.Include.NON_NULL)
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * 📝 Convert Object → JSON String
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("❌ Error converting object to JSON: {}", object, e);
            return null;
        }
    }

    /**
     * 📝 Convert Object → JSON String
     */
    public static String toExactJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new BusinessException(BusinessErrorCode.JSON_PARSE_ERROR, e);
        }
    }

    /**
     * 🔍 Convert JSON String → Object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            log.error("❌ Error converting JSON to object: {}", json, e);
            return null;
        }
    }

    /**
     * 📜 Convert JSON String → List<T>
     */
    public static <T> List<T> fromJsonToList(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
        } catch (Exception e) {
            log.error("❌ Error converting JSON to List: {}", json, e);
            return Collections.emptyList();
        }
    }

    /**
     * 📑 Convert JSON String → Page<T>
     */
    public static <T> Page<T> fromJsonToPage(String json, Class<T> clazz) {
        try {
            // Sử dụng TypeReference để đọc danh sách data
            TypeReference<PageImpl<T>> typeRef = new TypeReference<>() {};
            PageImpl<T> pageData = objectMapper.readValue(json, typeRef);

            // Tạo Page mới với PageRequest để đảm bảo tính toàn vẹn
            return new PageImpl<>(pageData.getContent(), PageRequest.of(pageData.getNumber(), pageData.getSize()), pageData.getTotalElements());
        } catch (Exception e) {
            log.error("❌ Error converting JSON to Page: {}", json, e);
            return Page.empty();
        }
    }
}
