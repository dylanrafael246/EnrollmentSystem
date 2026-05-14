package com.enrollment.interfaces;

import com.enrollment.entities.TuitionFeePayment;
import com.enrollment.exceptions.InvalidPaymentAmountException;
import java.util.List;

public interface ITuitionService {
    // PHP fee per unit
    double FEE_PER_UNIT = 1500.0;

    TuitionFeePayment calculateFee(String studentId, int totalUnits);
    void makePayment(String studentId, double amount) throws InvalidPaymentAmountException;
    double getRemainingBalance(String studentId);
    void applyScholarship(String studentId, double discountPercentage);
    TuitionFeePayment getPaymentRecord(String studentId);
    List<TuitionFeePayment> getAllPaymentRecords();
    void createPaymentRecord(TuitionFeePayment payment);
}
