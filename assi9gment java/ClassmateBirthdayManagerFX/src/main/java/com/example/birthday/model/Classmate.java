package com.example.birthday.model;

import java.time.LocalDate;

public class Classmate {
    private int id;
    private String name;
    private LocalDate dob;
    private String phone;
    private String notes;

    public Classmate(int id, String name, LocalDate dob, String phone, String notes) {
        this.id = id;
        this.name = name;
        this.dob = dob;
        this.phone = phone;
        this.notes = notes;
    }

    public Classmate(String name, LocalDate dob, String phone, String notes) {
        this(0, name, dob, phone, notes);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public LocalDate getDob() { return dob; }
    public String getPhone() { return phone; }
    public String getNotes() { return notes; }

    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDob(LocalDate dob) { this.dob = dob; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setNotes(String notes) { this.notes = notes; }
}
