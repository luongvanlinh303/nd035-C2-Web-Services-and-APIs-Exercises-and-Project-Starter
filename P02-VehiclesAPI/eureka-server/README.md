# Eureka Server

A Netflix Eureka service registry which resolves hosts. A service registry is useful because it enables client-side load-balancing and decouples service providers from consumers without the need for DNS.

## Instructions

Via shell it can be started using

```
$ mvn clean package
```

Run Eureka Server

```
$ java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```

You can also import it as a Maven project on your preferred IDE and
run the class `EurekaServerApplication`.
