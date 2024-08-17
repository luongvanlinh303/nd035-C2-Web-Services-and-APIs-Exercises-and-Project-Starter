package com.udacity.documentationservice.config.swagger;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

/**
 * Swagger UI configurations.
 * Configure bean of the {@link SwaggerResourcesProvider} to read data from in-memory context
 */
@Configuration
public class SwaggerConfig {

  private final SwaggerDefinitionsContext definitionContext;

  public SwaggerConfig(SwaggerDefinitionsContext definitionContext) {
    this.definitionContext = definitionContext;
  }

  @Bean
  public RestTemplate configureTemplate() {
    return new RestTemplate();
  }

  @Primary
  @Bean
  @Lazy
  public SwaggerResourcesProvider swaggerResourcesProvider(InMemorySwaggerResourcesProvider defaultResourcesProvider, RestTemplate temp) {
    return () -> {
      List<SwaggerResource> resources = new ArrayList<>(defaultResourcesProvider.get());
      resources.clear();
      resources.addAll(definitionContext.getSwaggerDefinitions());
      return resources;
    };
  }
}