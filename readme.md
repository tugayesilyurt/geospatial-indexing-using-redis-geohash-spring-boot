# Project

Implementing Geospatial Indexing in Spring Boot Using Redis Geohash

## Read this article on Medium

[Medium Article](https://medium.com)

## Geospatial Indexing Example

<img src="https://github.com/tugayesilyurt/geospatial-indexing-using-redis-geohash-spring-boot/blob/main/assets/geohash.png" width=70% height=70%>

## Tech Stack

- Java 21
- Spring Boot 3
- Redis 
- Redis Geohash
- Geospatial Indexing
- Docker

## Installation

 - follow the steps:

```shell
   docker-compose up -d
   cd find-nearest-vehicle
   ./mvnw spring-boot:run
```

## Redis Geohash - Adding Geospatial Data In Spring Boot

Redis offers the GEOADD command to add geospatial data. You can use it to add a single or multiple locations at once. Let's start with a single location.

## Using GeoOperations For Adding In Spring Boot

```java
private final GeoOperations<String, String> geoOperations;
public static final String vehicleLocation = "vehicle_location";

public void add(VehicleLocationRequest request) {
    Point point = new Point(request.getLongitude(), request.getLatitude());
    geoOperations.add(vehicleLocation, point, request.getVehicleName());
}
```


## Redis Geohash - Fetching Geospatial Data In Spring Boot

Fetching geospatial data is performed with GEODIST, GEORADIUS, or GEORADIUSBYMEMBER. Let's focus on GEORADIUS, which allows us to query locations within a specific radius of a point.

## Using GeoOperations For Fetching In Spring Boot

```java
public List<VehicleLocationResponse> findNearestVehicles(Double longitude, Double latitude, int km) {

  RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands
          .GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates()
          .includeDistance().sortAscending().limit(10);

  Circle circle = new Circle(new Point(longitude, latitude)
          ,new Distance(km, Metrics.KILOMETERS));
  GeoResults<RedisGeoCommands.GeoLocation<String>> response = 
          geoOperations.radius(vehicleLocation, circle, args);

  List<VehicleLocationResponse> vehicleLocationResponses = new ArrayList<>();
  response.getContent().stream().forEach(data -> {

      vehicleLocationResponses.add(VehicleLocationResponse.builder()
            .vehicleName(data.getContent().getName())
            .averageDistance(data.getDistance().toString())
            .point(data.getContent().getPoint())
            .hash(geoOperations.hash(vehicleLocation,data.getContent().getName()).stream().findFirst().get())
            .build());

  });

  return vehicleLocationResponses;
}
```

