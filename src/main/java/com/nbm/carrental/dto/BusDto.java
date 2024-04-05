package com.nbm.carrental.dto;

import lombok.*;

import java.util.List;

@Getter @Setter @Builder @NoArgsConstructor
@AllArgsConstructor
public class BusDto {

    private Long id;
    private String name;
    private String busNumber;
    private String model;
    private double latitude;
    private double longitude;
    private List<String> amenities;
    private List<String> imageUrls;
    private int capacity;
    private String description;
    private Long agencyId;  // Assuming agencyId is of type Long

    // Getters and setters

}
