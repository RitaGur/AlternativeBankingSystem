package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;

import java.util.ArrayList;
import java.util.List;

public class Loan {
    private final String f_LoanNameID;
    private final BankAccount f_LoanOwner; //The Borrower
    private final int f_LoanStartSum;
    private final int f_SumOfTimeUnit; // how many yaz - loan duration
    private final int f_TimeUnitsBetweenPayment; // how often (by yaz) you pay
    private final double f_Interest; //ribit - decimal number (0, 100]
    private Payment m_Payment;
    private List<PartInLoan> m_LendersAndAmountsSet;
    private LoanStatus m_LoanStatus;
    private int m_PendingMoney = 0; // raised money before activation
    private int m_BeginningTimeUnit;
    private int m_EndingTimeunit;
    private int m_LastPaidTimeUnit = 0;
    private final String f_LoanCategory;
    private List<PaymentInfo> m_PaymentInfoList;

    public Loan(String i_LoanNameID, BankAccount i_LoanOwner, int i_LoanStartSum, int i_SumOfTimeUnit,
                int i_HowOftenToPay, double i_Interest, String i_LoanCategory) {
        f_LoanOwner = i_LoanOwner;
        f_LoanNameID = i_LoanNameID;
        f_LoanStartSum = i_LoanStartSum;
        f_SumOfTimeUnit = i_SumOfTimeUnit;
        f_TimeUnitsBetweenPayment = i_HowOftenToPay;
        f_Interest = 0.01 * i_Interest;
        m_LendersAndAmountsSet = new ArrayList<>();
        m_Payment = new Payment(f_LoanStartSum, f_Interest, f_SumOfTimeUnit, f_TimeUnitsBetweenPayment);
        m_LoanStatus = LoanStatus.NEW;
        m_BeginningTimeUnit = -1;
        f_LoanCategory = i_LoanCategory;
        m_PaymentInfoList = new ArrayList<>();
        m_EndingTimeunit = m_BeginningTimeUnit + f_SumOfTimeUnit - 1;
    }

    public int getLastPaidTimeUnit() {
        return m_LastPaidTimeUnit;
    }

    public void updateEndingTimeunit(int i_CurrentTimeunit) {
        m_EndingTimeunit = i_CurrentTimeunit;
    }

    public int howManyTimeUnitsLeftForLoan(int i_CurrentTimeUnit) {
        return f_SumOfTimeUnit - i_CurrentTimeUnit;
    }

    public void addLender(BankClient i_NewLender, int i_AmountOfLoan) {
        m_LendersAndAmountsSet.add(new PartInLoan(i_NewLender, i_AmountOfLoan, f_LoanStartSum, m_Payment.getSumLeftToPay(), f_SumOfTimeUnit));
        i_NewLender.addAsLender(this);
    }

