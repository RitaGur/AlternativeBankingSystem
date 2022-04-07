package DTO.client;

import DTO.loan.LoanInformationDTO;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.loan.Loan;

import java.util.ArrayList;
import java.util.List;

public class ClientInformationDTO {
    private final String clientName;
    private List<RecentTransactionDTO> recentTransactionList;
    private List<LoanInformationDTO> clientAsBorrowerLoanList;
    private List<LoanInformationDTO> clientAsLenderLoanList;
    private double clientBalance;

    public ClientInformationDTO(BankClient bankClient) {
        this.clientName = bankClient.getClientName();
        this.recentTransactionList = recentTransactionListDTO(bankClient.getLastTransactions());
        this.clientAsBorrowerLoanList = clientLoanListDTO(bankClient.getClientAsBorrowerSet());
        this.clientAsLenderLoanList = clientLoanListDTO(bankClient.getClientAsLenderSet());
        this.clientBalance = bankClient.getAccountBalance();
    }

    private List<LoanInformationDTO> clientLoanListDTO(List<Loan> clientSet) {
        List<LoanInformationDTO> setToReturn = new ArrayList<>();

        for (Loan loan : clientSet) {
            setToReturn.add(new LoanInformationDTO(loan));
        }

        return setToReturn;
    }

    private List<RecentTransactionDTO> recentTransactionListDTO(List<RecentTransaction> lastTransactions) {
        List<RecentTransactionDTO> setToReturn = new ArrayList<>();

        for (RecentTransaction recentTransaction : lastTransactions) {
            setToReturn.add(new RecentTransactionDTO(recentTransaction.getAmountOfTransaction(),recentTransaction.getBalanceBeforeTransaction(),
                    recentTransaction.getBalanceAfterTransaction(), recentTransaction.getTransactionTimeUnit(), recentTransaction.getKindOfTransaction()));
        }

        return setToReturn;
    }

    public String getClientName() {
        return clientName;
    }

    public List<RecentTransactionDTO> getRecentTransactionList() {
        return recentTransactionList;
    }

    public List<LoanInformationDTO> getClientAsBorrowerLoanList() {
        return clientAsBorrowerLoanList;
    }

    public List<LoanInformationDTO> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
    }

    public double getClientBalance() {
        return clientBalance;
    }
}
