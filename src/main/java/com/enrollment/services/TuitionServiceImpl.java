package com.enrollment.services;

import com.enrollment.entities.TuitionFeePayment;
import com.enrollment.exceptions.InvalidPaymentAmountException;
import com.enrollment.interfaces.ITuitionService;
import java.util.ArrayList;
import java.util.List;

public class TuitionServiceImpl implements ITuitionService {

    private final List<TuitionFeePayment> paymentRecords = new ArrayList<>();

    @Override
    public TuitionFeePayment calculateFee(String studentId, int totalUnits) {
        double fee = FEE_PER_UNIT * totalUnits;
        String paymentId = "PAY-" + studentId + "-" + System.currentTimeMillis();
        return new TuitionFeePayment(paymentId, studentId, fee);
    }

    @Override
    public void createPaymentRecord(TuitionFeePayment payment) {
        // replace existing record if any, otherwise add
        paymentRecords.removeIf(p -> p.getStudentId().equalsIgnoreCase(payment.getStudentId()));
        paymentRecords.add(payment);
    }

    @Override
    public void makePayment(String studentId, double amount) throws InvalidPaymentAmountException {
        if (amount <= 0) {
            throw new InvalidPaymentAmountException("Payment amount must be greater than zero.");
        }

        TuitionFeePayment record = getPaymentRecord(studentId);
        if (record == null) {
            throw new InvalidPaymentAmountException("No tuition record found for student: " + studentId);
        }

        double remaining = record.getRemainingBalance();
        if (amount > remaining) {
            // We'll still allow overpayment but warn — some schools allow this
            throw new InvalidPaymentAmountException(
                String.format("Payment of PHP %.2f exceeds remaining balance of PHP %.2f. Please enter the correct amount.", amount, remaining)
            );
        }

        record.addPayment(amount);
        System.out.printf("  [OK] Payment of PHP %.2f accepted. New balance: PHP %.2f%n",
                amount, record.getRemainingBalance());
    }

    @Override
    public double getRemainingBalance(String studentId) {
        TuitionFeePayment record = getPaymentRecord(studentId);
        if (record == null) return 0.0;
        return record.getRemainingBalance();
    }

    @Override
    public void applyScholarship(String studentId, double discountPercentage) {
        TuitionFeePayment record = getPaymentRecord(studentId);
        if (record == null) {
            System.out.println("  [!] No payment record found for student: " + studentId);
            return;
        }
        record.setHasScholarship(true);
        record.setScholarshipDiscount(discountPercentage / 100.0);
        System.out.printf("  [OK] Scholarship applied: %.0f%% discount for student %s%n",
                discountPercentage, studentId);
    }

    @Override
    public TuitionFeePayment getPaymentRecord(String studentId) {
        for (TuitionFeePayment p : paymentRecords) {
            if (p.getStudentId().equalsIgnoreCase(studentId)) return p;
        }
        return null;
    }

    @Override
    public List<TuitionFeePayment> getAllPaymentRecords() {
        return paymentRecords;
    }
}
