package bankingSystem;

import DTO.loan.LoanInformationDTO;
import bankingSystem.timeline.TimeUnit;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.loan.Loan;

import java.util.HashSet;
import java.util.Set;

public class BankingSystem implements LogicInterface{
    private Set<BankClient> m_BankAccountList;
    private Set<Loan> m_LoanList;
    private TimeUnit m_CurrentTimeUnit;
    private Set<String> m_LoanCategoryList;

    public BankingSystem() {
        m_BankAccountList = new HashSet<>();
        m_LoanList = new HashSet<>();
        m_CurrentTimeUnit = new TimeUnit();
        m_LoanCategoryList = new HashSet<>();
    }



    @Override
    public void addBankClient(int i_AccountBalance, String i_ClientName) throws Exception {
        BankAccount account = null;

        for (BankClient i_BankClient:m_BankAccountList) {
            if (i_BankClient.getClientName() == i_ClientName) {
                account = i_BankClient;
            }
        }

        if (account != null) {
            throw new Exception("This bank client already exists.");
        }

        m_BankAccountList.add(new BankClient(i_AccountBalance, i_ClientName));
    }

    // maybe change BackAccount to String name and then search for the right BankAccount
    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit, int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception {
        BankAccount borrowerAccount = findBankAccountByName(i_BorrowerName);
        if (checkIfCategoryExists(i_LoanCategory)) {
            m_LoanList.add(new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, m_CurrentTimeUnit.getCurrentTimeUnit(), i_LoanCategory));
        }
        else {
            throw new Exception("Loan Category does not exist");
        }
    }


    /*public void loansDistribution(int i_InvestSum, LoanCategory i_LoanCategory, int i_MinInterest, int i_MinTimeUnits, int i_MaxOwnershipPercentage, int i_MaxOpenLoans) {

    }*/

    @Override
    public void promoteTimeline() {
        m_CurrentTimeUnit.addOneToTimeUnit();
    }

    @Override
    public void readFromFile() {

    }

    @Override
    public Set<LoanInformationDTO> showLoansInformation() {
        Set<LoanInformationDTO> loanListToReturn = new HashSet<>();
        for (Loan i_Loan : m_LoanList) {
            loanListToReturn.add(new LoanInformationDTO(i_Loan));
        }
        return loanListToReturn;
    }

    @Override
    public void showClientsInformation() {

    }

    @Override
    public void addMoneyToAccount(String i_ClientAccount, int i_AmountToAdd) throws Exception {
        BankAccount accountToAddMoney = findBankAccountByName(i_ClientAccount);
        accountToAddMoney.addMoneyToAccount(i_AmountToAdd);
        accountToAddMoney.addLastTransaction(i_AmountToAdd, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception {
        BankAccount accountToAddMoney = findBankAccountByName(i_ClientAccount);
        accountToAddMoney.addMoneyToAccount(i_AmountToReduce);
        accountToAddMoney.addLastTransaction(i_AmountToReduce, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void loansDistribution() {

    }

    @Override
    public void updateLoansCategories(Set<String> i_LoansCategories) {
        for (String i_Category: i_LoansCategories) {
            m_LoanCategoryList.add(i_Category);
        }
    }

    private BankAccount findBankAccountByName(String i_ClientName) throws Exception {
        BankAccount account = null;

        for (BankClient i_BankClient:m_BankAccountList) {
            if (i_BankClient.getClientName() == i_ClientName) {
                account = i_BankClient;
            }
        }

        if (account == null) {
            throw new Exception("This bank client does not exist, please add the bank client first.");
        }

        return account;
    }

    private boolean checkIfCategoryExists(String i_CategoryToCheck) {
        boolean categoryExist = false;

        for (String i_Category:m_LoanCategoryList) {
            if (i_Category == i_CategoryToCheck) {
                categoryExist = true;
            }
        }

        return categoryExist;
    }
}
