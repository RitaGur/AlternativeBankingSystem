package bankingSystem.timeline.loan;

public class Payment {
    private double m_SumLeftToPay;  // m_InitialSumLeftToPay + m_InterestLeftToPay
    private double m_FundLeftToPay; // KEREN - Fund
    private double m_InterestLeftToPay; // RIBIT - Interest
    private final double f_FundToPayEveryTimeUnit;
    private final double f_InterestToPayEveryTimeUnit;
    private final double f_SumToPayEveryTimeUnit;
    private final double f_InitialFund;
    private final double f_InitialInterest;
    private double m_AmountToPayNextPayment;
    private double m_FundToPayNextPayment;
    private double m_InterestToPayNextPayment;


    public Payment(double i_InitialSum, double i_InitialInterest, double i_SumOfTimeUnit, double i_TimeUnitsBetweenPayments) {
        double timeUnitsToPay = i_SumOfTimeUnit / i_TimeUnitsBetweenPayments;
        f_InitialFund = m_FundLeftToPay = i_InitialSum;
        f_InitialInterest = m_InterestLeftToPay = i_InitialInterest * i_InitialSum;
        m_SumLeftToPay = m_FundLeftToPay + m_InterestLeftToPay;
        m_FundToPayNextPayment = f_FundToPayEveryTimeUnit = i_InitialSum / timeUnitsToPay;
        m_InterestToPayNextPayment = f_InterestToPayEveryTimeUnit = f_InitialInterest / timeUnitsToPay;
        f_SumToPayEveryTimeUnit = f_FundToPayEveryTimeUnit + f_InterestToPayEveryTimeUnit;
        m_AmountToPayNextPayment = m_InterestToPayNextPayment + m_FundToPayNextPayment;
    }

    public void addPayment(int howManyPaymentsToPay) {
        m_FundLeftToPay -= f_FundToPayEveryTimeUnit * howManyPaymentsToPay;
        m_InterestLeftToPay -= f_InterestToPayEveryTimeUnit * howManyPaymentsToPay;
        m_SumLeftToPay = m_FundLeftToPay + m_InterestLeftToPay;
    }

    /*public void addPayment() {
        m_FundLeftToPay -= m_FundToPayNextPayment;
        m_InterestLeftToPay -= m_InterestToPayNextPayment;
        m_SumLeftToPay = m_FundLeftToPay + m_InterestLeftToPay;
    }*/

    public void updateNextPaymentActiveAgain() {
        m_FundToPayNextPayment = f_FundToPayEveryTimeUnit;
        m_InterestToPayNextPayment = f_InterestToPayEveryTimeUnit;
        m_AmountToPayNextPayment = m_FundToPayNextPayment + m_InterestToPayNextPayment;
    }

    public void updateNextPaymentRisk() {
        m_FundToPayNextPayment += f_FundToPayEveryTimeUnit;
        m_InterestToPayNextPayment += f_InterestToPayEveryTimeUnit;
        m_AmountToPayNextPayment = m_FundToPayNextPayment + m_InterestToPayNextPayment;
    }

    public double getSumLeftToPay() {
        return m_SumLeftToPay;
    }

    public double getFundLeftToPay() {
        return m_FundLeftToPay;
    }

    public double getInterestLeftToPay() {
        return m_InterestLeftToPay;
    }

    public double paidFundAmount() {
        return f_InitialFund - m_FundLeftToPay;
    }

    public double paidInterestAmount() {
        return f_InitialInterest - m_InterestLeftToPay;
    }

    public double getFundToPayEveryTimeUnit() {
        return f_FundToPayEveryTimeUnit;
    }

    public double getInterestToPayEveryTimeUnit() {
        return f_InterestToPayEveryTimeUnit;
    }

    public double getSumToPayEveryTimeUnit() {
        return f_SumToPayEveryTimeUnit;
    }

    public double getInitialInterest() {
        return f_InitialInterest;
    }

    public double getAmountToPayNextPayment() {
        return m_AmountToPayNextPayment;
    }

    public double getFundToPayNextPayment() {
        return m_FundToPayNextPayment;
    }

    public double getInterestToPayNextPayment() {
        return m_InterestToPayNextPayment;
    }
}

