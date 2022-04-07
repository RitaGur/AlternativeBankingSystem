package bankingSystem.timeline.bankAccount;

import bankingSystem.timeline.loan.Loan;

import java.util.List;
import java.util.Set;

public interface BankAccount {
/*    public void askForLoan(String i_LoanID, int i_LoanSum, int i_SumOfTimeUnit, int i_TimeUnitsBetweenPayments,
                           double i_Interest, int i_LoanBeginningTimeUnit, String i_LoanCategory);*/
    public void putMoneyInInvest();
    public void addMoneyToAccount(double i_AmountToAdd, int i_TransactionTimeUnit);
    public void withdrawMoneyFromAccount(double i_AmountToWithdraw, int i_TransactionTimeUnit);
    public List<RecentTransaction> getLastTransactions();
    public void addLastTransaction(int i_AmountOfTransaction, int i_TransactionTimeUnit);
    public double getAccountBalance();
    public String getClientName();
    void addAsLoanOwner(Loan loanToAdd);
}
