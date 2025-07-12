package com.hsf302.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Table(name = "stations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_code", nullable = false, unique = true)
    private String stationCode;

    @Column(nullable = false)
    private String name;

    private String address;

    private Double latitude;
    private Double longitude;

    @Column(name = "is_terminal")
    private Boolean isTerminal = false;

    @Column(name = "is_interchange")
    private Boolean isInterchange = false;

    private String facilities; // có thể lưu JSON hoặc text

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StationBusRoute> stationBusRoutes = new ArrayList<>();
}
