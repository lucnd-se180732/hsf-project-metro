package com.hsf302.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trains")
@Getter
@Setter
public class Train {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "NVARCHAR(50)")
    private String trainCode;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String description;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private List<TrainStationSchedule> schedules = new ArrayList<>();
}
