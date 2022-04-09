package DTO.loan;

import bankingSystem.timeline.loan.Loan;
import bankingSystem.timeline.loan.PartInLoan;
import bankingSystem.timeline.loan.PaymentInfo;

import java.util.ArrayList;
import java.util.List;

public class LoanInformationDTO {
    private final String loanNameID;
    private final String borrowerName;
    private final String loanCategory;
    private final int loanStartSum;
    private final int loanSumOfTimeUnit;
    private final double loanInterest;
    private final int timeUnitsBetweenPayments;
    private final String loanStatus;
    private List<PartInLoanDTO> lenderSetAndAmounts;
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
    private int nextPaymentTimeUnit;
    private double sumAmountToPayEveryTimeUnit;
    private List<PaymentsDTO> paymentsListInDTO;
    private int lastPaymentTimeunit;
    private double amountToPayNextPayment;
    private double fundToPayNextPayment;
    private double interestToPayNextPayment;
    private int numberOfUnpaidPayments;

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
        interestAmount = loan.interestLoanToPayAmount();
        sumAmount = fundAmount + interestAmount;
        paidFund = loan.loanPaidFund();
        paidInterest = loan.loanPaidInterest();
        fundLeftToPay = loan.loanFundLeftToPay();
        interestLeftToPay = loan.loanInterestLeftToPay();
        pendingMoney = loan.getPendingMoney();
        nextPaymentTimeUnit = loan.getLastPaidTimeUnit() + timeUnitsBetweenPayments; //Todo: check if correct
        sumAmountToPayEveryTimeUnit = loan.sumAmountToPayEveryTimeUnit();
        paymentsListInDTO = paymentsListInDTO(loan.getPaymentInfoList());
        lastPaymentTimeunit = loan.getLastPaidTimeUnit();
        amountToPayNextPayment = loan.amountOfNextPayment();
        fundToPayNextPayment = loan.fundOfNextPayment();
        interestToPayNextPayment = loan.interestOfNextPayment();
        numberOfUnpaidPayments = loan.howManyUnpaidPayments();
    }

    private List<PaymentsDTO> paymentsListInDTO(List<PaymentInfo> i_paymentsInfoSet) {
        List<PaymentsDTO> listToReturn = new ArrayList<>();

        for (PaymentInfo i_PaymentInfo : i_paymentsInfoSet) {
            listToReturn.add(new PaymentsDTO(i_PaymentInfo.getPaymentTimeUnit(), i_PaymentInfo.getFundPayment(),
                    i_PaymentInfo.getInterestPayment(), i_PaymentInfo.getPaymentSum(), i_PaymentInfo.isWasItPaid()));
        }

        return listToReturn;
    }

    private List<PartInLoanDTO> lenderSetAndAmountInDTO(List<PartInLoan> i_LenderSetAndAmount) {
        List<PartInLoanDTO> setToReturn = new ArrayList<>();

        for (PartInLoan i_PartInLoan : i_LenderSetAndAmount) {
            setToReturn.add(new PartInLoanDTO(i_PartInLoan.getLender().getClientName(), i_PartInLoan.getAmountOfLoan()));
        }

        return setToReturn;
    }

    public int getLastPaymentTimeunit() {
        return lastPaymentTimeunit;
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

    public List<PartInLoanDTO> getLenderSetAndAmounts() {
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

    public int getNextPaymentTimeUnit() {
        return nextPaymentTimeUnit;
    }

    public double getSumAmountToPayEveryTimeUnit() {
        return sumAmountToPayEveryTimeUnit;
    }

    public List<PaymentsDTO> getPaymentsList() {
        return paymentsListInDTO;
    }

    public double getAmountToPayNextPayment() {
        return amountToPayNextPayment;
    }

    public double getFundToPayNextPayment() {
        return fundToPayNextPayment;
    }

    public double getInterestToPayNextPayment() {
        return interestToPayNextPayment;
    }

    public int getNumberOfUnpaidPayments() {
        return numberOfUnpaidPayments;
    }

    /*public int amountOfLenderPartInLoan(BankAccount borrower) {

    }*/

    public double amountOfUnPaidPayments() {
        int sum = 0;

        for (PaymentsDTO singlePayment : paymentsListInDTO) {
            if (singlePayment.isWasItPaid() == false) {
                sum += singlePayment.getPaymentSum();
            }
        }

        return sum;
    }
}
