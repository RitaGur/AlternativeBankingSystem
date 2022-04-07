package bankingSystem.timeline.bankAccount;

import bankingSystem.timeline.loan.Loan;

import java.util.List;

public interface BankAccount {
    public void putMoneyInInvest();
    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit);
    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit) throws Exception;
    public List<RecentTransaction> getLastTransactions();
    public void addLastTransaction(int i_AmountOfTransaction, int i_TransactionTimeUnit);
    public double getAccountBalance();
    public String getClientName();
    void addAsLoanOwner(Loan loanToAdd);
}
