package com.enrollment.interfaces;

import com.enrollment.entities.Course;
import com.enrollment.exceptions.DuplicateIdException;
import java.util.List;

public interface ICourseService {
    void addCourse(Course course) throws DuplicateIdException;
    void updateCourse(String courseId, String courseName, int units, String description);
    void removeCourse(String courseId);
    List<Course> getAllCourses();
    Course findCourseById(String courseId);
    void addPrerequisite(String courseId, String prerequisiteCourseId);
}
