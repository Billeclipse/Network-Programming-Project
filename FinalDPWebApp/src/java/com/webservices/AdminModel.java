package com.webservices;

public class AdminModel {
    int id;
    String username;

    public AdminModel() {
    }

    public AdminModel(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "AdminModel{" + "id=" + id + ", username=" + username + '}';
    }
}
