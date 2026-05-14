package com.enrollment;

import com.enrollment.entities.*;
import com.enrollment.exceptions.*;
import com.enrollment.interfaces.*;
import com.enrollment.services.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit Tests for Enrollment System business logic.
 * Follows the AAA Pattern: Arrange, Act, Assert.
 * Minimum 7 tests to earn full bonus.
 */
public class EnrollmentSystemTest {

    private IStudentService studentService;
    private IInstructorService instructorService;
    private ICourseService courseService;
    private ITuitionService tuitionService;
    private IEnrollmentService enrollmentService;

    @BeforeEach
    public void setUp() {
        // Fresh instances before each test — no leftover state
        courseService     = new CourseServiceImpl();
        instructorService = new InstructorServiceImpl();
        studentService    = new StudentServiceImpl();
        tuitionService    = new TuitionServiceImpl();
        enrollmentService = new EnrollmentServiceImpl(courseService, instructorService);
    }

    // ══════════════════════════════════════════
    // TEST 1: Capacity validation — reject when full
    // ══════════════════════════════════════════
    @Test
    public void test_EnrollStudent_WhenSectionIsFull_ThrowsSectionFullException() throws Exception {
        // ARRANGE
        Section section = new Section("SEC-T1", "BSIT-1A", "CS101", 2, "MWF", "Room 1");
        Student alice   = new Student("S01", "Alice", "Doe", "a@test.com", "BSIT", 1);
        Student bob     = new Student("S02", "Bob",   "Doe", "b@test.com", "BSIT", 1);
        Student charlie = new Student("S03", "Charlie","Doe","c@test.com", "BSIT", 1);

        // Fill the section
        enrollmentService.enrollStudentInSection(alice, section);
        enrollmentService.enrollStudentInSection(bob, section);

        // ACT & ASSERT: 3rd student must be rejected
        assertThrows(SectionFullException.class, () ->
                enrollmentService.enrollStudentInSection(charlie, section),
                "System should throw SectionFullException when section is full!"
        );
        assertEquals(2, section.getEnrolledStudents().size(),
                "Section size must stay at max capacity (2).");
    }

    // ══════════════════════════════════════════
    // TEST 2: Successful enrollment under capacity
    // ══════════════════════════════════════════
    @Test
    public void test_EnrollStudent_WhenSectionHasSpace_EnrollsSuccessfully() throws Exception {
        // ARRANGE
        Section section = new Section("SEC-T2", "BSIT-1B", "CS101", 5, "TTH", "Room 2");
        Student juan    = new Student("S04", "Juan", "Reyes", "j@test.com", "BSIT", 1);

        // ACT
        enrollmentService.enrollStudentInSection(juan, section);

        // ASSERT
        assertEquals(1, section.getEnrolledStudents().size());
        assertEquals("S04", section.getEnrolledStudents().get(0).getStudentId());
    }

    // ══════════════════════════════════════════
    // TEST 3: Tuition calculation
    // ══════════════════════════════════════════
    @Test
    public void test_CalculateFee_CorrectAmountForGivenUnits() {
        // ARRANGE
        int units = 6;
        double expectedFee = ITuitionService.FEE_PER_UNIT * units; // 1500 * 6 = 9000

        // ACT
        TuitionFeePayment payment = tuitionService.calculateFee("STU001", units);

        // ASSERT
        assertEquals(expectedFee, payment.getTotalFee(), 0.001,
                "Fee should be PHP 9000.00 for 6 units at PHP 1500/unit");
    }

    // ══════════════════════════════════════════
    // TEST 4: Scholarship discount applied correctly
    // ══════════════════════════════════════════
    @Test
    public void test_ApplyScholarship_50Percent_HalvesTuition() throws Exception {
        // ARRANGE
        TuitionFeePayment payment = tuitionService.calculateFee("STU002", 6); // PHP 9000
        tuitionService.createPaymentRecord(payment);

        // ACT
        tuitionService.applyScholarship("STU002", 50.0);

        // ASSERT
        TuitionFeePayment record = tuitionService.getPaymentRecord("STU002");
        assertEquals(4500.0, record.getDiscountedTotalFee(), 0.001,
                "50% scholarship should reduce PHP 9000 to PHP 4500");
    }

    // ══════════════════════════════════════════
    // TEST 5: Valid payment reduces balance
    // ══════════════════════════════════════════
    @Test
    public void test_MakePayment_ValidAmount_ReducesBalance() throws Exception {
        // ARRANGE
        TuitionFeePayment payment = tuitionService.calculateFee("STU003", 3); // PHP 4500
        tuitionService.createPaymentRecord(payment);

        // ACT
        tuitionService.makePayment("STU003", 1500.0);

        // ASSERT
        assertEquals(3000.0, tuitionService.getRemainingBalance("STU003"), 0.001,
                "Balance should be PHP 3000 after paying PHP 1500 out of PHP 4500");
    }

