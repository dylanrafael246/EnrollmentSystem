package com.enrollment.entities;

import java.util.ArrayList;
import java.util.List;

public class Section {
    private String sectionId;
    private String sectionName;   // e.g. BSIT-1A
    private String courseId;
    private int maxCapacity;
    private String instructorId;  // assigned instructor (nullable)
    private List<Student> enrolledStudents;
    private String schedule;      // e.g. "MWF 8:00-9:00 AM"
    private String room;

    public Section(String sectionId, String sectionName, String courseId, int maxCapacity, String schedule, String room) {
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.courseId = courseId;
        this.maxCapacity = maxCapacity;
        this.schedule = schedule;
        this.room = room;
        this.enrolledStudents = new ArrayList<>();
        this.instructorId = null;
    }

    // Getters
    public String  getSectionId()             { return sectionId; }
    public String  getSectionName()           { return sectionName; }
    public String  getCourseId()              { return courseId; }
    public int     getMaxCapacity()           { return maxCapacity; }
    public String  getInstructorId()          { return instructorId; }
    public List<Student> getEnrolledStudents(){ return enrolledStudents; }
    public String  getSchedule()              { return schedule; }
    public String  getRoom()                  { return room; }

    // Setters
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
    public void setInstructorId(String instructorId){ this.instructorId = instructorId; }
    public void setSchedule(String schedule)       { this.schedule = schedule; }
    public void setRoom(String room)               { this.room = room; }
    public void setMaxCapacity(int maxCapacity)    { this.maxCapacity = maxCapacity; }

    public int getAvailableSlots() {
        return maxCapacity - enrolledStudents.size();
    }

    public boolean isFull() {
        return enrolledStudents.size() >= maxCapacity;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | Course: %s | Capacity: %d/%d | Schedule: %s | Room: %s",
                sectionId, sectionName, courseId, enrolledStudents.size(), maxCapacity, schedule, room);
    }
}
