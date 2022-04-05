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
import java.util.*;

public class BankingSystem implements LogicInterface {
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

        for (BankClient i_BankClient : m_BankAccountList) {
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
            Loan loanToAdd = new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, m_CurrentTimeUnit.getCurrentTimeUnit(), i_LoanCategory);
            m_LoanList.add(loanToAdd);
            borrowerAccount.addAsLoanOwner(loanToAdd);
        } else {
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
        m_LoanList = new HashSet<>();
        for (AbsLoan i_Loan : absLoan) {
            addLoan(i_Loan.getId(), i_Loan.getAbsOwner(), i_Loan.getAbsCapital(), i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(),
                    i_Loan.getAbsIntristPerPayment(), i_Loan.getAbsCategory());
        }
    }

    private void fillBankClientsList(List<AbsCustomer> absCustomerList) {
        m_BankAccountList = new HashSet<>();
        for (AbsCustomer i_Customer : absCustomerList) {
            m_BankAccountList.add(new BankClient(i_Customer.getAbsBalance(), i_Customer.getName()));
        }
    }

    private void fromListToSetCategories(List<String> i_ListToConvert) {
        m_LoanCategoryList = new HashSet<>();
        for (String i_CategoryInList : i_ListToConvert) {
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
        //accountToAddMoney.addMoneyToAccount(i_AmountToAdd);
        accountToAddMoney.addLastTransaction(i_AmountToAdd, m_CurrentTimeUnit.getCurrentTimeUnit());
        accountToAddMoney.addMoneyToAccount(i_AmountToAdd);
    }

    @Override
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception {
        BankAccount accountToReduceMoney = findBankAccountByName(i_ClientAccount);
        //accountToReduceMoney.withdrawMoneyFromAccount(i_AmountToReduce);
        accountToReduceMoney.addLastTransaction(i_AmountToReduce * (-1), m_CurrentTimeUnit.getCurrentTimeUnit());
        accountToReduceMoney.withdrawMoneyFromAccount(i_AmountToReduce);
    }

    @Override
    public void loansDistribution() {

    }

    @Override
    public void updateLoansCategories(Set<String> i_LoansCategories) {
        for (String i_Category : i_LoansCategories) {
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

        for (String i_Category : m_LoanCategoryList) {
            if (i_Category.equals(i_CategoryToCheck)) {
                categoryExist = true;
            }
        }

        return categoryExist;
    }

    public ClientInformationDTO clientInformationByName(String i_ClientName) throws Exception {
        BankClient clientAccount = findBankAccountByName(i_ClientName);
        return new ClientInformationDTO(clientAccount);
    }

    public Set<String> getLoanCategoryList() {
        return m_LoanCategoryList;
    }

    public Set<LoanInformationDTO> optionsForLoans(String clientName, String categories, int amountOfMoneyToInvest,
                                                   int interest, int minimumTotalTimeunits) throws Exception {
        Set<LoanInformationDTO> optionLoansSet = new HashSet<>();
        BankAccount investClient = findBankAccountByName(clientName);
        Set<String> chosenCategories = new HashSet<>();
        fillChosenCategoriesList(chosenCategories, categories);

        try {
            if (amountOfMoneyToInvest > investClient.getAccountBalance()) {
                throw new Exception("Sorry, you do not have enough money in your account to invest this amount of money.");
            }
            for (Loan loanOfBankingSystem : m_LoanList) {
                //check our client is not the loan owner
                if (!loanOfBankingSystem.getLoanOwner().getClientName().equals(clientName)) {
                    if (chosenCategories.contains(loanOfBankingSystem.getLoanCategory()) || chosenCategories.size() == 0) { //what if null?
                        if (interest >= (loanOfBankingSystem.getInterest()*100) || interest == -1) {
                            if (minimumTotalTimeunits >= loanOfBankingSystem.getSumOfTimeUnit()) {
                                optionLoansSet.add(new LoanInformationDTO(loanOfBankingSystem));
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw e;
        }

        return optionLoansSet;
    }

    private void fillChosenCategoriesList(Set<String> chosenCategories, String categories) {
        if (!categories.equals("0")) {
            StringTokenizer numbers = new StringTokenizer(categories);
            while (numbers.hasMoreTokens()) {
                chosenCategories.add(findCategoryByNumberString(numbers.nextToken()));
            }
        }
    }

    private String findCategoryByNumberString(String numberAsString) {
        int numberInInt = Integer.parseInt(numberAsString);
        Iterator<String> categoryIterator = m_LoanCategoryList.iterator();
        for (int i = 0; i < numberInInt - 1; i++) {
            categoryIterator.next();
        }
        return categoryIterator.next(); //TODO; check it brings the right category
    }
}