    // ══════════════════════════════════════════
    // TEST 6: Overpayment throws exception
    // ══════════════════════════════════════════
    @Test
    public void test_MakePayment_Overpayment_ThrowsException() throws Exception {
        // ARRANGE
        TuitionFeePayment payment = tuitionService.calculateFee("STU004", 3); // PHP 4500
        tuitionService.createPaymentRecord(payment);

        // ACT & ASSERT
        assertThrows(InvalidPaymentAmountException.class, () ->
                tuitionService.makePayment("STU004", 99999.0),
                "Overpayment should throw InvalidPaymentAmountException"
        );
    }

    // ══════════════════════════════════════════
    // TEST 7: Prerequisite check — reject if not passed
    // ══════════════════════════════════════════
    @Test
    public void test_EnrollStudent_PrerequisiteNotMet_ThrowsException() throws Exception {
        // ARRANGE
        Course cs102 = new Course("CS102", "Programming 1", 3, "Intro to Java");
        Course cs103 = new Course("CS103", "Data Structures", 3, "DS & Algo");
        courseService.addCourse(cs102);
        courseService.addCourse(cs103);
        courseService.addPrerequisite("CS103", "CS102"); // CS103 requires CS102

        Section section = new Section("SEC-T3", "BSCS-2A", "CS103", 30, "MWF", "Lab 1");
        Student newStudent = new Student("S05", "Maya", "Cruz", "m@test.com", "BSCS", 2);
        // Note: newStudent has NOT passed CS102

        // ACT & ASSERT
        assertThrows(PrerequisiteNotMetException.class, () ->
                enrollmentService.enrollStudentInSection(newStudent, section),
                "Should throw PrerequisiteNotMetException when CS102 not passed"
        );
    }

    // ══════════════════════════════════════════
    // TEST 8: Prerequisite check — allow if passed
    // ══════════════════════════════════════════
    @Test
    public void test_EnrollStudent_PrerequisiteMet_EnrollsSuccessfully() throws Exception {
        // ARRANGE
        Course cs102 = new Course("CS102", "Programming 1", 3, "Intro to Java");
        Course cs103 = new Course("CS103", "Data Structures", 3, "DS & Algo");
        courseService.addCourse(cs102);
        courseService.addCourse(cs103);
        courseService.addPrerequisite("CS103", "CS102");

        Section section = new Section("SEC-T4", "BSCS-2A", "CS103", 30, "MWF", "Lab 1");
        Student pedro   = new Student("S06", "Pedro", "Lim", "p@test.com", "BSCS", 2);
        pedro.getPassedCourseIds().add("CS102"); // Pedro passed CS102

        // ACT
        enrollmentService.enrollStudentInSection(pedro, section);

        // ASSERT
        assertEquals(1, section.getEnrolledStudents().size(),
                "Pedro should be enrolled since he passed the prerequisite");
    }

    // ══════════════════════════════════════════
    // TEST 9: Duplicate student ID rejected
    // ══════════════════════════════════════════
    @Test
    public void test_AddStudent_DuplicateId_ThrowsException() throws Exception {
        // ARRANGE
        Student s1 = new Student("S07", "Lena", "Park", "lena@test.com", "BSIT", 1);
        Student s2 = new Student("S07", "Lena", "Copy", "copy@test.com", "BSIT", 1); // same ID!
        studentService.addStudent(s1);

        // ACT & ASSERT
        assertThrows(DuplicateIdException.class, () ->
                studentService.addStudent(s2),
                "Duplicate student ID should throw DuplicateIdException"
        );
    }

    // ══════════════════════════════════════════
    // TEST 10: Full payment shows correct status
    // ══════════════════════════════════════════
    @Test
    public void test_FullPayment_ShowsFullyPaidStatus() throws Exception {
        // ARRANGE
        TuitionFeePayment payment = tuitionService.calculateFee("STU006", 3); // PHP 4500
        tuitionService.createPaymentRecord(payment);

        // ACT
        tuitionService.makePayment("STU006", 4500.0);

        // ASSERT
        TuitionFeePayment record = tuitionService.getPaymentRecord("STU006");
        assertTrue(record.isFullyPaid(), "Student should be fully paid after paying the exact balance.");
        assertEquals("FULLY PAID", record.getStatus());
        assertEquals(0.0, record.getRemainingBalance(), 0.001);
    }
}
