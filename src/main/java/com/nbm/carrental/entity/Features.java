package com.nbm.carrental.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Features {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean multiZoneAC;
    private boolean heatedFrontSeats;
    private boolean androidAuto;
    private boolean navigationSystem;
    private boolean premiumSoundSystem;
    private boolean bluetooth;
    private boolean keylessStart;
    private boolean memorySeat;
    private boolean adaptiveCruiseControl;
    private boolean intermittentWipers;
    private boolean powerWindows;

    // Getters and Setters
}