package com.udacity.documentationservice.config.swagger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.udacity.documentationservice.config.swagger.domain.SwaggerResource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Periodically pull the service instances and update the in-memory store as key value pair
 */
@Component
public class SwaggerDefinitionUpdater {

  private static final Logger logger = LoggerFactory.getLogger(SwaggerDefinitionUpdater.class);

  private static final String DEFAULT_SWAGGER_URL = "/v2/api-docs";
  private static final String KEY_SWAGGER_URL = "swagger_url";
  private static final Set<String> IGNORED_SERVICE_IDS = new HashSet<>() {{
    add("eureka-server");
  }};

  private final DiscoveryClient discoveryClient;
  private final SwaggerDefinitionsContext definitionContext;
  private final RestTemplate template;

  public SwaggerDefinitionUpdater(DiscoveryClient discoveryClient, SwaggerDefinitionsContext definitionContext) {
    this.discoveryClient = discoveryClient;
    this.definitionContext = definitionContext;
    this.template = new RestTemplate();
  }

  @Scheduled(fixedDelayString = "${swagger.config.refreshrate}")
  public void refreshSwaggerConfigurations() {
    logger.debug("Starting Swagger Definition Context refresh");

    List<SwaggerResource> swaggerResources = discoveryClient.getServices().stream()
        .filter(serviceId -> {
          List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);
          boolean isFound = instances != null && !instances.isEmpty();
          logger.debug("Attempting Swagger Definition refresh for Service ID: {} [Status={}]", serviceId, isFound);
          return isFound;
        })
        .filter(Predicate.not(IGNORED_SERVICE_IDS::contains))
        .map(serviceId -> new SwaggerResource(serviceId, getSwaggerURL(discoveryClient.getInstances(serviceId).get(0))))
        .collect(Collectors.toList());

    swaggerResources.forEach(swaggerResource -> {
      String swaggerDefinition = getSwaggerApiDefinition(swaggerResource);
      definitionContext.addServiceDefinition(swaggerResource.getServiceId(), swaggerDefinition);
      logger.info("Swagger Definition Context Refreshed at: {}",
          LocalDateTime.now());
    });
  }

  private String getSwaggerURL(ServiceInstance instance) {
    String swaggerURL = instance.getMetadata().get(KEY_SWAGGER_URL);
    return swaggerURL != null ? instance.getUri() + swaggerURL : instance.getUri() + DEFAULT_SWAGGER_URL;
  }

  private String getSwaggerApiDefinition(SwaggerResource swaggerResource) {
    logger.debug("Accessing JSON Swagger API Definition for Service ID: {} - URL: {} ",
        swaggerResource.getServiceId(), swaggerResource.getUrl());
    Optional<Object> optionalDefinition;
    try {
      optionalDefinition = Optional.ofNullable(template.getForObject(swaggerResource.getUrl(), Object.class));
    } catch (RestClientException ex) {
      logger.error("[Error] Fetching Swagger API Definition for Service ID: {} - Cause: {}",
          swaggerResource.getServiceId(), ex.getMessage());
      optionalDefinition = Optional.empty();
    }
    if (optionalDefinition.isEmpty()) {
      logger.error("[Error] Skipping Service ID: {} - Cause: Could not fetch Swagger API Definition",
          swaggerResource.getServiceId());
      return "";
    }

    return getJSON(optionalDefinition.get());
  }

  private String getJSON(Object jsonData) {
    try {
      return new ObjectMapper().writeValueAsString(jsonData);
    } catch (JsonProcessingException e) {
      logger.error("Error: {} ", e.getMessage());
      return "";
    }
  }
}