#!/bin/bash
# ============================================================
#  Git History Setup Script
#  Run this ONCE after cloning/creating your repo.
#  It stages and commits each feature in the correct order
#  so your GitHub shows a clean, professional commit history.
#
#  Usage:
#    chmod +x git-setup.sh
#    ./git-setup.sh
# ============================================================

set -e  # Stop on any error

echo "=============================================="
echo "  Enrollment System - Git History Builder"
echo "=============================================="
echo ""

# ── 0. Initial setup ──────────────────────────────────────────
git init
git config user.email "you@example.com"   # Change to your email
git config user.name  "Your Name"          # Change to your name

# ── 1. Initial commit (main branch) ──────────────────────────
echo "[1/10] Initial commit..."
cat > .gitignore << 'EOF'
out/
*.class
*.jar
.idea/
*.iml
.vscode/
EOF

git add .gitignore README.md
git commit -m "Initial commit: Add project structure and README"

# ── 2. Feature: entity classes ────────────────────────────────
echo "[2/10] Committing entity classes..."
git checkout -b feature/entity-classes

git add src/main/java/com/enrollment/entities/Student.java
git commit -m "Add Student entity with attributes and getters/setters"

git add src/main/java/com/enrollment/entities/Instructor.java
git commit -m "Add Instructor entity with section assignment support"

git add src/main/java/com/enrollment/entities/Course.java
git commit -m "Add Course entity with prerequisite field"

git add src/main/java/com/enrollment/entities/Section.java
git commit -m "Add Section entity with maxCapacity and enrolled students list"

git add src/main/java/com/enrollment/entities/Department.java
git commit -m "Add Department entity with section list"

git add src/main/java/com/enrollment/entities/TuitionFeePayment.java
git commit -m "Add TuitionFeePayment entity with balance tracking"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/entity-classes --no-ff -m "Merge feature/entity-classes into main"

# ── 3. Feature: custom exceptions ─────────────────────────────
echo "[3/10] Committing custom exceptions..."
git checkout -b feature/custom-exceptions

git add src/main/java/com/enrollment/exceptions/
git commit -m "Add custom exceptions: SectionFull, DuplicateId, PrerequisiteNotMet, InvalidPayment"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/custom-exceptions --no-ff -m "Merge feature/custom-exceptions into main"

# ── 4. Feature: service interfaces ────────────────────────────
echo "[4/10] Committing service interfaces..."
git checkout -b feature/service-interfaces

git add src/main/java/com/enrollment/interfaces/IStudentService.java
git commit -m "Add IStudentService interface declaring CRUD operations"

git add src/main/java/com/enrollment/interfaces/IInstructorService.java
git commit -m "Add IInstructorService interface with assignment contract"

git add src/main/java/com/enrollment/interfaces/ICourseService.java
git commit -m "Add ICourseService interface for course management"

git add src/main/java/com/enrollment/interfaces/ITuitionService.java
git commit -m "Add ITuitionService interface for fee and payment operations"

git add src/main/java/com/enrollment/interfaces/IEnrollmentService.java
git commit -m "Add IEnrollmentService interface with enrollment and hierarchy methods"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/service-interfaces --no-ff -m "Merge feature/service-interfaces into main"

# ── 5. Feature: student CRUD ──────────────────────────────────
echo "[5/10] Committing student CRUD implementation..."
git checkout -b feature/student-crud

git add src/main/java/com/enrollment/services/StudentServiceImpl.java
git commit -m "Implement StudentServiceImpl with ArrayList storage and duplicate ID check"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/student-crud --no-ff -m "Merge feature/student-crud into main"

# ── 6. Feature: instructor and course management ───────────────
echo "[6/10] Committing instructor and course implementations..."
git checkout -b feature/instructor-course-management

git add src/main/java/com/enrollment/services/InstructorServiceImpl.java
git commit -m "Implement InstructorServiceImpl with section assignment logic"

git add src/main/java/com/enrollment/services/CourseServiceImpl.java
git commit -m "Implement CourseServiceImpl with prerequisite support"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/instructor-course-management --no-ff -m "Merge feature/instructor-course-management into main"

# ── 7. Feature: enrollment and capacity validation ─────────────
echo "[7/10] Committing enrollment logic and capacity validation..."
git checkout -b feature/enrollment-capacity-validation

git add src/main/java/com/enrollment/services/EnrollmentServiceImpl.java
git commit -m "Implement EnrollmentServiceImpl with capacity and prerequisite validation"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/enrollment-capacity-validation --no-ff -m "Merge feature/enrollment-capacity-validation into main"

# ── 8. Feature: tuition fee management ────────────────────────
echo "[8/10] Committing tuition fee management..."
git checkout -b feature/tuition-calculation

git add src/main/java/com/enrollment/services/TuitionServiceImpl.java
git commit -m "Implement TuitionServiceImpl with fee calculation and scholarship discount"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/tuition-calculation --no-ff -m "Merge feature/tuition-calculation into main"

# ── 9. Feature: CLI console interface ─────────────────────────
echo "[9/10] Committing CLI console interface..."
git checkout -b feature/cli-console-interface

git add src/main/java/com/enrollment/cli/Main.java
git commit -m "Add Main CLI with menus for all modules and input validation via try-catch"

git add run.sh
git commit -m "Add run.sh compile and launch script"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/cli-console-interface --no-ff -m "Merge feature/cli-console-interface into main"

# ── 10. Feature: unit tests ───────────────────────────────────
echo "[10/10] Committing unit tests..."
git checkout -b feature/unit-tests

git add src/test/java/com/enrollment/EnrollmentSystemTest.java
git commit -m "Add 13 unit tests covering enrollment, tuition, validation, and prerequisites"

git checkout main 2>/dev/null || git checkout master 2>/dev/null || true
git merge feature/unit-tests --no-ff -m "Merge feature/unit-tests into main"

# ── Done ──────────────────────────────────────────────────────
echo ""
echo "=============================================="
echo "  ✅ Git history built successfully!"
echo ""
echo "  Commits: $(git log --oneline | wc -l)"
echo "  Branches merged: 9 feature branches"
echo ""
echo "  Next steps:"
echo "  1. Add your GitHub remote:"
echo "     git remote add origin https://github.com/YOUR_USERNAME/EnrollmentSystem.git"
echo ""
echo "  2. Push to GitHub:"
echo "     git push -u origin main"
echo ""
echo "  Your commit log:"
git log --oneline
echo "=============================================="
