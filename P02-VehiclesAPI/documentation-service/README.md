# Documentation Service

Centralized Service providing REST API to consolidate all Swagger JSON in a single endpoint.

## Features

- REST API
- Registered as a Eureka client
- Centralized Swagger API docs

## Instructions

#### Run the Code

To properly run this application you need to start the [Eureka Server](../eureka-server) and
then other services [Pricing Service](../pricing-service), [Vehicles API](../vehicles-api), and [Maps Service](../boogle-maps).


```
$ mvn clean package
```

```
$ java -jar target/documentation-service-0.0.1-SNAPSHOT.jar
```

Import it in your favorite IDE as a Maven Project
and run the class `DocumentationServiceApplication`.

## Operations

Swagger UI: http://localhost:7171/swagger-ui/

### Get Swagger Definition

`GET` `/service/{serviceId}`

Gets Swagger API Definition by Service Name (Service ID)

`Response code: 200; Time: 680ms; Content length: 5444 bytes`
```
HTTP/1.1 200 
Content-Type: text/plain;charset=UTF-8
Content-Length: 5444
Date: Mon, 30 Aug 2021 15:08:07 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{"swagger":"2.0","info":{"description":"This API returns vehicle location data.","version":"1.0","title":"Maps Service","termsOfService":"TOS","contact":{"name":"Mostafa Elsheikh","url":"sasa94s.github.io","email":"abdelaleem@gmx.com"},"license":{"name":"API License","url":"https://github.com/Sasa94s/cars-microservices/blob/master/LICENSE"}},"host":"192.168.1.3:9191","basePath":"/","tags":[{"name":"basic-error-controller","description":"Basic Error Controller"},{"name":"maps-controller","description":"Maps Controller"}],"paths":{"/error":{"get":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingGET","produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"},"404":{"description":"Not Found"}}},"head":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingHEAD","consumes":["application/json"],"produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"204":{"description":"No Content"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"}}},"post":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingPOST","consumes":["application/json"],"produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"201":{"description":"Created"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"},"404":{"description":"Not Found"}}},"put":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingPUT","consumes":["application/json"],"produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"201":{"description":"Created"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"},"404":{"description":"Not Found"}}},"delete":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingDELETE","produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"204":{"description":"No Content"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"}}},"options":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingOPTIONS","consumes":["application/json"],"produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"204":{"description":"No Content"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"}}},"patch":{"tags":["basic-error-controller"],"summary":"error","operationId":"errorUsingPATCH","consumes":["application/json"],"produces":["*/*"],"responses":{"200":{"description":"OK","schema":{"type":"object","additionalProperties":{"type":"object"}}},"204":{"description":"No Content"},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"}}}},"/maps":{"get":{"tags":["maps-controller"],"summary":"get","operationId":"getUsingGET","produces":["*/*"],"parameters":[{"name":"lat","in":"query","description":"lat","required":true,"type":"number","format":"double"},{"name":"lon","in":"query","description":"lon","required":true,"type":"number","format":"double"}],"responses":{"200":{"description":"OK","schema":{"$ref":"#/definitions/Address"}},"401":{"description":"Unauthorized"},"403":{"description":"Forbidden"},"404":{"description":"Not Found"}}}}},"definitions":{"Address":{"type":"object","properties":{"address":{"type":"string"},"city":{"type":"string"},"state":{"type":"string"},"zip":{"type":"string"}},"title":"Address"},"ModelAndView":{"type":"object","properties":{"empty":{"type":"boolean"},"model":{"type":"object"},"modelMap":{"type":"object","additionalProperties":{"type":"object"}},"reference":{"type":"boolean"},"status":{"type":"string","enum":["ACCEPTED","ALREADY_REPORTED","BAD_GATEWAY","BAD_REQUEST","BANDWIDTH_LIMIT_EXCEEDED","CHECKPOINT","CONFLICT","CONTINUE","CREATED","DESTINATION_LOCKED","EXPECTATION_FAILED","FAILED_DEPENDENCY","FORBIDDEN","FOUND","GATEWAY_TIMEOUT","GONE","HTTP_VERSION_NOT_SUPPORTED","IM_USED","INSUFFICIENT_SPACE_ON_RESOURCE","INSUFFICIENT_STORAGE","INTERNAL_SERVER_ERROR","I_AM_A_TEAPOT","LENGTH_REQUIRED","LOCKED","LOOP_DETECTED","METHOD_FAILURE","METHOD_NOT_ALLOWED","MOVED_PERMANENTLY","MOVED_TEMPORARILY","MULTIPLE_CHOICES","MULTI_STATUS","NETWORK_AUTHENTICATION_REQUIRED","NON_AUTHORITATIVE_INFORMATION","NOT_ACCEPTABLE","NOT_EXTENDED","NOT_FOUND","NOT_IMPLEMENTED","NOT_MODIFIED","NO_CONTENT","OK","PARTIAL_CONTENT","PAYLOAD_TOO_LARGE","PAYMENT_REQUIRED","PERMANENT_REDIRECT","PRECONDITION_FAILED","PRECONDITION_REQUIRED","PROCESSING","PROXY_AUTHENTICATION_REQUIRED","REQUESTED_RANGE_NOT_SATISFIABLE","REQUEST_ENTITY_TOO_LARGE","REQUEST_HEADER_FIELDS_TOO_LARGE","REQUEST_TIMEOUT","REQUEST_URI_TOO_LONG","RESET_CONTENT","SEE_OTHER","SERVICE_UNAVAILABLE","SWITCHING_PROTOCOLS","TEMPORARY_REDIRECT","TOO_EARLY","TOO_MANY_REQUESTS","UNAUTHORIZED","UNAVAILABLE_FOR_LEGAL_REASONS","UNPROCESSABLE_ENTITY","UNSUPPORTED_MEDIA_TYPE","UPGRADE_REQUIRED","URI_TOO_LONG","USE_PROXY","VARIANT_ALSO_NEGOTIATES"]},"view":{"$ref":"#/definitions/View"},"viewName":{"type":"string"}},"title":"ModelAndView"},"View":{"type":"object","properties":{"contentType":{"type":"string"}},"title":"View"}}}

```
