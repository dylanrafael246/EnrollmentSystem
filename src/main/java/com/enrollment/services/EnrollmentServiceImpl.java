package com.enrollment.services;

import com.enrollment.entities.Department;
import com.enrollment.entities.Instructor;
import com.enrollment.entities.Section;
import com.enrollment.entities.Student;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.exceptions.PrerequisiteNotMetException;
import com.enrollment.exceptions.SectionFullException;
import com.enrollment.interfaces.IEnrollmentService;
import com.enrollment.interfaces.ICourseService;
import com.enrollment.interfaces.IInstructorService;

import java.util.ArrayList;
import java.util.List;

public class EnrollmentServiceImpl implements IEnrollmentService {

    private final List<Department> departments = new ArrayList<>();
    private final List<Section> allSections   = new ArrayList<>();

    // We need access to course and instructor services for display purposes
    private ICourseService courseService;
    private IInstructorService instructorService;

    public EnrollmentServiceImpl(ICourseService courseService, IInstructorService instructorService) {
        this.courseService = courseService;
        this.instructorService = instructorService;
    }

    @Override
    public void enrollStudentInSection(Student student, Section section)
            throws SectionFullException, PrerequisiteNotMetException {

        // 1. Capacity check
        if (section.isFull()) {
            throw new SectionFullException(
                "Enrollment FAILED: Section '" + section.getSectionName() + "' is full! (" +
                section.getEnrolledStudents().size() + "/" + section.getMaxCapacity() + " students)"
            );
        }

        // 2. Prerequisite check (Bonus feature!)
        if (courseService != null) {
            var course = courseService.findCourseById(section.getCourseId());
            if (course != null) {
                for (String prereqId : course.getPrerequisiteCourseIds()) {
                    if (!student.getPassedCourseIds().contains(prereqId)) {
                        var prereqCourse = courseService.findCourseById(prereqId);
                        String prereqName = (prereqCourse != null) ? prereqCourse.getCourseName() : prereqId;
                        throw new PrerequisiteNotMetException(
                            "Enrollment FAILED: " + student.getFullName() + " has not passed prerequisite: " + prereqName
                        );
                    }
                }
            }
        }

        // 3. Check if student is already enrolled in this section
        for (Student s : section.getEnrolledStudents()) {
            if (s.getStudentId().equalsIgnoreCase(student.getStudentId())) {
                System.out.println("  [!] Student is already enrolled in this section.");
                return;
            }
        }

        // 4. Enroll!
        section.getEnrolledStudents().add(student);
    }

    @Override
    public void unenrollStudentFromSection(String studentId, Section section) {
        boolean removed = section.getEnrolledStudents()
                .removeIf(s -> s.getStudentId().equalsIgnoreCase(studentId));
        if (removed) {
            System.out.println("  [OK] Student " + studentId + " unenrolled from " + section.getSectionName());
        } else {
            System.out.println("  [!] Student not found in section " + section.getSectionName());
        }
    }

    @Override
    public void viewDepartmentHierarchy(Department department) {
        System.out.println();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.printf ("║  DEPARTMENT: %-39s║%n", department.getDepartmentName());
        System.out.printf ("║  Dean: %-45s║%n", department.getDeanName());
        System.out.printf ("║  ID: %-47s║%n", department.getDepartmentId());
        System.out.println("╚══════════════════════════════════════════════════════╝");

        if (department.getSections().isEmpty()) {
            System.out.println("  (No sections under this department yet)");
            return;
        }

        for (Section section : department.getSections()) {
            System.out.println();
            System.out.println("  ┌─ Section: " + section.getSectionName() + " [" + section.getSectionId() + "]");
            System.out.println("  │  Course  : " + section.getCourseId());
            System.out.println("  │  Schedule: " + section.getSchedule());
            System.out.println("  │  Room    : " + section.getRoom());
            System.out.println("  │  Capacity: " + section.getEnrolledStudents().size() + "/" + section.getMaxCapacity()
                    + (section.isFull() ? " [FULL]" : " [" + section.getAvailableSlots() + " slots available]"));

            // Instructor
            if (section.getInstructorId() != null && instructorService != null) {
                Instructor instr = instructorService.findInstructorById(section.getInstructorId());
                System.out.println("  │  Instructor: " + (instr != null ? instr.getFullName() : "ID: " + section.getInstructorId()));
            } else {
                System.out.println("  │  Instructor: (Not assigned)");
            }

            // Students
            System.out.println("  │");
            if (section.getEnrolledStudents().isEmpty()) {
                System.out.println("  │  Students: (No students enrolled)");
            } else {
                System.out.println("  │  Students:");
                int num = 1;
                for (Student st : section.getEnrolledStudents()) {
                    System.out.printf("  │    %d. [%s] %s (%s Year %d)%n",
                            num++, st.getStudentId(), st.getFullName(), st.getProgram(), st.getYearLevel());
                }
            }
            System.out.println("  └────────────────────────────────────────────────");
        }
    }

    @Override
    public void addDepartment(Department department) throws DuplicateIdException {
        for (Department d : departments) {
            if (d.getDepartmentId().equalsIgnoreCase(department.getDepartmentId())) {
                throw new DuplicateIdException("Department ID '" + department.getDepartmentId() + "' already exists!");
            }
        }
        departments.add(department);
    }

    @Override
    public void addSectionToDepartment(String departmentId, Section section) {
        Department dept = findDepartmentById(departmentId);
        if (dept == null) {
            System.out.println("  [!] Department not found: " + departmentId);
            return;
        }
        dept.getSections().add(section);
        if (!allSections.contains(section)) {
            allSections.add(section);
        }
    }

    @Override
    public Department findDepartmentById(String departmentId) {
        for (Department d : departments) {
            if (d.getDepartmentId().equalsIgnoreCase(departmentId)) return d;
        }
        return null;
    }

    @Override
    public List<Department> getAllDepartments() {
        return departments;
    }

    @Override
    public Section findSectionById(String sectionId) {
        for (Section s : allSections) {
            if (s.getSectionId().equalsIgnoreCase(sectionId)) return s;
        }
        return null;
    }

    @Override
    public List<Section> getAllSections() {
        return allSections;
    }
}
