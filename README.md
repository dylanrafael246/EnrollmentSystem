# Interface-Driven Enrollment System

A console-based (CLI) enrollment system built in Java as a Capstone Project for 2nd Year IT.
Demonstrates **Interface Architecture**, **CRUD**, **Business Logic**, **Custom Exceptions**, and **JUnit Testing**.

---

## Features

### Baseline (Required)
- ✅ Strict Interface Architecture (`IStudentService`, `IEnrollmentService`, etc.)
- ✅ Full CRUD for Students, Instructors, and Courses
- ✅ Section capacity validation with `SectionFullException`
- ✅ Tuition calculation (`PHP 1500 × units`)
- ✅ Payment processing and balance tracking
- ✅ Department → Section → Student hierarchy view

### Bonus Features Implemented
- 🎓 **Prerequisite Checking** — can't enroll in CS103 without passing CS102 first
- 💸 **Scholarship Discounts** — apply a percentage discount to tuition
- 🛡️ **Duplicate ID Prevention** — `DuplicateIdException` for Student, Instructor, Course, Department
- 🔢 **Input Validation** — try-catch on all Scanner inputs, no crashes on bad input
- 🧪 **10 JUnit Unit Tests** — covering capacity, tuition, prerequisites, scholarships, duplicates
- 🌱 **Demo Data Seeder** — preloaded data so you can test right away

---

## Project Structure

```
EnrollmentSystem/
├── src/
│   ├── main/java/com/enrollment/
│   │   ├── entities/          ← Data classes (Student, Instructor, Course, etc.)
│   │   ├── exceptions/        ← Custom exceptions
│   │   ├── interfaces/        ← Service contracts (IStudentService, etc.)
│   │   ├── services/          ← Implementations (*ServiceImpl)
│   │   └── cli/               ← Console UI (EnrollmentCLI, DataSeeder)
│   └── test/java/com/enrollment/
│       └── EnrollmentSystemTest.java  ← 10 JUnit 5 tests
└── pom.xml                    ← Maven build file
```

---

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Run the App
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.enrollment.cli.EnrollmentCLI"
```

Or build a JAR and run it:
```bash
mvn package
java -jar target/EnrollmentSystem.jar
```

### Run Tests
```bash
mvn test
```

---

## Git Workflow (How this was developed)

This project followed the **Feature Branch Workflow**:

```
main
 ├── feature/project-setup
 ├── feature/entity-classes
 ├── feature/service-interfaces
 ├── feature/student-crud
 ├── feature/instructor-crud
 ├── feature/course-crud
 ├── feature/enrollment-service
 ├── feature/tuition-service
 ├── feature/capacity-validation
 ├── feature/prerequisite-checking
 ├── feature/scholarship-logic
 ├── feature/cli-menus
 ├── feature/data-seeder
 └── feature/unit-tests
```

### Commit Message Convention
```
Add Student entity and constructor
Implement IStudentService interface
Add StudentServiceImpl with duplicate ID check
Fix capacity validation logic in EnrollmentServiceImpl
Add prerequisite checking in enrollStudentInSection
Create tuition calculation and payment logic
Add scholarship discount feature to TuitionService
Write JUnit tests for enrollment and tuition logic
Add CLI menus for all services
Add DataSeeder for demo data on startup
```

---

## GitHub Setup Instructions

```bash
# 1. Initialize repository
git init
git add .
git commit -m "Initial project setup with all entities and structure"

# 2. Create GitHub repo, then push
git remote add origin https://github.com/YOUR_USERNAME/EnrollmentSystem.git
git branch -M main
git push -u origin main

# 3. Example feature branch workflow
git checkout -b feature/student-crud
# ... do your work ...
git add .
git commit -m "Add Student CRUD with duplicate ID validation"
git push origin feature/student-crud
# Then go to GitHub → Compare & pull request → Merge
```

---

## Default Tuition Rate

| Rate       | Value         |
|------------|---------------|
| Per Unit   | PHP 1,500.00  |
| 3-unit course | PHP 4,500.00 |
| 6-unit load | PHP 9,000.00 |

---

## Test Coverage

| Test # | What it tests |
|--------|---------------|
| 1 | Section full → `SectionFullException` thrown |
| 2 | Enrollment succeeds when space available |
| 3 | Tuition calculation: units × PHP 1500 |
| 4 | 50% scholarship halves tuition |
| 5 | Valid payment reduces balance |
| 6 | Overpayment → `InvalidPaymentAmountException` |
| 7 | Prerequisite not met → `PrerequisiteNotMetException` |
| 8 | Prerequisite met → enrollment succeeds |
| 9 | Duplicate student ID → `DuplicateIdException` |
| 10 | Full payment → status shows "FULLY PAID" |

---

*Capstone Project — College of Computer Studies*
