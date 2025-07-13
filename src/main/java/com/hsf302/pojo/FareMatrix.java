// 1. ENTITY: FareMatrix.java
package com.hsf302.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "fares")
@Getter
@Setter
public class FareMatrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_station",columnDefinition = "NVARCHAR(50)")
    private String fromStation;

    @Column(name = "arrival_station",columnDefinition = "NVARCHAR(50)")
    private String toStation;

    private int price;
}
