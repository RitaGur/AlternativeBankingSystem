package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Loan implements LoanInterface {
    private final String f_LoanNameID;
    private final BankAccount f_LoanOwner; //The Borrower
    private final int f_LoanStartSum;
    private final int f_SumOfTimeUnit; // how many yaz - loan duration
    private final int f_TimeUnitsBetweenPayment; // how often (by yaz) you pay
    private final double f_Interest; //ribit - decimal number (0, 100]
    private Payment m_Payment;
    private Set<PartInLoan> m_LendersSet;
    private LoanStatus m_LoanStatus;
    private int m_PendingMoney = 0; // raised money before activation
    private int m_BeginningTimeUnit;

    public int getLastPaidTimeUnit() {
        return m_LastPaidTimeUnit;
    }

    private int m_LastPaidTimeUnit = 0;
    private final String f_LoanCategory;

    public Loan(String i_LoanNameID, BankAccount i_LoanOwner, int i_LoanStartSum, int i_SumOfTimeUnit,
                int i_HowOftenToPay, double i_Interest, int i_LoanBeginningTimeUnit, String i_LoanCategory) {
        f_LoanOwner = i_LoanOwner;
        f_LoanNameID = i_LoanNameID;
        f_LoanStartSum = i_LoanStartSum;
        f_SumOfTimeUnit = i_SumOfTimeUnit;
        f_TimeUnitsBetweenPayment = i_HowOftenToPay;
        f_Interest = i_Interest;
        m_LendersSet = new HashSet<>();
        m_Payment = new Payment(f_LoanStartSum, f_Interest, f_SumOfTimeUnit, f_TimeUnitsBetweenPayment);
        m_LoanStatus = LoanStatus.NEW;
        m_BeginningTimeUnit = i_LoanBeginningTimeUnit;
        f_LoanCategory = i_LoanCategory;
    }

    @Override
    public int howManyTimeUnitsLeftForLoan(int i_CurrentTimeUnit) {
        return f_SumOfTimeUnit - i_CurrentTimeUnit;
    }

    @Override
    public Set<PartInLoan> lendersList() {
        return m_LendersSet;
    }

    public void addLender(BankClient i_NewLender, int i_AmountOfLoan) {
        m_LendersSet.add(new PartInLoan(i_NewLender, i_AmountOfLoan));
    }

    public void timeUnitPayment() { // as monthly payment
        m_Payment.addPayment();
    }

    public double loanSumLeftToPay() {
        return m_Payment.getSumLeftToPay();
    }

    public double loanFundLeftToPay() {
        return m_Payment.getFundLeftToPay();
    }

    public double loanInterestLeftToPay() {
        return m_Payment.getInterestLeftToPay();
    }

    public double loanPaidFund() {
        return m_Payment.paidFundAmount();
    }

    public double loanPaidInterest() {
        return m_Payment.paidInterestAmount();
    }

    public double interestLoanToPayAmount() {
        return m_Payment.getInitialInterest();
    }

    public int getPendingMoney() {
        return m_PendingMoney;
    }

    public int getSumOfTimeUnit() {
        return f_SumOfTimeUnit;
    }

    public int getBeginningTimeUnit() {
        return m_BeginningTimeUnit;
    }

    public String getLoanNameID() {
        return f_LoanNameID;
    }

    public BankAccount getLoanOwner() {
        return f_LoanOwner;
    }

    public Set<PartInLoan> getLendersSet() {
        return m_LendersSet; // clone?
    }

    public LoanStatus getLoanStatus() {
        return m_LoanStatus;
    }

    public String getLoanCategory() {
        return f_LoanCategory;
    }

    public int getLoanStartSum() {
        return f_LoanStartSum;
    }

    public double getInterest() {
        return f_Interest;
    }

    public int getTimeUnitsBetweenPayment() {
        return f_TimeUnitsBetweenPayment;
    }

    public void changeLoanStatus(LoanStatus i_LoanStatus) {
        m_LoanStatus = i_LoanStatus;
    }

    public void addToPendingMoney(int i_AmountToAdd, int i_BeginningTimeUnit) throws Exception {
        if (m_PendingMoney == 0 && m_LoanStatus == LoanStatus.NEW) {
            m_LoanStatus = LoanStatus.PENDING;
        }
        if (m_LoanStatus == LoanStatus.PENDING) {
            m_PendingMoney += i_AmountToAdd;
            updateLoanStatusToActive(i_BeginningTimeUnit);
        }
        else {
            throw new Exception("Loan status is not pending or new");
        }
    }

    private void updateLoanStatusToActive(int i_BeginningTimeUnit) {
        if (m_PendingMoney == f_LoanStartSum) {
            m_LoanStatus = LoanStatus.ACTIVE;
            f_LoanOwner.addMoneyToAccount(m_PendingMoney);
            m_PendingMoney = 0;
            m_BeginningTimeUnit = i_BeginningTimeUnit;
        }
    }

    public void updateLoanStatusToPending() {
        m_LoanStatus = LoanStatus.PENDING;
    }

    public void checkIfPaymentNeededAndPay(int i_CurrentTimeUnit) { // TODO: use in Banking System
        if (i_CurrentTimeUnit - (m_BeginningTimeUnit - 1)  % f_TimeUnitsBetweenPayment == 0 ) {
            if (f_LoanOwner.getAccountBalance() >= m_Payment.getFundToPayEveryTimeUnit() + m_Payment.getInterestToPayEveryTimeUnit())
            m_Payment.addPayment();
            m_LastPaidTimeUnit = i_CurrentTimeUnit;
            // TODO: add to lenders and whithdraw from borrower

        }
    }

    public void payToLenders() {
        for (PartInLoan i_PartInLoan: m_LendersSet) {
            i_PartInLoan.getLender().addMoneyToAccount(m_Payment.getSumToPayEveryTimeUnit());
        }
        // TODO: in stream
    }

    public double sumAmountToPayEveryTimeUnit() {
        return m_Payment.getSumToPayEveryTimeUnit();
    }
}
