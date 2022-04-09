package consoleUI;

import DTO.client.ClientInformationDTO;
import DTO.client.RecentTransactionDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PartInLoanDTO;
import DTO.loan.PaymentsDTO;
import bankingSystem.BankingSystem;
import exception.ValueOutOfRangeException;
import java.io.FileNotFoundException;
import java.util.*;

public class ConsoleStart {

    private final int f_MaxOptionsInMenu = 8;

    private BankingSystem m_Engine;
    private boolean isFileRead;

    public ConsoleStart() {
        m_Engine = new BankingSystem();
        isFileRead = false;
    }

    public void run(){
        startService();
    }

    private void startService(){
        showMenu();
        String userInputString;
        int userInputInt = 0;
        Scanner scanUserInput = new Scanner(System.in);

        do {
            try{
                userInputString = scanUserInput.next();
                userInputInt = isValidInput(userInputString.trim(), 8, 1);
                if (userInputInt != 1 &&  userInputInt != 8 && isFileRead == false) {
                    throw new Exception("A file was not read.");
                }
                userInputChoices(userInputInt);
            }
            catch (ValueOutOfRangeException i_OutOfRangeException){
                System.out.println(i_OutOfRangeException.getMessage() + "Please enter a number between " + i_OutOfRangeException.getM_MinValue() + "-" + i_OutOfRangeException.getMaxValue());
            }
            catch (NumberFormatException i_NumberFormatException){
                System.out.println(i_NumberFormatException.getMessage());
            }
            catch (Exception e) {
                System.out.println(e.getMessage() + " Please upload a file before any other operation.");
                showMenu();
            }
        }
        while (userInputInt != f_MaxOptionsInMenu);
    }

    private void userInputChoices(int i_UserInput) {
        try {
            switch (i_UserInput) {
                case 1:
                    readFromFile();
                    break;
                case 2:
                    showLoansInformation();
                    break;
                case 3:
                    showClientsInformation();
                    break;
                case 4:
                    addMoney();
                    break;
                case 5:
                    withdrawMoney();
                    break;
                case 6:
                    loansDistribution();
                    break;
                case 7:
                    promoteTimeline();
                    break;
                case 8:
                    exitProgram();
                    break;
            }
        }
//        catch (ValueOutOfRangeException i_OutOfRangeException) {
//            afterKeyPressedMessage(i_OutOfRangeException.getMessage() + "Please enter a number between " + i_OutOfRangeException.getM_MinValue() + "-" + i_OutOfRangeException.getM_MaxValue());
//        }
        catch (Exception i_Exception) {
            afterKeyPressedMessage(i_Exception.getMessage());
        }

        showMenu();
    }

    private void afterKeyPressedMessage(String i_Message) {
        System.out.println(i_Message);
        //System.out.println("Please enter any key to continue");
    }

    private void exitProgram() {
        System.out.println("Bye-Bye!");
        System.exit(0);
    }

    private void promoteTimeline() throws Exception {
        System.out.println("Previous Timeunit: " + m_Engine.getCurrentTimeUnit().getCurrentTimeUnit());
        m_Engine.promoteTimeline();
        System.out.println("Current Timeunit: " + m_Engine.getCurrentTimeUnit().getCurrentTimeUnit());
        System.out.println();
    }

