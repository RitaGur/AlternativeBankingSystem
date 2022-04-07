package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;

import java.util.List;

public interface LogicInterface {
    public void readFromFile(String fileName) throws Exception;
    public List<LoanInformationDTO> showLoansInformation();
    public List<ClientInformationDTO> showClientsInformation();
    public void addMoneyToAccount(String i_ClientAccount, int i_AmountToAdd) throws Exception;
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception;
    public void loansDistribution(List<LoanInformationDTO> chosenLoans, int amountOfMoneyToInvest, String lenderName) throws Exception;
    public void promoteTimeline() throws Exception;

    public void addBankClient(int i_AccountBalance, String i_ClientName) throws Exception;

    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit,
                        int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception;
    public void updateLoansCategories(List<String> i_LoansCategories);
}
