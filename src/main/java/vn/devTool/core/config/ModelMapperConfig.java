package vn.devTool.core.config;

import org.modelmapper.AbstractConverter;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class ModelMapperConfig {

    private final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // ✅ Only map non-null fields (avoid overriding with null values)
        modelMapper.getConfiguration()
                .setPropertyCondition(Conditions.isNotNull())
                .setMatchingStrategy(MatchingStrategies.STRICT) // Strict field matching
                .setFieldMatchingEnabled(true) // Enable field matching by name
                .setSkipNullEnabled(true); // Skip null values during mapping

        Converter<LocalDateTime, String> localDateTimeToStringConverter = new AbstractConverter<>() {
            @Override
            protected String convert(LocalDateTime source) {
                return source != null ? source.format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : null;
            }
        };

        Converter<String, LocalDateTime> stringToLocalDateTimeConverter = new AbstractConverter<>() {
            @Override
            protected LocalDateTime convert(String source) {
                return source != null ? LocalDateTime.parse(source, DateTimeFormatter.ofPattern(DATE_TIME_PATTERN)) : null;
            }
        };

        modelMapper.addConverter(localDateTimeToStringConverter);
        modelMapper.addConverter(stringToLocalDateTimeConverter);

        return modelMapper;
    }
}
