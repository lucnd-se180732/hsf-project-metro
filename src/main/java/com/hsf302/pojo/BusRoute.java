package com.hsf302.pojo;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "bus_routes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_number", nullable = false, unique = true)
    private String routeNumber;

    @Column(nullable = false,columnDefinition = "NVARCHAR(500)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String description;

    private String operator;

    @Column(name = "is_active")
    private Boolean isActive = true;



    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "bus_route_stops", joinColumns = @JoinColumn(name = "bus_route_id"))
    @Column(name = "stop_name", columnDefinition = "NVARCHAR(255)")
    private List<String> stops = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @OneToMany(mappedBy = "busRoute", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StationBusRoute> stationBusRoutes = new ArrayList<>();
}

