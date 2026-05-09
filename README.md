# Interface-Driven Enrollment System

A Java console application implementing a college enrollment system using strict Interface-based architecture.

## Project Structure

```
src/
 └── main/java/com/enrollment/
      ├── entities/       - Data classes (Student, Instructor, Course, Section, Department, TuitionFeePayment)
      ├── interfaces/     - Service contracts (IStudentService, IInstructorService, etc.)
      ├── services/       - Concrete implementations (StudentServiceImpl, etc.)
      ├── exceptions/     - Custom exceptions (SectionFullException, DuplicateIdException, etc.)
      └── cli/            - Main.java (console UI)
 └── test/java/com/enrollment/
      └── EnrollmentSystemTest.java - 13 unit tests
```

## How to Run

### Prerequisites
- Java 17 or higher

### Compile & Run (Main App)
```bash
chmod +x run.sh
./run.sh
```

### Compile & Run (Unit Tests)
```bash
./run.sh test
```

### Manual Compile
```bash
mkdir -p out
find src/main/java -name "*.java" | xargs javac -d out
java -cp out com.enrollment.cli.Main
```

## Features

- **Student CRUD** - Add, view, update, delete students
- **Instructor Management** - Add instructors and assign them to sections
- **Course Management** - CRUD with prerequisite course support
- **Enrollment** - Enroll students with capacity checks and prerequisite validation
- **Tuition** - Calculate fees (PHP 1,500/unit + PHP 2,500 misc), process payments, apply scholarship discounts
- **Department Hierarchy View** - See Department → Sections → Instructor + Students
- **Custom Exceptions** - SectionFullException, DuplicateIdException, PrerequisiteNotMetException, InvalidPaymentAmountException
- **13 Unit Tests** covering all major business logic paths

## Demo Data (Pre-loaded on Start)

| Type | Data |
|------|------|
| Courses | CS101, CS102 (prereq: CS101), MATH101, ENG101 |
| Instructors | I001 - Dr. Maria Santos, I002 - Prof. Juan dela Cruz |
| Sections | SEC001 BSIT-1A (cap: 3), SEC002 BSIT-1B (cap: 30), SEC003 BSIT-2A |
| Students | S001 Alice, S002 Bob, S003 Clara (has CS101 completed) |
| Department | D001 - College of Computer Studies |
