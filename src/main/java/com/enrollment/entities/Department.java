package com.enrollment.entities;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private String departmentId;
    private String departmentName;  // e.g. College of Computer Studies
    private String deanName;
    private List<Section> sections;

    public Department(String departmentId, String departmentName, String deanName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.deanName = deanName;
        this.sections = new ArrayList<>();
    }

    // Getters
    public String getDepartmentId()         { return departmentId; }
    public String getDepartmentName()       { return departmentName; }
    public String getDeanName()             { return deanName; }
    public List<Section> getSections()      { return sections; }

    // Setters
    public void setDepartmentName(String departmentName) { this.departmentName = departmentName; }
    public void setDeanName(String deanName)             { this.deanName = deanName; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Dean: %s | Sections: %d",
                departmentId, departmentName, deanName, sections.size());
    }
}
