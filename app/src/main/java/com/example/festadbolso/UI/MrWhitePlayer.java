package com.example.festadbolso.UI;

import java.io.Serializable;
import java.util.UUID;

public class MrWhitePlayer implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String role; // "Regular" or "MrWhite"
    private String word; // The word assigned to this player

    public MrWhitePlayer(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.role = "Unknown";
        this.word = "Unknown";
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MrWhitePlayer player = (MrWhitePlayer) o;
        return id.equals(player.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}