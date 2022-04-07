package DTO.client;

public class RecentTransactionDTO {
    private final double f_AmountOfTransaction;
    private final double f_BalanceBeforeTransaction;
    private final double f_BalanceAfterTransaction;
    private final int f_TransactionTimeUnit;
    private final char f_KindOfTransaction;

    public RecentTransactionDTO(double f_AmountOfTransaction, double i_BalanceBeforeTransaction, double i_BalanceAfterTransaction, int i_TransactionTimeUnit, char i_KindOfTransaction) {
        this.f_AmountOfTransaction = f_AmountOfTransaction;
        this.f_BalanceBeforeTransaction = i_BalanceBeforeTransaction;
        this.f_BalanceAfterTransaction = i_BalanceAfterTransaction;
        this.f_TransactionTimeUnit = i_TransactionTimeUnit;
        this.f_KindOfTransaction = i_KindOfTransaction;
    }

    public double getAmountOfTransaction() {
        return f_AmountOfTransaction;
    }

    public double getBalanceBeforeTransaction() {
        return f_BalanceBeforeTransaction;
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
