package com.enrollment.entities;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TuitionFeePayment {
    private String paymentId;
    private String studentId;
    private double totalFee;        // total amount due
    private double amountPaid;      // total amount already paid
    private LocalDateTime lastPaymentDate;
    private boolean hasScholarship;
    private double scholarshipDiscount; // percentage e.g. 0.20 = 20%

    public TuitionFeePayment(String paymentId, String studentId, double totalFee) {
        this.paymentId = paymentId;
        this.studentId = studentId;
        this.totalFee = totalFee;
        this.amountPaid = 0.0;
        this.hasScholarship = false;
        this.scholarshipDiscount = 0.0;
    }

    // Getters
    public String getPaymentId()          { return paymentId; }
    public String getStudentId()          { return studentId; }
    public double getTotalFee()           { return totalFee; }
    public double getAmountPaid()         { return amountPaid; }
    public LocalDateTime getLastPaymentDate() { return lastPaymentDate; }
    public boolean hasScholarship()       { return hasScholarship; }
    public double getScholarshipDiscount(){ return scholarshipDiscount; }

    // Setters / update methods
    public void setTotalFee(double totalFee)         { this.totalFee = totalFee; }
    public void setAmountPaid(double amountPaid)     { this.amountPaid = amountPaid; }
    public void setHasScholarship(boolean b)         { this.hasScholarship = b; }
    public void setScholarshipDiscount(double d)     { this.scholarshipDiscount = d; }

    public double getRemainingBalance() {
        double discountedFee = totalFee * (1.0 - scholarshipDiscount);
        return discountedFee - amountPaid;
    }

    public double getDiscountedTotalFee() {
        return totalFee * (1.0 - scholarshipDiscount);
    }

    public void addPayment(double amount) {
        this.amountPaid += amount;
        this.lastPaymentDate = LocalDateTime.now();
    }

    public boolean isFullyPaid() {
        return getRemainingBalance() <= 0;
    }

    public String getStatus() {
        if (isFullyPaid()) return "FULLY PAID";
        if (amountPaid > 0) return "PARTIALLY PAID";
        return "UNPAID";
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm");
        String lastPaid = (lastPaymentDate != null) ? lastPaymentDate.format(fmt) : "N/A";
        return String.format(
            "Payment ID: %s | Student: %s\n" +
            "  Total Fee    : PHP %.2f\n" +
            "  Scholarship  : %s (%.0f%% off)\n" +
            "  Discounted   : PHP %.2f\n" +
            "  Amount Paid  : PHP %.2f\n" +
            "  Balance      : PHP %.2f\n" +
            "  Status       : %s\n" +
            "  Last Payment : %s",
            paymentId, studentId,
            totalFee,
            hasScholarship ? "YES" : "NO", scholarshipDiscount * 100,
            getDiscountedTotalFee(),
            amountPaid,
            getRemainingBalance(),
            getStatus(),
            lastPaid
        );
    }
}
