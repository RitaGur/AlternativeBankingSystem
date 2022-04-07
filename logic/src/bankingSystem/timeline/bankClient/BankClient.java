package bankingSystem.timeline.bankClient;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.loan.Loan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class BankClient implements BankAccount {
    private final String f_ClientName;
    private double m_AccountBalance;
    private Set<Loan> m_ClientAsLenderSet;
    private Set<Loan> m_ClientAsBorrowerSet;
    private List<RecentTransaction> m_RecentTransactionList;

    public BankClient(int i_AccountBalance, String i_ClientName) {
        f_ClientName = i_ClientName;
        m_AccountBalance = i_AccountBalance;
        m_ClientAsBorrowerSet = new HashSet<>();
        m_ClientAsLenderSet = new HashSet<>();
        m_RecentTransactionList = new Stack<>();
    }

    @Override
    public double getAccountBalance() {
        return m_AccountBalance;
    }

/*    @Override
    public void askForLoan(String i_LoanID, int i_LoanSum, int i_SumOfTimeUnit, int i_TimeUnitsBetweenPayments,
                           double i_Interest, int i_LoanBeginningTimeUnit, String i_LoanCategory) {
        m_ClientAsBorrowerSet.add(new Loan(i_LoanID, this, i_LoanSum, i_SumOfTimeUnit, i_TimeUnitsBetweenPayments,
                                   i_Interest, i_LoanBeginningTimeUnit, i_LoanCategory));
    }*/

    @Override
    public void putMoneyInInvest() {
        // m_LenderSet
    }

    @Override
    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit) {
        m_RecentTransactionList.add(new RecentTransaction(i_AmountToAdd, m_AccountBalance, i_TransactionTimeUnit));
        m_AccountBalance += i_AmountToAdd;
    }

    @Override
    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit) {
        m_RecentTransactionList.add(new RecentTransaction(i_AmountToWithdraw * (-1), m_AccountBalance, i_TransactionTimeUnit));
        m_AccountBalance -= i_AmountToWithdraw;
    }

    @Override
    public List<RecentTransaction> getLastTransactions() {
        return m_RecentTransactionList;
    }

    @Override
    public void addLastTransaction(int i_AmountOfTransaction, int i_TransactionTimeUnit) {
        m_RecentTransactionList.add(new RecentTransaction(i_AmountOfTransaction, m_AccountBalance, i_TransactionTimeUnit));
    }

    @Override
    public String getClientName() {
        return f_ClientName;
    }

    @Override
    public void addAsLoanOwner(Loan loanToAdd) { //as borrower
        m_ClientAsBorrowerSet.add(loanToAdd);
    }

    public void addAsLender(Loan loanToAdd) {
        m_ClientAsLenderSet.add(loanToAdd);
    }

    public Set<Loan> getClientAsLenderSet() {
        return m_ClientAsLenderSet;
    }

    public Set<Loan> getClientAsBorrowerSet() {
        return m_ClientAsBorrowerSet;
    }
}