    private void loansDistribution() throws Exception {
        List<ClientInformationDTO> clientInfoList = m_Engine.showClientsInformation();
        String userAccountInputString = "", categories = "";
        Scanner scanUserInput = new Scanner(System.in);
        int counter = 1, amountOfMoneyToInvest = -1, interest = -1, minimumTotalTimeunits = -1;
        boolean userInputCorrect = false;

        //userAccountInputString = accountNameFromUser(); TODO: do methods for clean code
        //categories = categoriesListFromUser();
        //amountOfMoneyToInvest =


        while (!userInputCorrect) {
            System.out.println("Please choose the account you would like to add an investment to (enter full name):");
            printClientsNameAndAmount();
            userAccountInputString = scanUserInput.nextLine();
            userInputCorrect = isUserInputExist(userAccountInputString.trim(), clientInfoList);
            if (!userInputCorrect) {
                System.out.println("This account does not exist, please try again.");
            }
        }
        userInputCorrect = false;

        while (!userInputCorrect) {
            try {
                System.out.println("Please enter the amount of money you would like to invest (required field):");
                amountOfMoneyToInvest = Integer.parseInt(scanUserInput.nextLine());
                userInputCorrect = checkUserInputInvestmentAmount(amountOfMoneyToInvest, userAccountInputString);
                if (!userInputCorrect) {
                    throw new Exception("There is not enough money in the account.");
                }
            }
            catch (NumberFormatException numberEx) {
                System.out.println("Invalid amount of money, please try again.");
            }
            catch(Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        List<String> chosenCategories = new ArrayList<>();
        userInputCorrect = false;
        while (!userInputCorrect) {
            try {
                printCategoriesMessageAndLoanCategories();
                categories = scanUserInput.nextLine();
                m_Engine.fillChosenCategoriesList(chosenCategories, categories);
                userInputCorrect = true;
            }
            catch (NumberFormatException numberEx) {
                System.out.println("Invalid input, should be numbers only.");
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
        userInputCorrect = false;
        while(!userInputCorrect) {
            try {
                System.out.println("Please fill the minimum interest you would like to get (Integer between 0-100): ");
                System.out.println("In case you choose 0, the system would offer you any loan, regardless it's interest.");
                interest = Integer.parseInt(scanUserInput.nextLine());
                userInputCorrect = isInterestInputCorrect(interest);
                if (!userInputCorrect) {
                    System.out.println("Invalid input, the interest needs to be an integer between 0-100,");
                }
            }
            catch (Exception ex) {
                System.out.println("Invalid input, should be numbers only.");
            }
        }
        userInputCorrect = false;
        while (!userInputCorrect) {
            try {
                System.out.println("Please fill minimum total timeunits for a loan (Integer):");
                System.out.println("In case you fill 0 in this field, the system would offer you any loan, regardless it's total timeunits.");
                minimumTotalTimeunits = Integer.parseInt(scanUserInput.nextLine());
                if (minimumTotalTimeunits < 0) {
                    System.out.println("The minimum total timeunits needs to be a positive number.");
                    userInputCorrect = false;
                }
                else {
                    userInputCorrect = true;
                }
            }
            catch (NumberFormatException numberEx) {
                System.out.println("Invalid input, should be numbers only.");
            }
        }
        List<LoanInformationDTO> loansOptions = m_Engine.optionsForLoans(userAccountInputString, chosenCategories, amountOfMoneyToInvest,
                                                interest, minimumTotalTimeunits);

        if(loansOptions.size() == 0) {
            System.out.println("Sorry, there was no suitable loan for your demands.");
            return;
        }

        userInputCorrect = false;
        while (!userInputCorrect) {
            try{
                String stringToPrint = "Please choose a loan from the options below:\n";
                stringToPrint += "You can choose as many as you prefer. For example: 1 2 3\n";

                stringToPrint += printLoansOptions(loansOptions);
                System.out.println(stringToPrint);
                String chosenLoans = scanUserInput.nextLine();
                List<LoanInformationDTO> chosenLoansSet = fillChosenLoans(chosenLoans, loansOptions);
                // part where we distribute the money between the loans the user chose
                m_Engine.loansDistribution(chosenLoansSet, amountOfMoneyToInvest, userAccountInputString);
                System.out.println("Your investment money was distributed successfully!");
                System.out.println("You can now see the changes in options 2,3 of the system.");
                System.out.println();
                userInputCorrect = true;
            }
            catch (NumberFormatException numberEx) {
                System.out.println("Invalid input, should be numbers only.");
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private boolean checkUserInputInvestmentAmount(int amountOfMoneyToInvest, String clientNName) {
        List<ClientInformationDTO> clientsList = m_Engine.showClientsInformation();

        for (ClientInformationDTO singleClient : clientsList) {
            if ((singleClient.getClientName().equals(clientNName)) && (amountOfMoneyToInvest > 0 && amountOfMoneyToInvest <= singleClient.getClientBalance()))
                return true;
        }

        return false;
    }

    private boolean isInterestInputCorrect(int interest) {
        return  (interest >= 0 && interest <= 100);
    }

    private List<LoanInformationDTO> fillChosenLoans(String chosenLoans, List<LoanInformationDTO> loansOptions) throws Exception {
        List<LoanInformationDTO> setToReturn = new ArrayList<>();
        StringTokenizer numbers = new StringTokenizer(chosenLoans);
        int numberInInt;
        Object[] loansOptionsArr = loansOptions.toArray();

        while (numbers.hasMoreTokens()) {
            numberInInt = Integer.parseInt(numbers.nextToken());
            if (numberInInt > loansOptions.size() || numberInInt < 0) {
                throw new Exception("There are no categories for the number: "+ numberInInt);
            }
            setToReturn.add((LoanInformationDTO)loansOptionsArr[numberInInt-1]);
        }

        return setToReturn;
    }

    private String printLoansOptions(List<LoanInformationDTO> loansOptions) {
        String stringToPrint = "";
        int counter = 1;
        for (LoanInformationDTO loanOption : loansOptions) {
            stringToPrint += counter++ + ")\n";
            stringToPrint += clientLoansInformationString(loanOption);
        }
        return stringToPrint;
    }

    private void printCategoriesMessageAndLoanCategories() {
        System.out.println("Please choose categories for your investment:");
        System.out.println("You can choose as many as you prefer, or none. For example: 1 2 3 / 0");
        System.out.println("In case you don't choose any category, the system would offer you any loan, regardless it's category. ");
        int counter = 1;

        for (String category : m_Engine.getLoanCategoryList()) {
            System.out.println(counter++ + ")" + category);
        }
    }

    private void printClientsNameAndAmount() {
        List<ClientInformationDTO> clientInfoList = m_Engine.showClientsInformation();
        int counter = 1;

        for (ClientInformationDTO clientDTO: clientInfoList) {
            System.out.println(counter++ + ")Client Name:" + clientDTO.getClientName());
            System.out.println("  Account Balance: " + clientDTO.getClientBalance());
        }
    }

    private void withdrawMoney() throws Exception {
        List<ClientInformationDTO> clientInfoList = m_Engine.showClientsInformation();
        String userAccountInputString = "";
        int userAccountInput, userAmountOfMoneyInput;
        Scanner scanUserInput = new Scanner(System.in);
        boolean inputCorrect = false;

        while (!inputCorrect) {
            System.out.println("Please choose the account you would like to withdraw money from(enter full name): ");
            printClientsNameAndBalance(clientInfoList);
            userAccountInputString = scanUserInput.nextLine();
            inputCorrect = isUserInputExist(userAccountInputString.trim(), clientInfoList);
            if (!inputCorrect) {
                System.out.println("This account does not exist, please try again.");
            }
        }

        inputCorrect = false;

      while (!inputCorrect) {
            try {
                System.out.println("Please enter the amount of money you would like to withdraw from the chosen account: ");
                userAmountOfMoneyInput = Integer.parseInt(scanUserInput.nextLine());

                m_Engine.withdrawMoneyFromAccount(userAccountInputString, userAmountOfMoneyInput);
                System.out.println("Money was withdrawed successfully. Current Account Balance is: " + m_Engine.clientInformationByName(userAccountInputString).getClientBalance());
                inputCorrect = true;
            }
            catch (NumberFormatException numberFormatEx) {
                System.out.println("Invalid input - Should be numbers only.");
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
      }
 }

    private void addMoney() throws Exception {
        boolean inputCorrect = false;
        String userAccountInputString = "";
        int userAccountInput, userAmountOfMoneyInput;
        Scanner scanUserInput = new Scanner(System.in);
        List<ClientInformationDTO> clientInfoList = m_Engine.showClientsInformation();

        while (!inputCorrect) {
            System.out.println("Please choose the account you would like to add money to(enter full name): ");
            printClientsNameAndBalance(clientInfoList);
            userAccountInputString = scanUserInput.nextLine();
            inputCorrect = isUserInputExist(userAccountInputString.trim(), clientInfoList);
            if (!inputCorrect) {
                System.out.println("This account does not exist, please try again.");
            }
        }

        inputCorrect = false;

        while (!inputCorrect) {
            try {
                System.out.println("Please enter the amount of money you would like to add to the chosen account: ");
                userAmountOfMoneyInput = Integer.parseInt(scanUserInput.nextLine());
                inputCorrect = true;

                m_Engine.addMoneyToAccount(userAccountInputString, userAmountOfMoneyInput);
                System.out.println("Money was added successfully. Current Account Balance is: " + m_Engine.clientInformationByName(userAccountInputString).getClientBalance());

            } catch (NumberFormatException numberFormatEx) {
                System.out.println("Invalid input - Should be numbers only.");
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    private boolean isUserInputExist(String userAccountInputString, List<ClientInformationDTO> clientInfoList) {
        for (ClientInformationDTO clientDTO : clientInfoList) {
            if (clientDTO.getClientName().equals(userAccountInputString)) {
                return true;
            }
        }

        return false;
    }

    private void printClientsNameAndBalance(List<ClientInformationDTO> clientInfoList) {
        int counter = 1;
        for (ClientInformationDTO clientDTO: clientInfoList) {
            System.out.println(counter++ + ")" + clientDTO.getClientName() + ", " + clientDTO.getClientBalance());
        }
    }

    private boolean isValidNameInput(String userAccountInputString) {
        boolean nameIsValid = false;

        if(userAccountInputString.matches("[a-zA-z] ")) {

        }
        return nameIsValid;
    }

    private void showClientsInformation() {
        List<ClientInformationDTO> clientInfo = m_Engine.showClientsInformation();
        System.out.println("-----Clients Information-----");
        int counter = 1;

        for (ClientInformationDTO clientDTO : clientInfo) {
            System.out.println("Client number " + counter++ + ":");
            printClientInformation(clientDTO);
        }
        counter = 1;
        System.out.println("---------------------------");
    }

    private void printClientInformation(ClientInformationDTO singleClientInformation) {
        String stringToPrint = "";
        stringToPrint += "Client name: " + singleClientInformation.getClientName() + "\n";
        int counter = 1;

        // recent transactions:
        List<RecentTransactionDTO> recentTransactionList = singleClientInformation.getRecentTransactionList();
        stringToPrint += "*****Last Transactions*****\n";
        for (RecentTransactionDTO singleTransaction : recentTransactionList) {
            stringToPrint += "Transaction number " + counter++ + ":\n";
            stringToPrint += clientsRecentTransactionString(singleTransaction);
        }
        stringToPrint+= "*******************************\n";
        counter = 1;

        // loans as borrower info
        stringToPrint+= "*****Client loans as Borrower*****\n";
        for (LoanInformationDTO loanAsBorrower : singleClientInformation.getClientAsBorrowerLoanList()) {
            stringToPrint += "Client loan number " + counter++ + ":\n";
            stringToPrint += clientLoansInformationString(loanAsBorrower);
        }

        stringToPrint += "*******************************\n";
        counter = 1;

        //loans as lender info
        stringToPrint+= "*****Client loans as Lender*****\n";
        for (LoanInformationDTO loanAsLender : singleClientInformation.getClientAsLenderLoanList()) {
            stringToPrint += "Client loan number " + counter++ + ":\n";
            stringToPrint += clientLoansInformationString(loanAsLender);
        }
        counter = 1;
        stringToPrint += "*******************************\n";

        System.out.println(stringToPrint);
    }

    private String clientsRecentTransactionString(RecentTransactionDTO singleTransaction) {
        String stringToReturn = "";
        stringToReturn += "Transaction Timeunit: " + singleTransaction.getTransactionTimeUnit() + "\n" +
                "Transaction Amount: " +singleTransaction.getAmountOfTransaction() + "\n" +
                "Kind Of Transaction: " + singleTransaction.getKindOfTransaction() + "\n" +
                "Account Balance Before Transaction: " + singleTransaction.getBalanceBeforeTransaction() + "\n" +
                "Account Balance After Transaction: " + singleTransaction.getBalanceAfterTransaction() + "\n";

        return stringToReturn;
    }

    private String clientLoansInformationString(LoanInformationDTO loan) {
        String stringToPrint = "";
        stringToPrint +=
                "Loan ID: " + loan.getLoanNameID() + "\n" +
                        "Loan Category: " + loan.getLoanCategory() + "\n" +
                        "Loan Initial Amount: " + loan.getLoanStartSum() + "\n" +
                        "TimeUnits Between Payments: " + loan.getTimeUnitsBetweenPayments() + "\n" +
                        "Loan Interest: " + (int)(loan.getLoanInterest()*100) + "%\n" +
                        "Loan Final Amount(Fund + Interest): " + loan.getSumAmount() + "\n" +
                        "Loan Status: " + loan.getLoanStatus() + "\n";
        stringToPrint += clientLoanStatusInformation(loan.getLoanStatus(), loan) + "\n";

        return stringToPrint;
    }

    private String clientLoanStatusInformation(String loanStatus, LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "";
        switch (loanStatus) {
            case "NEW":
                break;
            case "PENDING":
                stringToReturn += clientLoanPendingStatus(singleLoanInformation);
                break;
            case "ACTIVE":
                stringToReturn += clientLoanActiveStatus(singleLoanInformation);
                break;
            case "RISK":
                stringToReturn += clientLoanRiskStatus(singleLoanInformation);
                break;
            case "FINISHED":
                stringToReturn += clientLoanFinishedStatus(singleLoanInformation);
                break;
        }

        return stringToReturn;
    }

    private String clientLoanFinishedStatus(LoanInformationDTO singleLoanInformation) {
        return "Beginning Timeunit: " + singleLoanInformation.getBeginningTimeUnit() +"\n" +
                "Ending Timeunit: " +singleLoanInformation.getEndingTimeUnit() +"\n";
    }

    private String clientLoanRiskStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "";

        stringToReturn += "Number of unpaid payments: " + singleLoanInformation.getNumberOfUnpaidPayments() + "\n";
        stringToReturn += "Sum of unpaid payments: " + singleLoanInformation.amountOfUnPaidPayments();

        return stringToReturn;
    }

    private String clientLoanActiveStatus(LoanInformationDTO singleLoanInformation) {
        return "Next Payment Timeunit: " + singleLoanInformation.getNextPaymentTimeUnit() + "\n" +
                "Next Payment Amount: " + singleLoanInformation.getSumAmountToPayEveryTimeUnit() + "\n";
    }

    private String clientLoanPendingStatus(LoanInformationDTO singleLoanInformation) {
        return "Remaining loan amount to activate: " + (singleLoanInformation.getFundAmount() - singleLoanInformation.getPendingMoney()) + "\n";
    }

    private void showLoansInformation() {
        List<LoanInformationDTO> loanInfoList = m_Engine.showLoansInformation();
        System.out.println("-----Loans Information-----");
        int counter = 1;

        for (LoanInformationDTO loanDTO : loanInfoList) {
            System.out.println("Loan Number " + (counter++) + ":");
            printLoanInformation(loanDTO);
        }
        counter = 1;
        System.out.println("---------------------------");
    }

    private void printLoanInformation(LoanInformationDTO singleLoanInformation) {
        String stringToPrint = "";
        stringToPrint +=
                    "Loan ID: " + singleLoanInformation.getLoanNameID() + "\n" +
                     "Loan Owner: " + singleLoanInformation.getBorrowerName() + "\n" +
                    "Loan Category: " + singleLoanInformation.getLoanCategory() + "\n" +
                    "Loan Status: " + singleLoanInformation.getLoanStatus() + "\n" +
                     "Loan Sum: " + singleLoanInformation.getLoanStartSum() + "\n" +
                     "Loan Time Duration: " + singleLoanInformation.getLoanSumOfTimeUnit() + "\n" +
                     "Loan Interest: " + (int)(singleLoanInformation.getLoanInterest() * 100) + "%" + "\n" +
                     "Timeunits between payments: " + singleLoanInformation.getTimeUnitsBetweenPayments() + "\n";
        stringToPrint += printByLoanStatus(singleLoanInformation.getLoanStatus(), singleLoanInformation);

        System.out.println(stringToPrint);
        System.out.println();
    }

    private String printByLoanStatus(String loanStatus, LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "";
        switch (loanStatus) {
            case "NEW":
                break;
            case "PENDING":
                stringToReturn += pendingStatus(singleLoanInformation);
                break;
            case "ACTIVE":
                stringToReturn += activeStatus(singleLoanInformation);
                break;
            case "RISK":
                stringToReturn += riskStatus(singleLoanInformation);
                break;
            case "FINISHED":
                stringToReturn += finishedStatus(singleLoanInformation);
                break;
        }

        return stringToReturn;
    }

    private String finishedStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = lendersPartInLoanString(singleLoanInformation);

        stringToReturn += "Beginning Time Unit: " + singleLoanInformation.getBeginningTimeUnit() + "\n";
        stringToReturn += "Ending Time Unit: " + singleLoanInformation.getEndingTimeUnit() + "\n";
        stringToReturn += loanPaymentsString(singleLoanInformation);

        return stringToReturn;
    }

    private String riskStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = activeStatus(singleLoanInformation);
        stringToReturn += "Number of unpaid payments: " + singleLoanInformation.getNumberOfUnpaidPayments() + "\n";
        stringToReturn += "Sum of unpaid payments: " + (singleLoanInformation.getSumAmountToPayEveryTimeUnit() * singleLoanInformation.getNumberOfUnpaidPayments()) + "\n";
        return stringToReturn;
    }

    private String activeStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = lendersPartInLoanString(singleLoanInformation);
        stringToReturn += "Beginning Timeunit: " + singleLoanInformation.getBeginningTimeUnit() + "\n";
        stringToReturn += "Next Payment Timeunit: " + calculateNextPaymentUnit(singleLoanInformation) + "\n";
        stringToReturn += loanPaymentsString(singleLoanInformation);
        stringToReturn += "Paid Fund Amount: " + singleLoanInformation.getPaidFund() + "\n";
        stringToReturn += "Paid Interest Amount: " + singleLoanInformation.getPaidInterest() + "\n";
        stringToReturn += "Fund Left To Pay Amount: " + singleLoanInformation.getFundLeftToPay() + "\n";
        stringToReturn += "Interest Left To Pay Amount: " + singleLoanInformation.getInterestLeftToPay() + "\n";

        return stringToReturn;
    }

    private int calculateNextPaymentUnit(LoanInformationDTO singleLoanInformation) {
        if (singleLoanInformation.getLastPaymentTimeunit() == 0) {
            return singleLoanInformation.getBeginningTimeUnit() - 1 + singleLoanInformation.getTimeUnitsBetweenPayments();
        }
        else {
            return singleLoanInformation.getLastPaymentTimeunit() + singleLoanInformation.getTimeUnitsBetweenPayments();
        }
    }

    private String loanPaymentsString(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "*****Payments Details*****\n";
        List<PaymentsDTO> loanPayments = singleLoanInformation.getPaymentsList();
        int counter = 1;

        for (PaymentsDTO singleLoanPayment : loanPayments) {
            stringToReturn += "Payment number " + counter++ +":\n";
            stringToReturn += "Payment Timeunit: " + singleLoanPayment.getPaymentTimeUnit() + "\n";
            stringToReturn += "Fund Amount: " + singleLoanPayment.getFundPayment()+ "\n";
            stringToReturn += "Interest Amount: " + singleLoanPayment.getInterestPayment() + "\n";
            stringToReturn += "Sum Amount(Fund + Interest): " + singleLoanPayment.getPaymentSum() + "\n";
            if (singleLoanInformation.getLoanStatus().equals("RISK")) {
                if (singleLoanPayment.isWasItPaid() == false) {
                    stringToReturn += "NOT PAID!\n";
                }
            }

        }
        stringToReturn += "*****End of Payment Details*****\n";
        return stringToReturn;
    }

    private String pendingStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "";

        stringToReturn += lendersPartInLoanString(singleLoanInformation);
        stringToReturn += "Raised loan amount: " + singleLoanInformation.getPendingMoney() + "\n";
        stringToReturn += "Remaining loan amount to activate: " + (singleLoanInformation.getFundAmount() - singleLoanInformation.getPendingMoney()) + "\n";

        return stringToReturn;
    }

    private String lendersPartInLoanString(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "*****Lenders Information:*****\n";
        int counter = 1;

        for (PartInLoanDTO singlePartInLoanDTO : singleLoanInformation.getLenderSetAndAmounts()) {
            stringToReturn += counter++ + ")";
            stringToReturn += "Lender Name: " + singlePartInLoanDTO.getLenderName() + "\n";
            stringToReturn += "Lender amount in loan: " + singlePartInLoanDTO.getAmountOfLoan() + "\n";
        }
        counter = 1;
        stringToReturn += "*****End of Lenders Information*****\n";
        return stringToReturn;
    }

    private void readFromFile() {
        isFileRead = false;
        Scanner scanUserInput = new Scanner(System.in);
        String userInput;
        System.out.println("Please enter a full path of the XML file you would like to upload: ");
        userInput = scanUserInput.nextLine();
        try {
            m_Engine.readFromFile(userInput.trim());
            System.out.println("The file was uploaded successfully!");
            System.out.println();
            isFileRead = true;
        } catch (FileNotFoundException fileNotFoundEx) {
            System.out.println("The system could not find the file, please check the file path again.");
        }/*
        catch (JAXBException e) {
            System.out.println(e.getMessage());
        }*/
         catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private int isValidInput(String i_UserInputString, int i_MaxOptions, int i_MinOptions) throws ValueOutOfRangeException {
        int userChoice;

        try{
            userChoice =  Integer.parseInt(i_UserInputString);
        }
        catch (NumberFormatException ex){
            throw new NumberFormatException("Invalid input - should be numbers only. ");
        }

        if (userChoice < i_MinOptions || userChoice > i_MaxOptions) {
            throw new ValueOutOfRangeException("Invalid input. ", i_MaxOptions, i_MinOptions);
        }

        return userChoice;
    }

    private void showMenu() {
        System.out.println("Current Timeunit: " + m_Engine.getCurrentTimeUnit().getCurrentTimeUnit());
        System.out.println("Please choose one of the following options:");
        System.out.println("1. Read system settings from a file");
        System.out.println("2. Show existing loans information");
        System.out.println("3. Show clients information ");
        System.out.println("4. Add money to the account");
        System.out.println("5. Withdraw money from the account");
        System.out.println("6. Activate loans distribution");
        System.out.println("7. Promote timeline");
        System.out.println("8. Exit system");
    }
}