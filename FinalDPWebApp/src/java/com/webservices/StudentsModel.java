package com.webservices;

public class StudentsModel {
    private int id;
    private String username;
    private String name;
    private String surname;
    private boolean valid;

    public StudentsModel() {
    }

    public StudentsModel(int id, String username, String name, String surname, boolean valid) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.surname = surname;
        this.valid = valid;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @Override
    public String toString() {
        return "StudentsModel{" + "id=" + id + ", username=" + username + ", name=" + name + ", surname=" + surname + ", valid=" + valid + '}';
    }
}
