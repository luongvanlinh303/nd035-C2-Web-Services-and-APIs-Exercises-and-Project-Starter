# Boogle Maps

This is a Mock that simulates a Maps WebService where, given a latitude
longitude, will return a random address.

## Instructions

Via shell it can be started using

```
$ mvn clean package
```

```
$ java -jar target/boogle-maps-0.0.1-SNAPSHOT.jar
```

The service is available by default on port `9191`. You can check it on the 
command line by using

```
$ curl http://localhost:9191/maps\?lat\=20.0\&lon\=30.0
``` 

You can also import it as a Maven project on your preferred IDE and 
run the class `BoogleMapsApplication`.

## Operations

Swagger UI: http://localhost:9191/swagger-ui/

### Get Address

`GET` `/maps?lat=20.0&lon=30.0`

Retrieves address information based on latitude and longitude.

```
HTTP/1.1 200 
Access-Control-Allow-Methods: POST, GET, OPTIONS, PUT, DELETE
Access-Control-Max-Age: 3600
Access-Control-Allow-Credentials: true
Content-Type: application/json
Transfer-Encoding: chunked
Date: Mon, 30 Aug 2021 11:00:05 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
  "address": "135 Fairgrounds Memorial Pkwy",
  "city": "Ithaca",
  "state": "NY",
  "zip": "14850"
}
```

`Response code: 200; Time: 334ms; Content length: 86 bytes`