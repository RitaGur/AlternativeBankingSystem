package bankingSystem.timeline.loan;

public class PaymentInfo {
    private final int paymentTimeUnit;
    private final double fundPayment;
    private final double interestPayment;
    private final int paymentSum;
    private boolean wasItPaid;

    public PaymentInfo(int paymentTimeUnit, double fundAtPayment, double interestAtPayment, int paymentSum, boolean wasItPaid) {
        this.paymentTimeUnit = paymentTimeUnit;
        this.fundPayment = fundAtPayment;
        this.interestPayment = interestAtPayment;
        this.paymentSum = paymentSum;
        this.wasItPaid = wasItPaid;
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

    public boolean isWasItPaid() {
        return wasItPaid;
    }
}
