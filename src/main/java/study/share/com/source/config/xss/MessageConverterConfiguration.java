//package study.share.com.source.config.xss;
//
//import com.fasterxml.jackson.databind.DeserializationFeature;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.SerializationFeature;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.converter.HttpMessageConverter;
//import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
//
//import java.util.List;
//
//@Configuration
//public class MessageConverterConfiguration extends WebMvcConfigurationSupport {
//    /**
//     * MappingJackson2HttpMessageConverter 를 커스터마이징 하여 응답 객체 이스케이프 문자 설정
//     * @return 커스텀 설정이 적용된 컨버터
//     */
//    @Bean
//    public MessageConverterConfiguration controlTowerWebConfigurerAdapter() {
//        return new MessageConverterConfiguration() {
//
//            @Override
//            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//                super.configureMessageConverters(converters);
//
//                // 5. WebMvcConfigurerAdapter에 MessageConverter 추가
//                converters.add(htmlEscapingConveter());
//            }
//
//            private HttpMessageConverter<?> htmlEscapingConveter() {
//                ObjectMapper objectMapper = new ObjectMapper();
//                // 3. ObjectMapper에 특수 문자 처리 기능 적용
//                objectMapper.getFactory().setCharacterEscapes(new HTMLCharacterEscapes());
//
//                // 4. MessageConverter에 ObjectMapper 설정
//                MappingJackson2HttpMessageConverter htmlEscapingConverter =
//                        new MappingJackson2HttpMessageConverter();
//                htmlEscapingConverter.setObjectMapper(objectMapper);
//
//                return htmlEscapingConverter;
//            }
//        };
//    }
//}
