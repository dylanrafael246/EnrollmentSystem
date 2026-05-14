package com.enrollment.cli;

import com.enrollment.entities.*;
import com.enrollment.exceptions.DuplicateIdException;
import com.enrollment.interfaces.*;

/**
 * Seeds demo data so the system has something to interact with on first run.
 * Think of this as a "database populate" script.
 */
public class DataSeeder {

    public static void seed(
            IStudentService studentService,
            IInstructorService instructorService,
            ICourseService courseService,
            ITuitionService tuitionService,
            IEnrollmentService enrollmentService
    ) {
        try {
            // ── COURSES ──
            Course c1 = new Course("CS101", "Introduction to Computing",          3, "Basics of computing and IT");
            Course c2 = new Course("CS102", "Programming 1 (Java)",               3, "Introduction to Java programming");
            Course c3 = new Course("CS103", "Data Structures & Algorithms",       3, "Lists, Trees, Graphs, Sorting");
            Course c4 = new Course("CS201", "Object-Oriented Programming",        3, "OOP principles in Java");
            Course c5 = new Course("MATH101","Discrete Mathematics",              3, "Logic, sets, relations, graph theory");
            courseService.addCourse(c1);
            courseService.addCourse(c2);
            courseService.addCourse(c3);
            courseService.addCourse(c4);
            courseService.addCourse(c5);

            // CS103 requires CS102 (prerequisite demo)
            courseService.addPrerequisite("CS103", "CS102");
            // CS201 requires CS102
            courseService.addPrerequisite("CS201", "CS102");

            // ── INSTRUCTORS ──
            Instructor i1 = new Instructor("INS001", "Maria", "Santos",   "msantos@school.edu", "Software Engineering");
            Instructor i2 = new Instructor("INS002", "Jose",  "Reyes",    "jreyes@school.edu",  "Data Structures");
            Instructor i3 = new Instructor("INS003", "Ana",   "Cruz",     "acruz@school.edu",   "Mathematics");
            instructorService.addInstructor(i1);
            instructorService.addInstructor(i2);
            instructorService.addInstructor(i3);

            // ── STUDENTS ──
            Student s1 = new Student("STU001", "Juan",    "Dela Cruz", "juan@student.edu", "BSIT", 1);
            Student s2 = new Student("STU002", "Maria",   "Garcia",    "maria@student.edu","BSIT", 1);
            Student s3 = new Student("STU003", "Pedro",   "Lim",       "pedro@student.edu","BSCS", 2);
            Student s4 = new Student("STU004", "Ana",     "Reyes",     "ana@student.edu",  "BSCS", 2);
            Student s5 = new Student("STU005", "Carlo",   "Mendoza",   "carlo@student.edu","BSIT", 1);
            studentService.addStudent(s1);
            studentService.addStudent(s2);
            studentService.addStudent(s3);
            studentService.addStudent(s4);
            studentService.addStudent(s5);

            // Pedro and Ana already passed CS102 (so they can enroll in CS103)
            studentService.markCourseAsPassed("STU003", "CS102");
            studentService.markCourseAsPassed("STU004", "CS102");

            // ── DEPARTMENTS ──
            Department dept1 = new Department("DEPT01", "College of Computer Studies", "Dr. Roberto Tan");
            Department dept2 = new Department("DEPT02", "College of Engineering",      "Dr. Elena Flores");
            enrollmentService.addDepartment(dept1);
            enrollmentService.addDepartment(dept2);

            // ── SECTIONS ──
            Section sec1 = new Section("SEC001", "BSIT-1A", "CS101", 30, "MWF 7:30-9:00 AM",  "Room 101");
            Section sec2 = new Section("SEC002", "BSIT-1B", "CS102", 30, "TTH 9:00-10:30 AM", "Room 102");
            Section sec3 = new Section("SEC003", "BSCS-2A", "CS103",  3, "MWF 1:00-2:30 PM",  "Lab 201");  // capacity=3 for demo
            Section sec4 = new Section("SEC004", "BSCS-2B", "CS201", 25, "TTH 3:00-4:30 PM",  "Lab 202");
            enrollmentService.addSectionToDepartment("DEPT01", sec1);
            enrollmentService.addSectionToDepartment("DEPT01", sec2);
            enrollmentService.addSectionToDepartment("DEPT01", sec3);
            enrollmentService.addSectionToDepartment("DEPT01", sec4);

            // Assign instructors
            instructorService.assignInstructorToSection("INS001", sec1);
            instructorService.assignInstructorToSection("INS001", sec2);
            instructorService.assignInstructorToSection("INS002", sec3);
            instructorService.assignInstructorToSection("INS002", sec4);

            // ── ENROLL STUDENTS ──
            enrollmentService.enrollStudentInSection(s1, sec1);  // Juan -> BSIT-1A (CS101)
            enrollmentService.enrollStudentInSection(s2, sec1);  // Maria -> BSIT-1A (CS101)
            enrollmentService.enrollStudentInSection(s5, sec1);  // Carlo -> BSIT-1A (CS101)
            enrollmentService.enrollStudentInSection(s3, sec3);  // Pedro -> BSCS-2A (CS103) - has prereq
            enrollmentService.enrollStudentInSection(s4, sec3);  // Ana   -> BSCS-2A (CS103) - has prereq
            // SEC003 now has 2/3 — one slot left for demo!

            // ── TUITION RECORDS ──
            TuitionFeePayment p1 = tuitionService.calculateFee("STU001", 3);
            TuitionFeePayment p2 = tuitionService.calculateFee("STU002", 3);
            TuitionFeePayment p3 = tuitionService.calculateFee("STU003", 3);
            TuitionFeePayment p4 = tuitionService.calculateFee("STU004", 3);
            TuitionFeePayment p5 = tuitionService.calculateFee("STU005", 3);
            tuitionService.createPaymentRecord(p1);
            tuitionService.createPaymentRecord(p2);
            tuitionService.createPaymentRecord(p3);
            tuitionService.createPaymentRecord(p4);
            tuitionService.createPaymentRecord(p5);

            // Partial payment demo
            p1.addPayment(2000.0); // Juan paid PHP 2000

            // Scholarship demo
            tuitionService.applyScholarship("STU004", 50.0); // Ana has 50% scholarship

            System.out.println("  [SEEDER] Demo data loaded successfully.");

        } catch (Exception e) {
            System.out.println("  [SEEDER ERROR] " + e.getMessage());
        }
    }
}
