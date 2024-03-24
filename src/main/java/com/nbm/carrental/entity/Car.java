package com.nbm.carrental.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;
    private String model;
    private String year;
    private double pricePerDay;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "features_id", referencedColumnName = "id")
    private Features features;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "specifications_id", referencedColumnName = "id")
    private Specifications specifications;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "agency_id")
    private Agency agency;
}
