package bankingSystem.timeline.bankClient;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.loan.Loan;

import java.util.*;

import static java.lang.Math.round;

public class BankClient implements BankAccount {
    private final String f_ClientName;
    private double m_AccountBalance;
    private List<Loan> m_ClientAsLenderSet;
    private List<Loan> m_ClientAsBorrowerSet;
    private List<RecentTransaction> m_RecentTransactionList;

    public BankClient(int i_AccountBalance, String i_ClientName) {
        f_ClientName = i_ClientName;
        m_AccountBalance = i_AccountBalance;
        m_ClientAsBorrowerSet = new ArrayList<>();
        m_ClientAsLenderSet = new ArrayList<>();
        m_RecentTransactionList = new ArrayList<>();
    }

    @Override
    public double getAccountBalance() {
        return m_AccountBalance;
    }

    @Override
    public void putMoneyInInvest() {
        // m_LenderSet
    }

    @Override
    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit) {
        i_AmountToAdd = i_AmountToAdd;
        m_RecentTransactionList.add(new RecentTransaction(i_AmountToAdd, m_AccountBalance, i_TransactionTimeUnit));
        m_AccountBalance += i_AmountToAdd;
    }

    @Override
    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit) throws Exception {
        i_AmountToWithdraw = i_AmountToWithdraw;
        if (i_AmountToWithdraw > m_AccountBalance) {
            throw new Exception("The client does not have enough money in the account.");
        }
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

    public List<Loan> getClientAsLenderSet() {
        return m_ClientAsLenderSet;
    }

    public List<Loan> getClientAsBorrowerSet() {
        return m_ClientAsBorrowerSet;
    }
}
