package com.enrollment.entities;

import java.util.ArrayList;
import java.util.List;

public class Instructor {
    private String instructorId;
    private String firstName;
    private String lastName;
    private String email;
    private String specialization;
    private List<String> assignedSectionIds;

    public Instructor(String instructorId, String firstName, String lastName, String email, String specialization) {
        this.instructorId = instructorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.specialization = specialization;
        this.assignedSectionIds = new ArrayList<>();
    }

    // Getters
    public String getInstructorId()            { return instructorId; }
    public String getFirstName()               { return firstName; }
    public String getLastName()                { return lastName; }
    public String getEmail()                   { return email; }
    public String getSpecialization()          { return specialization; }
    public List<String> getAssignedSectionIds(){ return assignedSectionIds; }

    // Setters
    public void setFirstName(String firstName)       { this.firstName = firstName; }
    public void setLastName(String lastName)         { this.lastName = lastName; }
    public void setEmail(String email)               { this.email = email; }
    public void setSpecialization(String spec)       { this.specialization = spec; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | Specialization: %s | Email: %s",
                instructorId, firstName, lastName, specialization, email);
    }
}
