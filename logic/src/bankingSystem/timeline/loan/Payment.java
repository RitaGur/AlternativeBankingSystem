package bankingSystem.timeline.loan;

import java.sql.Time;

public class Payment {
    private double m_SumLeftToPay;  // m_InitialSumLeftToPay + m_InterestLeftToPay
    private double m_FundLeftToPay; // KEREN - Fund TODO: delete
    private double m_InterestLeftToPay; // RIBIT - Interest
    private final double f_FundToPayEveryTimeUnit;
    private final double f_InterestToPayEveryTimeUnit;
    private final double f_SumToPayEveryTimeUnit;
    private final double f_InitialFund;
    private final double f_InitialInterest;

    public Payment(double i_InitialSum, double i_InitialInterest, double i_SumOfTimeUnit, double i_TimeUnitsBetweenPayments) {
        double timeUnitsToPay = i_SumOfTimeUnit / i_TimeUnitsBetweenPayments;
        f_InitialFund = m_FundLeftToPay = i_InitialSum;
        f_InitialInterest = m_InterestLeftToPay = i_InitialInterest * i_InitialSum;
        m_SumLeftToPay = m_FundLeftToPay + m_InterestLeftToPay;
        f_InterestToPayEveryTimeUnit = i_SumOfTimeUnit / timeUnitsToPay;
        f_FundToPayEveryTimeUnit = i_SumOfTimeUnit / timeUnitsToPay;
        f_SumToPayEveryTimeUnit = f_FundToPayEveryTimeUnit + f_InterestToPayEveryTimeUnit;
    }

    public void addPayment() {
        m_FundLeftToPay -= f_FundToPayEveryTimeUnit;
        m_InterestLeftToPay -= f_InterestToPayEveryTimeUnit;
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
}