    public void addPaymentToPaymentInfoList(int paymentTimeUnit, boolean wasItPaid) {
        m_PaymentInfoList.add(new PaymentInfo(paymentTimeUnit, m_Payment.getFundToPayNextPayment(), m_Payment.getInterestToPayNextPayment(), (int)m_Payment.getAmountToPayNextPayment(), wasItPaid));
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

    public List<PartInLoan> getLendersSet() {
        return m_LendersAndAmountsSet;
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

    public double fundOfNextPayment() {
        return m_Payment.getFundToPayNextPayment();
    }

    public double interestOfNextPayment() {
        return m_Payment.getInterestToPayNextPayment();
    }

    public double amountOfNextPayment() {
        return m_Payment.getAmountToPayNextPayment();
    }

    public int getTimeUnitsBetweenPayment() {
        return f_TimeUnitsBetweenPayment;
    }

    public List<PaymentInfo> getPaymentInfoList() {
        return m_PaymentInfoList;
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

    private void updateLoanStatusToActive(int i_BeginningTimeUnit) throws Exception {
        if (m_PendingMoney == f_LoanStartSum) {
            m_LoanStatus = LoanStatus.ACTIVE;
            f_LoanOwner.addMoneyToAccount(m_PendingMoney, i_BeginningTimeUnit); //pay to borrower
            m_PendingMoney = 0;
            m_BeginningTimeUnit = i_BeginningTimeUnit;
            checkIfPaymentNeededAndPay(i_BeginningTimeUnit); //pay to lenders
        }
    }

    public void updateLoanStatusToPending() {
        m_LoanStatus = LoanStatus.PENDING;
    }

    public boolean isItPaymentTime(int i_CurrentTimeunit) {
        /*return (((i_CurrentTimeunit - (m_BeginningTimeUnit - 1) % f_TimeUnitsBetweenPayment) == 0 &&
                (i_CurrentTimeunit - m_BeginningTimeUnit > 0)) || (f_TimeUnitsBetweenPayment == 1)) &&
                (m_LoanStatus == LoanStatus.ACTIVE || m_LoanStatus == LoanStatus.RISK);*/

        return (((i_CurrentTimeunit - (m_BeginningTimeUnit - 1)) % f_TimeUnitsBetweenPayment) == 0 &&
                (m_LoanStatus == LoanStatus.ACTIVE || m_LoanStatus == LoanStatus.RISK));
    }

    private void payForLoan(int i_CurrentTimeunit) throws Exception {
        m_Payment.addPayment();
        addPaymentToPaymentInfoList(i_CurrentTimeunit, true);
        m_LastPaidTimeUnit = i_CurrentTimeunit;
        takePaymentsFromBorrowerToLenders(i_CurrentTimeunit);
    }

    public void checkIfPaymentNeededAndPay(int i_CurrentTimeUnit) throws Exception {
        if(isItPaymentTime(i_CurrentTimeUnit)) {
            if (f_LoanOwner.getAccountBalance() >= m_Payment.getAmountToPayNextPayment()) {
                m_Payment.addPayment();
                addPaymentToPaymentInfoList(i_CurrentTimeUnit, true);
                m_LastPaidTimeUnit = i_CurrentTimeUnit;
                takePaymentsFromBorrowerToLenders(i_CurrentTimeUnit);
                if (m_LoanStatus == LoanStatus.RISK) { // In case the lender paid all his last debt
                    m_LoanStatus = LoanStatus.ACTIVE;
                    m_Payment.updateNextPaymentActiveAgain();
                    updateNextPaymentsOfLendersActiveAgain();
                }
                checkIfLoanIsFinished(i_CurrentTimeUnit); // can be finished even in RISK
            } else {
                m_LoanStatus = LoanStatus.RISK;
                addPaymentToPaymentInfoList(i_CurrentTimeUnit, false);
                m_Payment.updateNextPaymentRisk();
                updateNextPaymentsOfLendersRisk();
                throw new Exception("Loan owner does not have enough money to pay his payment.");
            }

        }
    }

    private void checkIfLoanIsFinished(int i_CurrentTimeUnit) {
        if (m_Payment.getSumLeftToPay() < 1) {
            m_LoanStatus = LoanStatus.FINISHED;
            updateEndingTimeunit(i_CurrentTimeUnit);
        }
    }

    private void updateNextPaymentsOfLendersRisk() {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.updateAmountToReceiveNextPaymentRisk();
        }
    }

    private void updateNextPaymentsOfLendersActiveAgain() {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.updateAmountToReceiveNextPaymentActiveAgain();
        }
    }

    public boolean canTheBorrowerPayForLoanThisTimeunit() {
        return f_LoanOwner.getAccountBalance() >= m_Payment.getAmountToPayNextPayment();
    }

    private void takePaymentsFromBorrowerToLenders(int i_currentTimeUnit) throws Exception {
        for (PartInLoan lenderPart : m_LendersAndAmountsSet) {
            lenderPart.getLender().addMoneyToAccount(lenderPart.getAmountToReceiveNextPayment(), i_currentTimeUnit);
            f_LoanOwner.withdrawMoneyFromAccount(lenderPart.getAmountToReceiveNextPayment(), i_currentTimeUnit);
        }
        //what if he does not have enough money - we come here after we chose to pay this loan with knowing he has the money
    }

    public double sumAmountToPayEveryTimeUnit() {
        return m_Payment.getSumToPayEveryTimeUnit();
    }

    public int howManyUnpaidPayments() {
        int counter = 0;

        for (PaymentInfo singlePayment : m_PaymentInfoList) {
            if (singlePayment.isWasItPaid() == false) {
                counter++;
            }
        }

        return counter;
    }
}
