package bankingSystem;

import DTO.client.ClientInformationDTO;
import DTO.loan.LoanInformationDTO;
import bankingSystem.generated.*;
import bankingSystem.timeline.TimeUnit;
import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;
import bankingSystem.timeline.loan.Loan;
import bankingSystem.timeline.loan.LoanStatus;
import org.omg.IOP.CodecPackage.FormatMismatch;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class BankingSystem implements LogicInterface {
    private List<BankClient> m_BankAccountList;
    private List<Loan> m_LoanList;
    private TimeUnit m_CurrentTimeUnit;
    private List<String> m_LoanCategoryList;

    public BankingSystem() {
        m_BankAccountList = new ArrayList<>();
        m_LoanList = new ArrayList<>();
        m_CurrentTimeUnit = new TimeUnit();
        m_LoanCategoryList = new ArrayList<>();
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
            Loan loanToAdd = new Loan(i_LoanID, borrowerAccount, i_LoanStartSum, i_SumOfTimeUnit, i_HowOftenToPay, i_Interest, i_LoanCategory);
            m_LoanList.add(loanToAdd);
            borrowerAccount.addAsLoanOwner(loanToAdd);
        } else {
            throw new Exception("Loan Category does not exist");
        }
    }

    @Override
    public void promoteTimeline() throws Exception {
        m_CurrentTimeUnit.addOneToTimeUnit();

        for (BankClient client : m_BankAccountList) {
            ArrayList<Loan> loansNeedToBePaidThisTimeunitList = whichLoansNeedToPayListByBorrower(client);
            if (loansNeedToBePaidThisTimeunitList.size() == 0) {
                continue;
            }

            // sort by yaz
            Collections.sort(loansNeedToBePaidThisTimeunitList, new Comparator<Loan>() {
                @Override
                public int compare(Loan loan1, Loan loan2) {
                    return loan1.getBeginningTimeUnit() - loan2.getBeginningTimeUnit();
                }
            });
            // sort by amount of loan payment
            Collections.sort(loansNeedToBePaidThisTimeunitList, new Comparator<Loan>() {
                @Override
                public int compare(Loan loan1, Loan loan2) {
                    if (loan1.getBeginningTimeUnit() == loan2.getBeginningTimeUnit()) {
                        return (int)(loan1.sumAmountToPayEveryTimeUnit() - loan2.sumAmountToPayEveryTimeUnit());
                    }
                    return 0;
                }
            });

            for (Loan loan : loansNeedToBePaidThisTimeunitList) {
                loan.checkIfPaymentNeededAndPay(m_CurrentTimeUnit.getCurrentTimeUnit());
            }
        }
    }

    private ArrayList<Loan> whichLoansNeedToPayListByBorrower(BankClient client) {
        ArrayList<Loan> listToReturn = new ArrayList<>();

        for (Loan loan : client.getClientAsBorrowerSet()) {
            if (loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                listToReturn.add(loan);
            }
        }

        return listToReturn;
    }

    private ArrayList<Loan> whichLoansNeedToPayList() {
        ArrayList<Loan> listToReturn = new ArrayList<>();

        for (Loan loan : m_LoanList) {
            if (loan.isItPaymentTime(m_CurrentTimeUnit.getCurrentTimeUnit())) {
                listToReturn.add(loan);
            }
        }

        return listToReturn;
    }

    @Override
    public void readFromFile(String fileName) throws Exception {
        try {
            File file;
            file = new File(fileName);
            if (!file.exists()) {
                throw new FileNotFoundException();
            }
            if (!fileName.endsWith(".xml")) {
                throw new Exception();
            }
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

            // Timeunit:
            m_CurrentTimeUnit.setCurrentTimeUnit();

        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (Exception e) {
            throw e;
        }
    }

    private void fillLoanList(List<AbsLoan> absLoan) throws Exception {
        m_LoanList = new ArrayList<>();
        for (AbsLoan i_Loan : absLoan) {
            addLoan(i_Loan.getId(), i_Loan.getAbsOwner(), i_Loan.getAbsCapital(), i_Loan.getAbsTotalYazTime(), i_Loan.getAbsPaysEveryYaz(),
                    i_Loan.getAbsIntristPerPayment(), i_Loan.getAbsCategory());
        }
    }

    private void fillBankClientsList(List<AbsCustomer> absCustomerList) {
        m_BankAccountList = new ArrayList<>();
        for (AbsCustomer i_Customer : absCustomerList) {
            m_BankAccountList.add(new BankClient(i_Customer.getAbsBalance(), i_Customer.getName()));
        }
    }

    private void fromListToSetCategories(List<String> i_ListToConvert) {
        m_LoanCategoryList = new ArrayList<>();
        for (String i_CategoryInList : i_ListToConvert) {
            m_LoanCategoryList.add(i_CategoryInList);
        }
    }

    @Override
    public List<LoanInformationDTO> showLoansInformation() {
        List<LoanInformationDTO> loanListToReturn = new ArrayList<>();

        for (Loan i_Loan : m_LoanList) {
            loanListToReturn.add(new LoanInformationDTO(i_Loan));
        }

        return loanListToReturn;
    }

    @Override
    public List<ClientInformationDTO> showClientsInformation() {
        List<ClientInformationDTO> clientsListToReturn = new ArrayList<>();

        for (BankClient bankClient : m_BankAccountList) {
            clientsListToReturn.add(new ClientInformationDTO(bankClient));
        }

        return clientsListToReturn;
    }

    @Override
    public void addMoneyToAccount(String i_ClientAccount, int i_AmountToAdd) throws Exception {
        BankAccount accountToAddMoney = findBankAccountByName(i_ClientAccount);
        accountToAddMoney.addMoneyToAccount(i_AmountToAdd, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void withdrawMoneyFromAccount(String i_ClientAccount, int i_AmountToReduce) throws Exception {
        BankAccount accountToReduceMoney = findBankAccountByName(i_ClientAccount);
        accountToReduceMoney.withdrawMoneyFromAccount(i_AmountToReduce, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    @Override
    public void loansDistribution(List<LoanInformationDTO> chosenLoans, int amountOfMoneyToInvest, String lenderName) throws Exception {
        int smallerPartAmount = (amountOfMoneyToInvest / chosenLoans.size());
        int biggerPartAmount = smallerPartAmount + 1;
        int bigParts = amountOfMoneyToInvest % chosenLoans.size();
        Object[] chosenLoansArr = chosenLoans.toArray();
        int arrIndex = 0;
        BankClient lender = findBankAccountByName(lenderName);

        for (int i = 0; i < bigParts; i++) {
            checkLenderPartForLoan((LoanInformationDTO)chosenLoansArr[arrIndex++], biggerPartAmount, lender);
        }

        for (int i = 0; i < (chosenLoans.size() - bigParts); i++) {
            checkLenderPartForLoan((LoanInformationDTO)chosenLoansArr[arrIndex++], smallerPartAmount, lender);
        }
    }

    private void checkLenderPartForLoan(LoanInformationDTO chosenLoan, int lenderAmount, BankClient lender) throws Exception {
        Loan loan = findLoanById(chosenLoan.getLoanNameID());
        int finalAmountForLoan = 0;
        if (loan == null) {
            throw new Exception("There wasn't any loan found by this ID, check chosen loan array.");
        }
        if (loan.getLoanStatus() == LoanStatus.NEW) {
            finalAmountForLoan = (loan.getLoanStartSum() <= lenderAmount) ? loan.getLoanStartSum() : lenderAmount;
        }
        else if (loan.getLoanStatus() == LoanStatus.PENDING) {
            int moneyLeftToRaise = loan.getLoanStartSum() - loan.getPendingMoney();
            finalAmountForLoan = moneyLeftToRaise >= lenderAmount ? lenderAmount : moneyLeftToRaise;
        }

        loan.addLender(lender, finalAmountForLoan);
        lender.withdrawMoneyFromAccount(finalAmountForLoan, m_CurrentTimeUnit.getCurrentTimeUnit()); // take the money from the lender
        loan.addToPendingMoney(finalAmountForLoan, m_CurrentTimeUnit.getCurrentTimeUnit());
    }

    private Loan findLoanById(String loanNameID) {
        Loan loanToReturn = null;
        for (Loan loan : m_LoanList) {
            if (loan.getLoanNameID().equals(loanNameID)) {
                loanToReturn = loan;
                break;
            }
        }

        return loanToReturn;
    }

    @Override
    public void updateLoansCategories(List<String> i_LoansCategories) {
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

    public List<String> getLoanCategoryList() {
        return m_LoanCategoryList;
    }

    public List<LoanInformationDTO> optionsForLoans(String clientName, String categories, int amountOfMoneyToInvest,
                                                   int interest, int minimumTotalTimeunits) throws Exception {
        List<LoanInformationDTO> optionLoansSet = new ArrayList<>();
        BankAccount investClient = findBankAccountByName(clientName);
        List<String> chosenCategories = new ArrayList<>();
        fillChosenCategoriesList(chosenCategories, categories);

        try {
            if (amountOfMoneyToInvest > investClient.getAccountBalance()) {
                throw new Exception("Sorry, you do not have enough money in your account to invest this amount of money.");
            }
            for (Loan loanOfBankingSystem : m_LoanList) {
                //check our client is not the loan owner
                if (!loanOfBankingSystem.getLoanOwner().getClientName().equals(clientName) &&
                        (loanOfBankingSystem.getLoanStatus() == LoanStatus.NEW ||
                                loanOfBankingSystem.getLoanStatus()== LoanStatus.PENDING)) {
                    if (chosenCategories.contains(loanOfBankingSystem.getLoanCategory()) || chosenCategories.size() == 0) { //what if null?
                        if (interest <= (loanOfBankingSystem.getInterest()*100) || interest == 0) {
                            if (minimumTotalTimeunits <= loanOfBankingSystem.getSumOfTimeUnit()) {
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

    private void fillChosenCategoriesList(List<String> chosenCategories, String categories) {
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
        return categoryIterator.next();
    }

    public TimeUnit getCurrentTimeUnit() {
        return m_CurrentTimeUnit;
    }
}
