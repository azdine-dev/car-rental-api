package com.nbm.carrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Bus {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private String busNumber;

        private String model;

        private double latitude;

        private double longitude;

        @ElementCollection
        private List<String> amenities;

        @OneToMany(cascade = CascadeType.ALL)
        private List<BusImage> images;

        private int capacity;

        private String description;

        @ManyToOne
        @JoinColumn(name = "agency_id")
        private Agency agency;

    }



