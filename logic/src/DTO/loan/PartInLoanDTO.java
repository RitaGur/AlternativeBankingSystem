package DTO.loan;

import bankingSystem.timeline.bankAccount.BankAccount;

public class PartInLoanDTO {
    private final String m_LenderName;
    private final int m_AmountOfLoan;

    public PartInLoanDTO(String i_LenderName, int i_AmountOfLoan) {
        this.m_LenderName = i_LenderName;
        this.m_AmountOfLoan = i_AmountOfLoan;
    }

    public String getLenderName() {
        return m_LenderName;
    }

    public int getAmountOfLoan() {
        return m_AmountOfLoan;
    }
}
