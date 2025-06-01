// config/WebConfig.java
package com.guesthouse.wishboard.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/**")
            .allowedOriginPatterns("*")
            .allowedOrigins("http://localhost:8081") // 또는 "*" (개발용)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS","PATCH")
            .allowedHeaders("*")
            .allowCredentials(true);
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/uploaded/**")
            // 실제 업로드된 파일이 저장된 로컬 경로
            .addResourceLocations("file:C:/upload/")
            .setCachePeriod(3600);
  }
            .allowedOrigins("http://localhost:8081") // 또는 "*" (개발용)
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
            .allowedHeaders("*")
            .allowCredentials(true);
  }

}
