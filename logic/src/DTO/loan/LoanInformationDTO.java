package DTO.loan;

import bankingSystem.timeline.loan.Loan;
import bankingSystem.timeline.loan.PartInLoan;

import java.util.HashSet;
import java.util.Set;

public class LoanInformationDTO {
    private final String loanNameID;
    private final String borrowerName;
    private final String loanCategory;
    private final int loanStartSum;
    private final int loanSumOfTimeUnit;
    private final double loanInterest;
    private final int timeUnitsBetweenPayments;
    private final String loanStatus;
    private Set<PartInLoanDTO> lenderSetAndAmounts;
    private int pendingMoney;
    private final int beginningTimeUnit;
    private final int endingTimeUnit;
    private final int fundAmount;
    private final double interestAmount;
    private final double sumAmount;
    private double paidFund;
    private double paidInterest;
    private double fundLeftToPay;
    private double interestLeftToPay;

    public LoanInformationDTO(Loan loan) {
        loanNameID = loan.getLoanNameID();
        borrowerName = loan.getLoanOwner().getClientName();
        loanCategory = loan.getLoanCategory();
        loanStartSum = loan.getLoanStartSum();
        loanSumOfTimeUnit = loan.getSumOfTimeUnit();
        loanInterest = loan.getInterest();
        timeUnitsBetweenPayments = loan.getTimeUnitsBetweenPayment();
        loanStatus = loan.getLoanStatus().toString();
        lenderSetAndAmounts = lenderSetAndAmountInDTO(loan.getLendersSet());
        beginningTimeUnit = loan.getBeginningTimeUnit();
        endingTimeUnit = beginningTimeUnit + loanSumOfTimeUnit - 1; //TODO: check if right
        fundAmount = loan.getLoanStartSum();
        interestAmount = loan.interestLoanToPay();
        sumAmount = fundAmount + interestAmount;
        paidFund = loan.loanPaidFund();
        paidInterest = loan.loanPaidInterest();
        fundLeftToPay = loan.loanFundLeftToPay();
        interestLeftToPay = loan.loanInterestLeftToPay();
        pendingMoney = loan.getPendingMoney();
    }

    private Set<PartInLoanDTO> lenderSetAndAmountInDTO(Set<PartInLoan> i_LenderSetAndAmount) {
        Set<PartInLoanDTO> setToReturn = new HashSet<>();

        for (PartInLoan i_PartInLoan : i_LenderSetAndAmount) {
            setToReturn.add(new PartInLoanDTO(i_PartInLoan.getLender().getClientName(), i_PartInLoan.getAmountOfLoan()));
        }

        return setToReturn;
    }

    public String getLoanNameID() {
        return loanNameID;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public String getLoanCategory() {
        return loanCategory;
    }

    public int getLoanStartSum() {
        return loanStartSum;
    }

    public int getLoanSumOfTimeUnit() {
        return loanSumOfTimeUnit;
    }

    public double getLoanInterest() {
        return loanInterest;
    }

    public int getTimeUnitsBetweenPayments() {
        return timeUnitsBetweenPayments;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public Set<PartInLoanDTO> getLenderSetAndAmounts() {
        return lenderSetAndAmounts;
    }

    public int getPendingMoney() {
        return pendingMoney;
    }

    public int getBeginningTimeUnit() {
        return beginningTimeUnit;
    }

    public int getEndingTimeUnit() {
        return endingTimeUnit;
    }

    public int getFundAmount() {
        return fundAmount;
    }

    public double getInterestAmount() {
        return interestAmount;
    }

    public double getSumAmount() {
        return sumAmount;
    }

    public double getPaidFund() {
        return paidFund;
    }

    public double getPaidInterest() {
        return paidInterest;
    }

    public double getFundLeftToPay() {
        return fundLeftToPay;
    }

    public double getInterestLeftToPay() {
        return interestLeftToPay;
    }
}
