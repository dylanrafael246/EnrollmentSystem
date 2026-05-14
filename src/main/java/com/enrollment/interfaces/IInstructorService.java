package com.enrollment.interfaces;

import com.enrollment.entities.Instructor;
import com.enrollment.entities.Section;
import com.enrollment.exceptions.DuplicateIdException;
import java.util.List;

public interface IInstructorService {
    void addInstructor(Instructor instructor) throws DuplicateIdException;
    void updateInstructor(String instructorId, String firstName, String lastName, String email, String specialization);
    void removeInstructor(String instructorId);
    void assignInstructorToSection(String instructorId, Section section);
    Instructor findInstructorById(String instructorId);
    List<Instructor> getAllInstructors();
    String getInstructorDetails(String instructorId);
}
