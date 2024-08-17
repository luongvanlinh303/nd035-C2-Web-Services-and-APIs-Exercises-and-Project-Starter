package com.udacity.documentationservice.controllers;

import com.udacity.documentationservice.config.swagger.SwaggerDefinitionsContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 *  Controller to serve the JSON of Swagger API Definition from in-memory store.
 */
@RestController
public class SwaggerDefinitionController {

    private final SwaggerDefinitionsContext definitionContext;

    public SwaggerDefinitionController(SwaggerDefinitionsContext definitionContext) {
        this.definitionContext = definitionContext;
    }

    /**
     * Gets Swagger API Definition by Service Name (Service ID)
     * @param serviceId Service Identifier
     * @return JSON for the Swagger API Definition
     */
    @GetMapping("/service/{serviceName}")
    public String getSwaggerDefinition(@PathVariable("serviceName") String serviceId) {
        return definitionContext.getSwaggerDefinition(serviceId);
    }
}