package com.nearest.vehicle.controller;

import com.nearest.vehicle.domain.request.VehicleLocationRequest;
import com.nearest.vehicle.domain.response.VehicleLocationResponse;
import com.nearest.vehicle.service.GeoLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/v1/api/vehicles")
@RestController
public class VehicleController {

    private final GeoLocationService geoLocationService;

    @PostMapping
    public ResponseEntity<Void> setVehicleLocation(@RequestBody VehicleLocationRequest request) {
        geoLocationService.add(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/nearest")
    public ResponseEntity<List<VehicleLocationResponse>> getNearestVehicleLocations(
                                                                   @RequestParam("latitude") double latitude,
                                                                   @RequestParam("longitude") double longitude,
                                                                   @RequestParam("km") int km) {

        List<VehicleLocationResponse> vehicleLocations = geoLocationService.findNearestVehicles(longitude, latitude, km);
        return new ResponseEntity<>(vehicleLocations, HttpStatus.OK);
    }

}
