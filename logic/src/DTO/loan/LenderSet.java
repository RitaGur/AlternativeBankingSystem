package DTO.loan;

public class LenderSet {
    private String lenderName;
    private int amountOfLoan;

    public LenderSet(String lenderName, int amountOfLoan) {
        this.lenderName = lenderName;
        this.amountOfLoan = amountOfLoan;
    }

    public String getLenderName() {
        return lenderName;
    }

    public int getAmountOfLoan() {
        return amountOfLoan;
    }
}
