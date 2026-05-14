package com.enrollment.interfaces;

import com.enrollment.entities.Department;
import com.enrollment.entities.Section;
import com.enrollment.entities.Student;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.exceptions.PrerequisiteNotMetException;
import com.enrollment.exceptions.SectionFullException;
import java.util.List;

public interface IEnrollmentService {
    void enrollStudentInSection(Student student, Section section)
            throws SectionFullException, PrerequisiteNotMetException;

    void unenrollStudentFromSection(String studentId, Section section);

    void viewDepartmentHierarchy(Department department);

    void addDepartment(Department department) throws DuplicateIdException;
    void addSectionToDepartment(String departmentId, Section section);

    Department findDepartmentById(String departmentId);
    List<Department> getAllDepartments();

    Section findSectionById(String sectionId);
    List<Section> getAllSections();
}
