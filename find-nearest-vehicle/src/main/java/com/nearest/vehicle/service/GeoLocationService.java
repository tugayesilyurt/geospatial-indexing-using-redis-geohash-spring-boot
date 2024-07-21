package com.nearest.vehicle.service;

import java.util.ArrayList;
import java.util.List;

import com.nearest.vehicle.domain.request.VehicleLocationRequest;
import com.nearest.vehicle.domain.response.VehicleLocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class GeoLocationService {

    private final GeoOperations<String, String> geoOperations;
    private final StringRedisTemplate redisTemplate;
    public static final String vehicleLocation = "vehicle_location";

    public void add(VehicleLocationRequest request) {
        Point point = new Point(request.getLongitude(), request.getLatitude());
        geoOperations.add(vehicleLocation, point, request.getVehicleName());
    }

    public List<VehicleLocationResponse> findNearestVehicles(Double longitude, Double latitude, int km) {

        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates()
                .includeDistance().sortAscending().limit(10);

        Circle circle = new Circle(new Point(longitude, latitude), new Distance(km, Metrics.KILOMETERS));
        GeoResults<RedisGeoCommands.GeoLocation<String>> response = geoOperations.radius(vehicleLocation, circle, args);

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

}
