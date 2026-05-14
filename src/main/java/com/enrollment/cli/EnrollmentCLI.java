package com.enrollment.cli;

import com.enrollment.entities.*;
import com.enrollment.exceptions.*;
import com.enrollment.interfaces.*;
import com.enrollment.services.*;

import java.util.List;
import java.util.Scanner;

/**
 * Main Console/CLI for the Interface-Driven Enrollment System.
 * All user input goes through here; services handle the logic.
 */
public class EnrollmentCLI {

    private static final Scanner sc = new Scanner(System.in);

    // Services (interface types — decoupled from impl)
    private static IStudentService    studentService;
    private static IInstructorService instructorService;
    private static ICourseService     courseService;
    private static ITuitionService    tuitionService;
    private static IEnrollmentService enrollmentService;

    public static void main(String[] args) {
        // Called from Main.java — this is where everything starts
        // Wire up implementations
        courseService     = new CourseServiceImpl();
        instructorService = new InstructorServiceImpl();
        studentService    = new StudentServiceImpl();
        tuitionService    = new TuitionServiceImpl();
        enrollmentService = new EnrollmentServiceImpl(courseService, instructorService);

        // Seed some demo data so there's something to work with right away
        DataSeeder.seed(studentService, instructorService, courseService, tuitionService, enrollmentService);

        printBanner();
        mainMenu();
    }

