package DTO.loan;

public class PaymentsDTO {
    private final int paymentTimeUnit;
    private final double fundPayment;
    private final double interestPayment;
    private final int paymentSum;

    public PaymentsDTO(int paymentTimeUnit, double fundAtPayment, double interestAtPayment, int paymentSum) {
        this.paymentTimeUnit = paymentTimeUnit;
        this.fundPayment = fundAtPayment;
        this.interestPayment = interestAtPayment;
        this.paymentSum = paymentSum;
    }

    public int getPaymentTimeUnit() {
        return paymentTimeUnit;
    }

    public double getFundPayment() {
        return fundPayment;
    }

    public double getInterestPayment() {
        return interestPayment;
    }

    public int getPaymentSum() {
        return paymentSum;
    }
}
