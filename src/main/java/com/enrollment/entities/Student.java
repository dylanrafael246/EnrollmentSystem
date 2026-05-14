package com.enrollment.entities;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String program;    // e.g. BSIT, BSCS
    private int yearLevel;
    private List<String> passedCourseIds; // for prerequisite checking

    public Student(String studentId, String firstName, String lastName, String email, String program, int yearLevel) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.program = program;
        this.yearLevel = yearLevel;
        this.passedCourseIds = new ArrayList<>();
    }

    // Getters
    public String getStudentId()       { return studentId; }
    public String getFirstName()       { return firstName; }
    public String getLastName()        { return lastName; }
    public String getEmail()           { return email; }
    public String getProgram()         { return program; }
    public int    getYearLevel()       { return yearLevel; }
    public List<String> getPassedCourseIds() { return passedCourseIds; }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName)   { this.lastName = lastName; }
    public void setEmail(String email)         { this.email = email; }
    public void setProgram(String program)     { this.program = program; }
    public void setYearLevel(int yearLevel)    { this.yearLevel = yearLevel; }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s %s | %s | Year %d | %s",
                studentId, firstName, lastName, program, yearLevel, email);
    }
}
