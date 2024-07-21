package com.nearest.vehicle.domain.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.geo.Point;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class VehicleLocationResponse {

    private String vehicleName;
    private String averageDistance;
    private Point point;
    private String hash;
}
