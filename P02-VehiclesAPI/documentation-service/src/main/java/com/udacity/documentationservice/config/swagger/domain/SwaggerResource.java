package com.udacity.documentationservice.config.swagger.domain;

public class SwaggerResource {
  private String serviceId;
  private String url;

  public SwaggerResource(String serviceId, String url) {
    this.serviceId = serviceId;
    this.url = url;
  }

  public String getServiceId() {
    return serviceId;
  }

  public void setServiceId(String serviceId) {
    this.serviceId = serviceId;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
