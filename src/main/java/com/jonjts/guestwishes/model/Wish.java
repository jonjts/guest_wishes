package com.jonjts.guestwishes.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity(name = "wishes")
public class Wish {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonIgnore
    @ManyToOne
    private Guest guest;

    @Column
    @NotNull
    private String local_name;


    @Column
    @NotNull
    private Double latitude;

    @Column
    @NotNull
    private Double longitude;

    public Wish() {
        super();
    }

    public Wish(Long id, Long guestId) {
        super();
        setGuest(new Guest(guestId));
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocal_name() {
        return local_name;
    }

    public void setLocal_name(String local_name) {
        this.local_name = local_name;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    @Override
    public String toString() {
        return getLocal_name() + " long: " + getLongitude() + ", lat: " + getLatitude();
    }
}
