package com.enrollment.services;

import com.enrollment.entities.Student;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.interfaces.IStudentService;
import java.util.ArrayList;
import java.util.List;

public class StudentServiceImpl implements IStudentService {

    private final List<Student> students = new ArrayList<>();

    @Override
    public void addStudent(Student student) throws DuplicateIdException {
        // Check for duplicate ID
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(student.getStudentId())) {
                throw new DuplicateIdException("Student ID '" + student.getStudentId() + "' already exists!");
            }
        }
        students.add(student);
    }

    @Override
    public void updateStudent(String studentId, String firstName, String lastName,
                               String email, String program, int yearLevel) {
        Student s = findStudentById(studentId);
        if (s == null) {
            System.out.println("  [!] Student not found: " + studentId);
            return;
        }
        s.setFirstName(firstName);
        s.setLastName(lastName);
        s.setEmail(email);
        s.setProgram(program);
        s.setYearLevel(yearLevel);
    }

    @Override
    public void removeStudent(String studentId) {
        students.removeIf(s -> s.getStudentId().equalsIgnoreCase(studentId));
    }

    @Override
    public List<Student> getAllStudents() {
        return students;
    }

    @Override
    public Student findStudentById(String studentId) {
        for (Student s : students) {
            if (s.getStudentId().equalsIgnoreCase(studentId)) return s;
        }
        return null;
    }

    @Override
    public void markCourseAsPassed(String studentId, String courseId) {
        Student s = findStudentById(studentId);
        if (s == null) {
            System.out.println("  [!] Student not found.");
            return;
        }
        if (!s.getPassedCourseIds().contains(courseId)) {
            s.getPassedCourseIds().add(courseId);
            System.out.println("  [OK] Marked course " + courseId + " as passed for " + s.getFullName());
        } else {
            System.out.println("  [!] Course " + courseId + " already marked as passed.");
        }
    }
}
