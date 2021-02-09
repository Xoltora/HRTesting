package uz.bdm.HrTesting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfigurer implements WebMvcConfigurer {

    private final long MAX_AGE_SECS = 3600;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE")
                .maxAge(MAX_AGE_SECS);
    }
//    @Bean
//    public CorsFilter ☻corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        List<String> strings = Arrays.asList("*");
//        config.setAllowedHeaders(strings);
//        config.setAllowedMethods(Arrays.asList("HEAD", "OPTIONS", "GET", "POST", "PUT", "PATCH", "DELETE"));
//        config.setAllowCredentials(true);
//
//        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
////            log.debug("Registering CORS filter");
//            source.registerCorsConfiguration("/**", config);
////            source.registerCorsConfiguration("/management/**", config);
////            source.registerCorsConfiguration("/v2/api-docs", config);
//        }
//        return new CorsFilter(source);
//    }

}
