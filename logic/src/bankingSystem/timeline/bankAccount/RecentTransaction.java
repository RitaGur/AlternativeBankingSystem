package bankingSystem.timeline.bankAccount;

public class RecentTransaction {
    private final int f_AmountOfTransaction;
    private final double f_BalanceBeforeTransaction;
    private final double f_BalanceAfterTransaction;
    private final int f_TransactionTimeUnit;
    private final char f_KindOfTransaction;

    public RecentTransaction(int i_AmountOfTransaction, double i_Balance, int i_TransactionTimeUnit) {
        f_AmountOfTransaction = i_AmountOfTransaction;
        f_BalanceBeforeTransaction = i_Balance;
        f_BalanceAfterTransaction = f_BalanceBeforeTransaction + i_AmountOfTransaction;
        f_TransactionTimeUnit = i_TransactionTimeUnit;
        f_KindOfTransaction = i_AmountOfTransaction >= 0 ? '+' : '-';
    }

    public double getBalanceBeforeTransaction() {
        return f_BalanceBeforeTransaction;
    }

    public int getAmountOfTransaction() {
        return f_AmountOfTransaction;
    }

    public double getBalanceAfterTransaction() {
        return f_BalanceAfterTransaction;
    }

    public int getTransactionTimeUnit() {
        return f_TransactionTimeUnit;
    }

    public char getKindOfTransaction() {
        return f_KindOfTransaction;
    }
}
