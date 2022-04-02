package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;

public class PartInLoan {
    private final BankAccount m_Lender;
    private int m_AmountOfLoan;

    public PartInLoan(BankAccount i_Lender, int i_AmountOfLoan) {
        this.m_Lender = i_Lender;
        this.m_AmountOfLoan = i_AmountOfLoan;
    }

    public int getAmountOfLoan() {
        return m_AmountOfLoan;
    }

    public BankAccount getLender() {
        return m_Lender;
    }

    //TODO: a list of PartInLoan
}
