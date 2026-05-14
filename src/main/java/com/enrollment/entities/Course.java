package com.enrollment.entities;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseId;
    private String courseName;
    private int units;
    private String description;
    private List<String> prerequisiteCourseIds; // Bonus: prerequisite checking

    public Course(String courseId, String courseName, int units, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.units = units;
        this.description = description;
        this.prerequisiteCourseIds = new ArrayList<>();
    }

    // Getters
    public String getCourseId()                          { return courseId; }
    public String getCourseName()                        { return courseName; }
    public int    getUnits()                             { return units; }
    public String getDescription()                       { return description; }
    public List<String> getPrerequisiteCourseIds()       { return prerequisiteCourseIds; }

    // Setters
    public void setCourseName(String courseName)         { this.courseName = courseName; }
    public void setUnits(int units)                      { this.units = units; }
    public void setDescription(String description)       { this.description = description; }

    @Override
    public String toString() {
        String prereqs = prerequisiteCourseIds.isEmpty() ? "None" : String.join(", ", prerequisiteCourseIds);
        return String.format("[%s] %s | %d units | Prerequisites: %s\n    %s",
                courseId, courseName, units, prereqs, description);
    }
}