    // ────────────────────────────────────────────
    //  MAIN MENU
    // ────────────────────────────────────────────
    private static void mainMenu() {
        while (true) {
            System.out.println("\n╔═══════════════════════════════╗");
            System.out.println("║      MAIN MENU                ║");
            System.out.println("╠═══════════════════════════════╣");
            System.out.println("║  1. Student Management        ║");
            System.out.println("║  2. Instructor Management     ║");
            System.out.println("║  3. Course Management         ║");
            System.out.println("║  4. Enrollment Management     ║");
            System.out.println("║  5. Tuition & Payments        ║");
            System.out.println("║  6. Department Hierarchy      ║");
            System.out.println("║  0. Exit                      ║");
            System.out.println("╚═══════════════════════════════╝");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> studentMenu();
                case 2 -> instructorMenu();
                case 3 -> courseMenu();
                case 4 -> enrollmentMenu();
                case 5 -> tuitionMenu();
                case 6 -> departmentHierarchyMenu();
                case 0 -> {
                    System.out.println("\n  Goodbye! See you next semester :)");
                    System.exit(0);
                }
                default -> System.out.println("  [!] Invalid choice. Try again.");
            }
        }
    }

    // ────────────────────────────────────────────
    //  STUDENT MENU
    // ────────────────────────────────────────────
    private static void studentMenu() {
        while (true) {
            System.out.println("\n── STUDENT MANAGEMENT ──────────");
            System.out.println("  1. Add Student");
            System.out.println("  2. Update Student");
            System.out.println("  3. Remove Student");
            System.out.println("  4. View All Students");
            System.out.println("  5. Search Student by ID");
            System.out.println("  6. Mark Course as Passed (Prerequisite)");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addStudent();
                case 2 -> updateStudent();
                case 3 -> removeStudent();
                case 4 -> viewAllStudents();
                case 5 -> searchStudent();
                case 6 -> markCoursePassed();
                case 0 -> { return; }
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void addStudent() {
        System.out.println("\n  -- Add New Student --");
        System.out.print("  Student ID   : "); String id = sc.nextLine().trim();
        System.out.print("  First Name   : "); String fn = sc.nextLine().trim();
        System.out.print("  Last Name    : "); String ln = sc.nextLine().trim();
        System.out.print("  Email        : "); String email = sc.nextLine().trim();
        System.out.print("  Program (e.g. BSIT): "); String prog = sc.nextLine().trim();
        System.out.print("  Year Level   : "); int yr = readInt();

        Student student = new Student(id, fn, ln, email, prog, yr);
        try {
            studentService.addStudent(student);
            System.out.println("  [OK] Student added: " + student.getFullName());
        } catch (DuplicateIdException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void updateStudent() {
        System.out.println("\n  -- Update Student --");
        System.out.print("  Enter Student ID to update: "); String id = sc.nextLine().trim();
        Student existing = studentService.findStudentById(id);
        if (existing == null) { System.out.println("  [!] Student not found."); return; }

        System.out.println("  Current: " + existing);
        System.out.print("  New First Name [" + existing.getFirstName() + "]: ");
        String fn = sc.nextLine().trim(); if (fn.isEmpty()) fn = existing.getFirstName();
        System.out.print("  New Last Name [" + existing.getLastName() + "]: ");
        String ln = sc.nextLine().trim(); if (ln.isEmpty()) ln = existing.getLastName();
        System.out.print("  New Email [" + existing.getEmail() + "]: ");
        String email = sc.nextLine().trim(); if (email.isEmpty()) email = existing.getEmail();
        System.out.print("  New Program [" + existing.getProgram() + "]: ");
        String prog = sc.nextLine().trim(); if (prog.isEmpty()) prog = existing.getProgram();
        System.out.print("  New Year Level [" + existing.getYearLevel() + "]: ");
        String yrStr = sc.nextLine().trim();
        int yr = yrStr.isEmpty() ? existing.getYearLevel() : parseIntSafe(yrStr, existing.getYearLevel());

        studentService.updateStudent(id, fn, ln, email, prog, yr);
        System.out.println("  [OK] Student updated.");
    }

    private static void removeStudent() {
        System.out.print("\n  Enter Student ID to remove: "); String id = sc.nextLine().trim();
        Student s = studentService.findStudentById(id);
        if (s == null) { System.out.println("  [!] Student not found."); return; }
        System.out.print("  Are you sure you want to remove " + s.getFullName() + "? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            studentService.removeStudent(id);
            System.out.println("  [OK] Student removed.");
        } else {
            System.out.println("  [!] Cancelled.");
        }
    }

    private static void viewAllStudents() {
        List<Student> list = studentService.getAllStudents();
        System.out.println("\n  ── All Students (" + list.size() + ") ─────────────");
        if (list.isEmpty()) { System.out.println("  (No students found)"); return; }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + list.get(i));
        }
    }

    private static void searchStudent() {
        System.out.print("\n  Enter Student ID: "); String id = sc.nextLine().trim();
        Student s = studentService.findStudentById(id);
        if (s == null) { System.out.println("  [!] Student not found."); return; }
        System.out.println("\n  " + s);
        System.out.println("  Passed Courses: " +
                (s.getPassedCourseIds().isEmpty() ? "None" : String.join(", ", s.getPassedCourseIds())));
    }

    private static void markCoursePassed() {
        System.out.print("\n  Student ID: "); String sid = sc.nextLine().trim();
        System.out.print("  Course ID  : "); String cid = sc.nextLine().trim();
        studentService.markCourseAsPassed(sid, cid);
    }

    // ────────────────────────────────────────────
    //  INSTRUCTOR MENU
    // ────────────────────────────────────────────
    private static void instructorMenu() {
        while (true) {
            System.out.println("\n── INSTRUCTOR MANAGEMENT ────────");
            System.out.println("  1. Add Instructor");
            System.out.println("  2. Update Instructor");
            System.out.println("  3. Remove Instructor");
            System.out.println("  4. View All Instructors");
            System.out.println("  5. Assign Instructor to Section");
            System.out.println("  6. View Instructor Details");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addInstructor();
                case 2 -> updateInstructor();
                case 3 -> removeInstructor();
                case 4 -> viewAllInstructors();
                case 5 -> assignInstructor();
                case 6 -> viewInstructorDetails();
                case 0 -> { return; }
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void addInstructor() {
        System.out.println("\n  -- Add New Instructor --");
        System.out.print("  Instructor ID  : "); String id = sc.nextLine().trim();
        System.out.print("  First Name     : "); String fn = sc.nextLine().trim();
        System.out.print("  Last Name      : "); String ln = sc.nextLine().trim();
        System.out.print("  Email          : "); String email = sc.nextLine().trim();
        System.out.print("  Specialization : "); String spec = sc.nextLine().trim();

        try {
            instructorService.addInstructor(new Instructor(id, fn, ln, email, spec));
            System.out.println("  [OK] Instructor added: " + fn + " " + ln);
        } catch (DuplicateIdException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void updateInstructor() {
        System.out.print("\n  Enter Instructor ID to update: "); String id = sc.nextLine().trim();
        Instructor existing = instructorService.findInstructorById(id);
        if (existing == null) { System.out.println("  [!] Instructor not found."); return; }

        System.out.println("  Current: " + existing);
        System.out.print("  New First Name [" + existing.getFirstName() + "]: ");
        String fn = sc.nextLine().trim(); if (fn.isEmpty()) fn = existing.getFirstName();
        System.out.print("  New Last Name [" + existing.getLastName() + "]: ");
        String ln = sc.nextLine().trim(); if (ln.isEmpty()) ln = existing.getLastName();
        System.out.print("  New Email [" + existing.getEmail() + "]: ");
        String email = sc.nextLine().trim(); if (email.isEmpty()) email = existing.getEmail();
        System.out.print("  New Specialization [" + existing.getSpecialization() + "]: ");
        String spec = sc.nextLine().trim(); if (spec.isEmpty()) spec = existing.getSpecialization();

        instructorService.updateInstructor(id, fn, ln, email, spec);
        System.out.println("  [OK] Instructor updated.");
    }

    private static void removeInstructor() {
        System.out.print("\n  Enter Instructor ID to remove: "); String id = sc.nextLine().trim();
        Instructor i = instructorService.findInstructorById(id);
        if (i == null) { System.out.println("  [!] Instructor not found."); return; }
        System.out.print("  Remove " + i.getFullName() + "? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            instructorService.removeInstructor(id);
            System.out.println("  [OK] Instructor removed.");
        }
    }

    private static void viewAllInstructors() {
        List<Instructor> list = instructorService.getAllInstructors();
        System.out.println("\n  ── All Instructors (" + list.size() + ") ──────────");
        if (list.isEmpty()) { System.out.println("  (No instructors found)"); return; }
        for (int i = 0; i < list.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + list.get(i));
        }
    }

    private static void assignInstructor() {
        viewAllInstructors();
        System.out.print("\n  Instructor ID : "); String instrId = sc.nextLine().trim();
        viewAllSections();
        System.out.print("  Section ID    : "); String sectId = sc.nextLine().trim();
        Section section = enrollmentService.findSectionById(sectId);
        if (section == null) { System.out.println("  [!] Section not found."); return; }
        instructorService.assignInstructorToSection(instrId, section);
    }

    private static void viewInstructorDetails() {
        System.out.print("\n  Instructor ID: "); String id = sc.nextLine().trim();
        System.out.println(instructorService.getInstructorDetails(id));
    }

    // ────────────────────────────────────────────
    //  COURSE MENU
    // ────────────────────────────────────────────
    private static void courseMenu() {
        while (true) {
            System.out.println("\n── COURSE MANAGEMENT ────────────");
            System.out.println("  1. Add Course");
            System.out.println("  2. Update Course");
            System.out.println("  3. Remove Course");
            System.out.println("  4. View All Courses");
            System.out.println("  5. Add Prerequisite to Course");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> addCourse();
                case 2 -> updateCourse();
                case 3 -> removeCourse();
                case 4 -> viewAllCourses();
                case 5 -> addPrerequisite();
                case 0 -> { return; }
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void addCourse() {
        System.out.println("\n  -- Add New Course --");
        System.out.print("  Course ID   : "); String id = sc.nextLine().trim();
        System.out.print("  Course Name : "); String name = sc.nextLine().trim();
        System.out.print("  Units       : "); int units = readInt();
        System.out.print("  Description : "); String desc = sc.nextLine().trim();

        try {
            courseService.addCourse(new Course(id, name, units, desc));
            System.out.println("  [OK] Course added: " + name);
        } catch (DuplicateIdException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void updateCourse() {
        System.out.print("\n  Course ID to update: "); String id = sc.nextLine().trim();
        var c = courseService.findCourseById(id);
        if (c == null) { System.out.println("  [!] Course not found."); return; }

        System.out.print("  New Name [" + c.getCourseName() + "]: ");
        String name = sc.nextLine().trim(); if (name.isEmpty()) name = c.getCourseName();
        System.out.print("  New Units [" + c.getUnits() + "]: ");
        String uStr = sc.nextLine().trim(); int units = uStr.isEmpty() ? c.getUnits() : parseIntSafe(uStr, c.getUnits());
        System.out.print("  New Description [" + c.getDescription() + "]: ");
        String desc = sc.nextLine().trim(); if (desc.isEmpty()) desc = c.getDescription();

        courseService.updateCourse(id, name, units, desc);
        System.out.println("  [OK] Course updated.");
    }

    private static void removeCourse() {
        System.out.print("\n  Course ID to remove: "); String id = sc.nextLine().trim();
        var c = courseService.findCourseById(id);
        if (c == null) { System.out.println("  [!] Course not found."); return; }
        System.out.print("  Remove " + c.getCourseName() + "? (y/n): ");
        if (sc.nextLine().trim().equalsIgnoreCase("y")) {
            courseService.removeCourse(id);
            System.out.println("  [OK] Course removed.");
        }
    }

    private static void viewAllCourses() {
        List<Course> list = courseService.getAllCourses();
        System.out.println("\n  ── All Courses (" + list.size() + ") ────────────────");
        if (list.isEmpty()) { System.out.println("  (No courses found)"); return; }
        for (Course c : list) System.out.println("  " + c);
    }

    private static void addPrerequisite() {
        viewAllCourses();
        System.out.print("\n  Course ID          : "); String cid = sc.nextLine().trim();
        System.out.print("  Prerequisite ID    : "); String pid = sc.nextLine().trim();
        courseService.addPrerequisite(cid, pid);
    }

    // ────────────────────────────────────────────
    //  ENROLLMENT MENU
    // ────────────────────────────────────────────
    private static void enrollmentMenu() {
        while (true) {
            System.out.println("\n── ENROLLMENT MANAGEMENT ─────────");
            System.out.println("  1. Enroll Student in Section");
            System.out.println("  2. Unenroll Student from Section");
            System.out.println("  3. Add Department");
            System.out.println("  4. Add Section to Department");
            System.out.println("  5. View All Sections");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> unenrollStudent();
                case 3 -> addDepartment();
                case 4 -> addSection();
                case 5 -> viewAllSections();
                case 0 -> { return; }
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void enrollStudent() {
        viewAllStudents();
        System.out.print("\n  Student ID : "); String sid = sc.nextLine().trim();
        Student student = studentService.findStudentById(sid);
        if (student == null) { System.out.println("  [!] Student not found."); return; }

        viewAllSections();
        System.out.print("  Section ID : "); String sectId = sc.nextLine().trim();
        Section section = enrollmentService.findSectionById(sectId);
        if (section == null) { System.out.println("  [!] Section not found."); return; }

        try {
            enrollmentService.enrollStudentInSection(student, section);
            System.out.println("  [OK] " + student.getFullName() + " enrolled in " + section.getSectionName());

            // Auto-create tuition record if not exists
            if (tuitionService.getPaymentRecord(sid) == null) {
                Course course = courseService.findCourseById(section.getCourseId());
                int units = (course != null) ? course.getUnits() : 3;
                TuitionFeePayment payment = tuitionService.calculateFee(sid, units);
                tuitionService.createPaymentRecord(payment);
                System.out.printf("  [INFO] Tuition record created: PHP %.2f%n", payment.getTotalFee());
            }

        } catch (SectionFullException | PrerequisiteNotMetException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void unenrollStudent() {
        System.out.print("\n  Student ID : "); String sid = sc.nextLine().trim();
        viewAllSections();
        System.out.print("  Section ID : "); String sectId = sc.nextLine().trim();
        Section section = enrollmentService.findSectionById(sectId);
        if (section == null) { System.out.println("  [!] Section not found."); return; }
        enrollmentService.unenrollStudentFromSection(sid, section);
    }

    private static void addDepartment() {
        System.out.println("\n  -- Add Department --");
        System.out.print("  Department ID   : "); String id = sc.nextLine().trim();
        System.out.print("  Department Name : "); String name = sc.nextLine().trim();
        System.out.print("  Dean Name       : "); String dean = sc.nextLine().trim();
        try {
            enrollmentService.addDepartment(new Department(id, name, dean));
            System.out.println("  [OK] Department added: " + name);
        } catch (DuplicateIdException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void addSection() {
        System.out.println("\n  -- Add Section to Department --");
        List<Department> depts = enrollmentService.getAllDepartments();
        if (depts.isEmpty()) { System.out.println("  [!] No departments. Add one first."); return; }
        System.out.println("  Departments:");
        depts.forEach(d -> System.out.println("    [" + d.getDepartmentId() + "] " + d.getDepartmentName()));
        System.out.print("  Department ID  : "); String deptId = sc.nextLine().trim();

        viewAllCourses();
        System.out.print("  Section ID     : "); String sid = sc.nextLine().trim();
        System.out.print("  Section Name   : "); String sname = sc.nextLine().trim();
        System.out.print("  Course ID      : "); String cid = sc.nextLine().trim();
        System.out.print("  Max Capacity   : "); int cap = readInt();
        System.out.print("  Schedule       : "); String sched = sc.nextLine().trim();
        System.out.print("  Room           : "); String room = sc.nextLine().trim();

        Section section = new Section(sid, sname, cid, cap, sched, room);
        enrollmentService.addSectionToDepartment(deptId, section);
        System.out.println("  [OK] Section added: " + sname);
    }

    private static void viewAllSections() {
        List<Section> list = enrollmentService.getAllSections();
        System.out.println("\n  ── All Sections (" + list.size() + ") ───────────────");
        if (list.isEmpty()) { System.out.println("  (No sections found)"); return; }
        for (Section s : list) System.out.println("  " + s);
    }

    // ────────────────────────────────────────────
    //  TUITION MENU
    // ────────────────────────────────────────────
    private static void tuitionMenu() {
        while (true) {
            System.out.println("\n── TUITION & PAYMENTS ────────────");
            System.out.println("  1. View Payment Record");
            System.out.println("  2. Make a Payment");
            System.out.println("  3. Apply Scholarship");
            System.out.println("  4. View All Payment Records");
            System.out.println("  5. Create/Recalculate Tuition");
            System.out.println("  0. Back");
            System.out.print("  Choice: ");

            int choice = readInt();
            switch (choice) {
                case 1 -> viewPaymentRecord();
                case 2 -> makePayment();
                case 3 -> applyScholarship();
                case 4 -> viewAllPayments();
                case 5 -> createTuition();
                case 0 -> { return; }
                default -> System.out.println("  [!] Invalid choice.");
            }
        }
    }

    private static void viewPaymentRecord() {
        System.out.print("\n  Student ID: "); String id = sc.nextLine().trim();
        TuitionFeePayment record = tuitionService.getPaymentRecord(id);
        if (record == null) { System.out.println("  [!] No payment record found."); return; }
        System.out.println("\n" + record);
    }

    private static void makePayment() {
        System.out.print("\n  Student ID    : "); String id = sc.nextLine().trim();
        TuitionFeePayment record = tuitionService.getPaymentRecord(id);
        if (record == null) { System.out.println("  [!] No payment record found."); return; }
        System.out.printf("  Remaining Balance: PHP %.2f%n", record.getRemainingBalance());
        System.out.print("  Amount to Pay  : PHP ");

        try {
            double amount = readDouble();
            tuitionService.makePayment(id, amount);
        } catch (InvalidPaymentAmountException e) {
            System.out.println("  [ERROR] " + e.getMessage());
        }
    }

    private static void applyScholarship() {
        System.out.print("\n  Student ID         : "); String id = sc.nextLine().trim();
        System.out.print("  Discount Percentage: "); double pct = readDouble();
        if (pct < 0 || pct > 100) {
            System.out.println("  [!] Invalid percentage. Must be 0-100.");
            return;
        }
        tuitionService.applyScholarship(id, pct);
        TuitionFeePayment record = tuitionService.getPaymentRecord(id);
        if (record != null) {
            System.out.printf("  [INFO] New discounted total: PHP %.2f%n", record.getDiscountedTotalFee());
        }
    }

    private static void viewAllPayments() {
        List<TuitionFeePayment> list = tuitionService.getAllPaymentRecords();
        System.out.println("\n  ── All Payment Records (" + list.size() + ") ────");
        if (list.isEmpty()) { System.out.println("  (No records)"); return; }
        for (TuitionFeePayment p : list) {
            System.out.println("  -------------------------------------------");
            System.out.println(p);
        }
    }

    private static void createTuition() {
        viewAllStudents();
        System.out.print("\n  Student ID : "); String sid = sc.nextLine().trim();
        Student s = studentService.findStudentById(sid);
        if (s == null) { System.out.println("  [!] Student not found."); return; }
        System.out.print("  Total Units: "); int units = readInt();
        TuitionFeePayment payment = tuitionService.calculateFee(sid, units);
        tuitionService.createPaymentRecord(payment);
        System.out.printf("  [OK] Tuition created: PHP %.2f for %s%n", payment.getTotalFee(), s.getFullName());
    }

    // ────────────────────────────────────────────
    //  DEPARTMENT HIERARCHY
    // ────────────────────────────────────────────
    private static void departmentHierarchyMenu() {
        List<Department> depts = enrollmentService.getAllDepartments();
        if (depts.isEmpty()) {
            System.out.println("\n  [!] No departments yet. Add one via Enrollment Management.");
            return;
        }

        System.out.println("\n  ── All Departments ──────────────────");
        for (int i = 0; i < depts.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + depts.get(i));
        }
        System.out.println("  0. Back");
        System.out.print("  Choose department to view: ");
        int choice = readInt();
        if (choice == 0 || choice > depts.size()) return;
        enrollmentService.viewDepartmentHierarchy(depts.get(choice - 1));
    }

    // ────────────────────────────────────────────
    //  HELPERS
    // ────────────────────────────────────────────
    private static int readInt() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.print("  [!] Please enter a valid number: ");
            }
        }
    }

    private static double readDouble() {
        while (true) {
            try {
                String line = sc.nextLine().trim();
                return Double.parseDouble(line);
            } catch (NumberFormatException e) {
                System.out.print("  [!] Please enter a valid amount: ");
            }
        }
    }

    private static int parseIntSafe(String str, int fallback) {
        try { return Integer.parseInt(str); }
        catch (NumberFormatException e) { return fallback; }
    }

    private static void printBanner() {
        System.out.println("╔═══════════════════════════════════════════════════╗");
        System.out.println("║   INTERFACE-DRIVEN ENROLLMENT SYSTEM  v1.0        ║");
        System.out.println("║   College of Computer Studies                     ║");
        System.out.println("║   Capstone Project — 2nd Year IT                  ║");
        System.out.println("╚═══════════════════════════════════════════════════╝");
        System.out.println("  [INFO] Demo data loaded. You can start right away!");
    }
}
