package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.generated.*;
import bankingSystem.timeline.TimeUnit;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.loan.Loan;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.util.HashSet;
import java.util.List;
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

    public void addLoan(String i_LoanID, String i_BorrowerName, int i_LoanStartSum, int i_SumOfTimeUnit, int i_HowOftenToPay, double i_Interest, String i_LoanCategory) throws Exception {
        BankAccount borrowerAccount = findBankAccountByName(i_BorrowerName);
        if (checkIfCategoryExists(i_LoanCategory)) {
            Loan loanToAdd =  new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, m_CurrentTimeUnit.getCurrentTimeUnit(), i_LoanCategory);
            m_LoanList.add(loanToAdd);
            borrowerAccount.addAsLoanOwner(loanToAdd);
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

        for (Loan i_Loan : m_LoanList) {
            i_Loan.checkIfPaymentNeededAndPay(m_CurrentTimeUnit.getCurrentTimeUnit());
        }
    }

    @Override
    public void readFromFile(String fileName) throws Exception {
        // parameter - time unit for LoanObject - loan beginning timiUnit
        try {
            File file;
            file = new File(fileName);
            JAXBContext jaxbContext = JAXBContext.newInstance(AbsDescriptor.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            AbsDescriptor descriptor = (AbsDescriptor) jaxbUnmarshaller.unmarshal(file);

            // Categories:
            List<String> categoriesList = descriptor.getAbsCategories().getAbsCategory();
            fromListToSetCategories(categoriesList); //filling m_LoanCategoryList

            // BankClients:
            fillBankClientsList(descriptor.getAbsCustomers().getAbsCustomer());

            // Loans:
            fillLoanList(descriptor.getAbsLoans().getAbsLoan());


        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw e;
        }
    }

    private void fillLoanList(List<AbsLoan> absLoan) throws Exception {
        for (AbsLoan i_Loan : absLoan) {
            BankAccount loanOwner = findBankAccountByName(i_Loan.getAbsOwner());
            m_LoanList.add(new Loan(i_Loan.getId(), loanOwner, i_Loan.getAbsCapital(), i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(),
                    i_Loan.getAbsIntristPerPayment(), m_CurrentTimeUnit.getCurrentTimeUnit(),i_Loan.getAbsCategory()));
        }
    }

    private void fillBankClientsList(List<AbsCustomer> absCustomerList) {
        for (AbsCustomer i_Customer : absCustomerList) {
            m_BankAccountList.add(new BankClient(i_Customer.getAbsBalance(), i_Customer.getName()));
        }
    }

    private void fromListToSetCategories(List<String> i_ListToConvert) {
        for (String i_CategoryInList: i_ListToConvert) {
            m_LoanCategoryList.add(i_CategoryInList);
        }
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
    public Set<ClientInformationDTO> showClientsInformation() {
        Set<ClientInformationDTO> clientsListToReturn = new HashSet<>();

        for (BankClient bankClient : m_BankAccountList) {
            clientsListToReturn.add(new ClientInformationDTO(bankClient));
        }

        return clientsListToReturn;
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

    private BankClient findBankAccountByName(String i_ClientName) throws Exception {
        BankClient account = null;

        for (BankClient i_BankClient : m_BankAccountList) {
            if (i_BankClient.getClientName().equals(i_ClientName)) {
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

    public ClientInformationDTO clientInformationByName(String i_ClientName) throws Exception {
        BankClient clientAccount = findBankAccountByName(i_ClientName);
        return new ClientInformationDTO(clientAccount);
    }
}
