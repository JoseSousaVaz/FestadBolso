package com.example.festadbolso.UI;

import java.io.Serializable;
import java.util.UUID;

public class Player implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String role;
    private String location;

    public Player(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.role = "Unknown";
        this.location = "Unknown";
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}