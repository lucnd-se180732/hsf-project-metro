// 1. ENTITY: FareMatrix.java
package com.hsf302.pojo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "fares")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FareMatrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "departure_station",columnDefinition = "NVARCHAR(50)")
    private String fromStation;

    @Column(name = "arrival_station",columnDefinition = "NVARCHAR(50)")
    private String toStation;

    private int price;

    public FareMatrix(String fromStation, String toStation, int price) {
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.price = price;
    }
}
