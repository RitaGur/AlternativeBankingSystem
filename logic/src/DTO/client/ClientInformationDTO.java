package DTO.client;

import DTO.loan.LoanInformationDTO;
import bankingSystem.timeline.bankAccount.RecentTransaction;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.loan.Loan;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ClientInformationDTO {
    private final String clientName;
    private Set<RecentTransactionDTO> recentTransactionList;
    private Set<LoanInformationDTO> clientAsBorrowerLoanList;
    private Set<LoanInformationDTO> clientAsLenderLoanList;
    private double clientBalance;

    public ClientInformationDTO(BankClient bankClient) {
        this.clientName = bankClient.getClientName();
        this.recentTransactionList = recentTransactionListDTO(bankClient.getLastTransactions());
        this.clientAsBorrowerLoanList = clientLoanListDTO(bankClient.getClientAsBorrowerSet());
        this.clientAsLenderLoanList = clientLoanListDTO(bankClient.getClientAsLenderSet());
        this.clientBalance = bankClient.getAccountBalance();
    }

    private Set<LoanInformationDTO> clientLoanListDTO(Set<Loan> clientSet) {
        Set<LoanInformationDTO> setToReturn = new HashSet<>();

        for (Loan loan : clientSet) {
            setToReturn.add(new LoanInformationDTO(loan));
        }

        return setToReturn;
    }

    private Set<RecentTransactionDTO> recentTransactionListDTO(List<RecentTransaction> lastTransactions) {
        Set<RecentTransactionDTO> setToReturn = new HashSet<>();

        for (RecentTransaction recentTransaction : lastTransactions) {
            setToReturn.add(new RecentTransactionDTO(recentTransaction.getAmountOfTransaction(),recentTransaction.getBalanceBeforeTransaction(),
                    recentTransaction.getBalanceBeforeTransaction(), recentTransaction.getTransactionTimeUnit(), recentTransaction.getKindOfTransaction()));
        }

        return setToReturn;
    }

    public String getClientName() {
        return clientName;
    }

    public Set<RecentTransactionDTO> getRecentTransactionList() {
        return recentTransactionList;
    }

    public Set<LoanInformationDTO> getClientAsBorrowerLoanList() {
        return clientAsBorrowerLoanList;
    }

    public Set<LoanInformationDTO> getClientAsLenderLoanList() {
        return clientAsLenderLoanList;
    }

    public double getClientBalance() {
        return clientBalance;
    }
}
