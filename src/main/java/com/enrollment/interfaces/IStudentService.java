package com.enrollment.interfaces;

import com.enrollment.entities.Student;
import com.enrollment.exceptions.DuplicateIdException;
import java.util.List;

public interface IStudentService {
    void addStudent(Student student) throws DuplicateIdException;
    void updateStudent(String studentId, String firstName, String lastName, String email, String program, int yearLevel);
    void removeStudent(String studentId);
    List<Student> getAllStudents();
    Student findStudentById(String studentId);
    void markCourseAsPassed(String studentId, String courseId);
}
