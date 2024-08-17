# Pricing Service

The Pricing Service is a REST WebService that simulates a backend that
would store and retrieve the price of a vehicle given a vehicle id as
input. In this project, you will convert it to a microservice.


## Features

- REST WebService integrated with Spring Boot

## Instructions

#### TODOs

- Convert the Pricing Service to be a microservice.
- Add an additional test to check whether the application appropriately generates a price for a given vehicle ID

#### Run the code

To run this service you execute:

```
$ mvn clean package
```

```
$ java -jar target/pricing-service-0.0.1-SNAPSHOT.jar
```

It can also be imported in your IDE as a Maven project.
and run the class `PricingServiceApplication`.

## Operations

Swagger UI: http://localhost:8082/swagger-ui/

### Get Prices

`GET` `/prices`
Retreives list of vehicle prices along with currency

`Response code: 200; Time: 468ms; Content length: 6095 bytes`
```
HTTP/1.1 200 
Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE
Access-Control-Max-Age: 3600
Access-Control-Allow-Credentials: true
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Mon, 30 Aug 2021 11:20:48 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "_embedded": {
    "prices": [
      {
        "currency": "USD",
        "price": 16410.92,
        "vehicleId": 1,
        "_links": {
          "self": {
            "href": "http://localhost:8082/prices/1"
          },
          "price": {
            "href": "http://localhost:8082/prices/1"
          }
        }
      },
      {
        "currency": "USD",
        "price": 14453.00,
        "vehicleId": 2,
        "_links": {
          "self": {
            "href": "http://localhost:8082/prices/2"
          },
          "price": {
            "href": "http://localhost:8082/prices/2"
          }
        }
      }
    ]
  },
  "_links": {
    "self": {
      "href": "http://localhost:8082/prices"
    },
    "profile": {
      "href": "http://localhost:8082/profile/prices"
    }
  },
  "page": {
    "size": 2,
    "totalElements": 2,
    "totalPages": 1,
    "number": 0
  }
}
```

### Get Vehicle Price

`GET` `/prices/{vehicleId}`
Retreives price of a specific vehicle.

`Response code: 200; Time: 158ms; Content length: 237 bytes`
```
HTTP/1.1 200 
Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE
Access-Control-Max-Age: 3600
Access-Control-Allow-Credentials: true
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/hal+json
Transfer-Encoding: chunked
Date: Mon, 30 Aug 2021 11:30:36 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "currency": "USD",
  "price": 16410.92,
  "vehicleId": 1,
  "_links": {
    "self": {
      "href": "http://localhost:8082/prices/1"
    },
    "price": {
      "href": "http://localhost:8082/prices/1"
    }
  }
}
```

### Update Vehicle Price

`PUT` `/prices/{vehicleId}`
Updates price of a specific vehicle.

`Content-Type: application/json`
```
{
  "currency": "USD",
  "price": 14000,
  "vehicleId": 1
}
```

`Response code: 204; Time: 380ms; Content length: 0 bytes`
```
HTTP/1.1 204 
Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE
Access-Control-Max-Age: 3600
Access-Control-Allow-Credentials: true
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Location: http://localhost:8082/prices/1
Date: Mon, 30 Aug 2021 11:32:32 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>
```

### Delete Vehicle Price

`DELETE` `/prices/{id}`
Deletes price of a specific vehicle.

`Response code: 204; Time: 314ms; Content length: 0 bytes`
```
HTTP/1.1 204 
Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE
Access-Control-Max-Age: 3600
Access-Control-Allow-Credentials: true
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Date: Mon, 30 Aug 2021 11:34:01 GMT
Keep-Alive: timeout=60
Connection: keep-alive

<Response body is empty>
```
