package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.loan.Loan;

import java.util.Set;

public interface LogicInterface {
    public void readFromFile(String fileName) throws Exception;
    public Set<LoanInformationDTO> showLoansInformation();
    public Set<ClientInformationDTO> showClientsInformation();
    public void addMoneyToAccount(String i_ClientAccount, int i_AmountToAdd) throws Exception;
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception;
    public void loansDistribution(Set<LoanInformationDTO> chosenLoans, int amountOfMoneyToInvest, String lenderName) throws Exception;
    public void promoteTimeline();

    public void addBankClient(int i_AccountBalance, String i_ClientName) throws Exception;

    //TODO: change to DTO as one parameter
    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit,
                        int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception;
    public void updateLoansCategories(Set<String> i_LoansCategories);
}
