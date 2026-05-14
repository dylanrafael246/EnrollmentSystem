package com.enrollment.services;

import com.enrollment.entities.Instructor;
import com.enrollment.entities.Section;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.interfaces.IInstructorService;
import java.util.ArrayList;
import java.util.List;

public class InstructorServiceImpl implements IInstructorService {

    private final List<Instructor> instructors = new ArrayList<>();

    @Override
    public void addInstructor(Instructor instructor) throws DuplicateIdException {
        for (Instructor i : instructors) {
            if (i.getInstructorId().equalsIgnoreCase(instructor.getInstructorId())) {
                throw new DuplicateIdException("Instructor ID '" + instructor.getInstructorId() + "' already exists!");
            }
        }
        instructors.add(instructor);
    }

    @Override
    public void updateInstructor(String instructorId, String firstName, String lastName,
                                  String email, String specialization) {
        Instructor i = findInstructorById(instructorId);
        if (i == null) {
            System.out.println("  [!] Instructor not found: " + instructorId);
            return;
        }
        i.setFirstName(firstName);
        i.setLastName(lastName);
        i.setEmail(email);
        i.setSpecialization(specialization);
    }

    @Override
    public void removeInstructor(String instructorId) {
        instructors.removeIf(i -> i.getInstructorId().equalsIgnoreCase(instructorId));
    }

    @Override
    public void assignInstructorToSection(String instructorId, Section section) {
        Instructor instructor = findInstructorById(instructorId);
        if (instructor == null) {
            System.out.println("  [!] Instructor not found: " + instructorId);
            return;
        }
        section.setInstructorId(instructorId);
        if (!instructor.getAssignedSectionIds().contains(section.getSectionId())) {
            instructor.getAssignedSectionIds().add(section.getSectionId());
        }
        System.out.println("  [OK] Assigned " + instructor.getFullName() + " to section " + section.getSectionName());
    }

    @Override
    public Instructor findInstructorById(String instructorId) {
        for (Instructor i : instructors) {
            if (i.getInstructorId().equalsIgnoreCase(instructorId)) return i;
        }
        return null;
    }

    @Override
    public List<Instructor> getAllInstructors() {
        return instructors;
    }

    @Override
    public String getInstructorDetails(String instructorId) {
        Instructor i = findInstructorById(instructorId);
        if (i == null) return "  [!] Instructor not found.";
        StringBuilder sb = new StringBuilder();
        sb.append("  ").append(i).append("\n");
        sb.append("  Assigned Sections: ");
        if (i.getAssignedSectionIds().isEmpty()) {
            sb.append("None");
        } else {
            sb.append(String.join(", ", i.getAssignedSectionIds()));
        }
        return sb.toString();
    }
}
