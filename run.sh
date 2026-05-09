#!/bin/bash
# ============================================================
#  Enrollment System - Compile & Run Script
#  Usage:
#    ./run.sh          -> compile and run main app
#    ./run.sh test     -> compile and run unit tests
# ============================================================

SRC="src/main/java"
TEST_SRC="src/test/java"
OUT="out"
MAIN_CLASS="com.enrollment.cli.Main"
TEST_CLASS="com.enrollment.EnrollmentSystemTest"

echo "Compiling..."
mkdir -p $OUT

# Gather all source files
MAIN_FILES=$(find $SRC -name "*.java")
TEST_FILES=$(find $TEST_SRC -name "*.java")

javac -d $OUT $MAIN_FILES
if [ $? -ne 0 ]; then
    echo "Compilation FAILED."
    exit 1
fi

if [ "$1" == "test" ]; then
    # Compile test files too
    javac -cp $OUT -d $OUT $TEST_FILES
    if [ $? -ne 0 ]; then
        echo "Test compilation FAILED."
        exit 1
    fi
    echo ""
    echo "Running unit tests..."
    echo ""
    java -cp $OUT $TEST_CLASS
else
    echo ""
    echo "Launching Enrollment System..."
    echo ""
    java -cp $OUT $MAIN_CLASS
fi
