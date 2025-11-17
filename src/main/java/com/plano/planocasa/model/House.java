package com.plano.planocasa.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class House {
    private Long id;
    private String name;
    private String Address;
    private OffsetDateTime createdAt;
    private List<Room> rooms = new ArrayList<>();

    public House() {}

    public House(Long id, String name, String Address) {
        this.id = id;
        this.name = name;
        this.Address = Address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
