package com.example.listofcomputers;

import java.io.Serializable;

public class Computer implements Serializable {
    private int id;
    private String name;
    private String status;
    private String location;
    private String lastOnline;

    public Computer() {
    }


    public Computer(String name, String status, String location, String lastOnline) {
        this.name = name;
        this.status = status;
        this.location = location;
        this.lastOnline = lastOnline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    @Override
    public String toString() {
        return name + "   " + status ;
    }
}
