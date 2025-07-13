package com.hsf302.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "stations")
@Getter
@Setter
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(50)",nullable = false, unique = true)
    private String stationCode;

    @Column(columnDefinition = "NVARCHAR(100)")
    private String name;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;

    private int stationOrder;

    private Double distanceFromStartKm;

    private Double latitude;

    private Double longitude;

    @Column(name = "is_terminal")
    private Boolean isTerminal = false;

    @Column(name = "is_interchange")
    private Boolean isInterchange = false;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt = new Date();

    @ManyToOne
    @JoinColumn(name = "route_id")
    @JsonBackReference
    private Route route;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL)
    private List<TrainStationSchedule> trainSchedules = new ArrayList<>();

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StationBusRoute> stationBusRoutes = new ArrayList<>();
}
