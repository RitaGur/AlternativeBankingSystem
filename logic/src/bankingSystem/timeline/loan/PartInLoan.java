package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;

public class PartInLoan {
    private final BankAccount m_Lender;
    private int m_AmountOfLoan;
    private final double m_AmountToReceiveEveryTimeUnit;

    public PartInLoan(BankAccount i_Lender, int i_AmountOfLoan, int i_LoanStartSum, double i_TotalReturnSumOfLoan) { // i_TotalReturnSumOfLoan = fund + interest
        this.m_Lender = i_Lender;
        this.m_AmountOfLoan = i_AmountOfLoan;
        double amountPercentageOfLoan = i_AmountOfLoan * 100 / i_LoanStartSum;
        m_AmountToReceiveEveryTimeUnit = amountPercentageOfLoan * i_TotalReturnSumOfLoan;
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
}