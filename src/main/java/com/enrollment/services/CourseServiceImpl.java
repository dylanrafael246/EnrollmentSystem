package com.enrollment.services;

import com.enrollment.entities.Course;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.interfaces.ICourseService;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements ICourseService {

    private final List<Course> courses = new ArrayList<>();

    @Override
    public void addCourse(Course course) throws DuplicateIdException {
        for (Course c : courses) {
            if (c.getCourseId().equalsIgnoreCase(course.getCourseId())) {
                throw new DuplicateIdException("Course ID '" + course.getCourseId() + "' already exists!");
            }
        }
        courses.add(course);
    }

    @Override
    public void updateCourse(String courseId, String courseName, int units, String description) {
        Course c = findCourseById(courseId);
        if (c == null) {
            System.out.println("  [!] Course not found: " + courseId);
            return;
        }
        c.setCourseName(courseName);
        c.setUnits(units);
        c.setDescription(description);
    }

    @Override
    public void removeCourse(String courseId) {
        courses.removeIf(c -> c.getCourseId().equalsIgnoreCase(courseId));
    }

    @Override
    public List<Course> getAllCourses() {
        return courses;
    }

    @Override
    public Course findCourseById(String courseId) {
        for (Course c : courses) {
            if (c.getCourseId().equalsIgnoreCase(courseId)) return c;
        }
        return null;
    }

    @Override
    public void addPrerequisite(String courseId, String prerequisiteCourseId) {
        Course c = findCourseById(courseId);
        Course prereq = findCourseById(prerequisiteCourseId);
        if (c == null || prereq == null) {
            System.out.println("  [!] One or both courses not found.");
            return;
        }
        if (!c.getPrerequisiteCourseIds().contains(prerequisiteCourseId)) {
            c.getPrerequisiteCourseIds().add(prerequisiteCourseId);
            System.out.println("  [OK] " + prereq.getCourseName() + " added as prerequisite for " + c.getCourseName());
        } else {
            System.out.println("  [!] Prerequisite already set.");
        }
    }
}
