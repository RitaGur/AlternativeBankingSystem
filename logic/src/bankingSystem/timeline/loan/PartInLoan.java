package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;

public class PartInLoan {
    private final BankAccount m_Lender;
    private int m_AmountOfLoan;
    private final double m_AmountToReceiveEveryTimeUnit;
    private double m_AmountToReceiveNextPayment;

    public PartInLoan(BankAccount i_Lender, int i_AmountOfLoan, int i_LoanStartSum, double i_TotalReturnSumOfLoan, int i_SumOfTimeUnit, int i_TimeunitsBetweenPayments) { // i_TotalReturnSumOfLoan = fund + interest
        this.m_Lender = i_Lender;
        this.m_AmountOfLoan = i_AmountOfLoan;
        double amountPercentageOfLoan = (double) (i_AmountOfLoan * 100 / i_LoanStartSum) / (double)100; // /100 for %
        m_AmountToReceiveEveryTimeUnit = (amountPercentageOfLoan * i_TotalReturnSumOfLoan) / (i_SumOfTimeUnit / i_TimeunitsBetweenPayments);
        m_AmountToReceiveNextPayment = m_AmountToReceiveEveryTimeUnit;
    }

    public void updateAmountToReceiveNextPaymentRisk() {
        m_AmountToReceiveNextPayment += m_AmountToReceiveEveryTimeUnit;
    }

    public void updateAmountToReceiveNextPaymentActiveAgain() {
        m_AmountToReceiveNextPayment = m_AmountToReceiveEveryTimeUnit;
    }

    public int getAmountOfLoan() {
        return m_AmountOfLoan;
    }

    public BankAccount getLender() {
        return m_Lender;
    }

    public double getAmountToReceiveEveryTimeUnit() {
        return m_AmountToReceiveEveryTimeUnit;
    }

    public double getAmountToReceiveNextPayment() {
        return m_AmountToReceiveNextPayment;
    }
}