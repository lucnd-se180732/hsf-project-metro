package com.hsf302.pojo;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "station_bus_routes", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"station_id", "bus_route_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationBusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id", nullable = false)
    private Station station;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bus_route_id", nullable = false)
    private BusRoute busRoute;

    @Column(name = "walking_distance_m")
    private Integer walkingDistanceM;

    @Column(name = "walking_time_minutes")
    private Integer walkingTimeMinutes;


}
