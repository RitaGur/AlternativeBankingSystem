package consoleUI;

import DTO.client.ClientInformationDTO;
import DTO.client.RecentTransactionDTO;
import DTO.loan.LoanInformationDTO;
import DTO.loan.PartInLoanDTO;
import bankingSystem.BankingSystem;
import bankingSystem.LogicInterface;
import exception.ValueOutOfRangeException;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class ConsoleStart {

    private final int f_MaxOptionsInMenu = 8;

    private BankingSystem m_Engine;

    public ConsoleStart() {
        m_Engine = new BankingSystem();
    }

    //TODO: delete throws Exception!!
    public void run() throws Exception {
        startService();
    }

    //TODO: delete throws Exception!!
    private void startService() throws Exception {
        Set<String> categories = new HashSet<>();
        categories.add("abc");
        categories.add("drf");
        m_Engine.updateLoansCategories(categories);
        // TODO: delete
        m_Engine.addBankClient(5000, "Menash");
        m_Engine.addLoan("Stock market intro", "Menash", 2400, 12, 1, 5, "abc");

        showMenu();
        String userInputString;
        int userInputInt = 0;
        Scanner scanUserInput = new Scanner(System.in);

        do {
            try{
                userInputString = scanUserInput.next();
                userInputInt = isValidInput(userInputString, 8, 1);
                userInputChoices(userInputInt);
            }
            catch (ValueOutOfRangeException i_OutOfRangeException){
                System.out.println(i_OutOfRangeException.getMessage() + "Please enter a number between " + i_OutOfRangeException.getM_MinValue() + "-" + i_OutOfRangeException.getMaxValue());
            }
            catch (NumberFormatException i_NumberFormatException){
                System.out.println(i_NumberFormatException.getMessage());
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
                    //TODO: if a file was not read, we cant show loans info
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

    private void promoteTimeline() {

    }

    private void loansDistribution() {
    }

    private void withdrawMoney() {
    }

    private void addMoney() throws Exception {
        Set<ClientInformationDTO> clientInfoList = m_Engine.showClientsInformation();
        int counter = 0;
        System.out.println("Please choose the account you would like to add money to(enter full name): ");
        for (ClientInformationDTO clientDTO: clientInfoList) {
            System.out.println(counter++ + ")" + clientDTO.getClientName());
        }
        String userAccountInputString;
        int userAccountInput, userAmountOfMoneyInput;
        Scanner scanUserInput = new Scanner(System.in);
        userAccountInputString = scanUserInput.next();

        System.out.println("Please enter the amount of money you would like to add to the chosen account: ");
        userAmountOfMoneyInput = scanUserInput.nextInt();

        m_Engine.addMoneyToAccount(userAccountInputString, userAmountOfMoneyInput);
    }

    private void showClientsInformation() {
        Set<ClientInformationDTO> clientInfo = m_Engine.showClientsInformation();

        for (ClientInformationDTO clientDTO : clientInfo) {
            printClientInformation(clientDTO);
        }
    }

    private void printClientInformation(ClientInformationDTO singleClientInformation) {
        String stringToPrint = "Client Information:\n";
        stringToPrint += "Client name: " + singleClientInformation.getClientName();
        int counter = 0;

        // recent transactions:
        stringToPrint+= "Last Transactions:\n";
        Set<RecentTransactionDTO> recentTransactionList = singleClientInformation.getRecentTransactionList();
        for (RecentTransactionDTO singleTransaction : recentTransactionList) {
            stringToPrint += counter++ + ")";
            stringToPrint += clientsRecentTransactionString(singleTransaction);
        }

        counter = 0;

        // loans as borrower info
        stringToPrint+= "Client loan as Borrower:\n";
        for (LoanInformationDTO loanAsBorrower : singleClientInformation.getClientAsBorrowerLoanList()) {
            stringToPrint += counter++ + ")";
            stringToPrint += clientLoansInformationString(loanAsBorrower);
        }

        counter = 0;

        //loans as lender info
        stringToPrint+= "Client loan as Lender:\n";
        for (LoanInformationDTO loanAsBorrower : singleClientInformation.getClientAsBorrowerLoanList()) {
            stringToPrint += counter++ + ")";
            stringToPrint += clientLoansInformationString(loanAsBorrower);
        }

        System.out.println(stringToPrint);
    }

    private String clientsRecentTransactionString(RecentTransactionDTO singleTransaction) {
        String stringToReturn = "Transaction Information: ";
        stringToReturn += "Transaction Timeunit: " + singleTransaction.getTransactionTimeUnit() + "\n" +
                "Transaction Amount: " +singleTransaction.getAmountOfTransaction() + "\n" +
                "Kind Of Transaction: " + singleTransaction.getKindOfTransaction() + "\n" +
                "Account Balance Before Transaction: " + singleTransaction.getBalanceBeforeTransaction() + "\n" +
                "Account Balance After Transaction: " + singleTransaction.getBalanceAfterTransaction() + "\n";

        return stringToReturn;
    }

    private String clientLoansInformationString(LoanInformationDTO loan) {
        String stringToPrint = "Loan Information:\n";
        stringToPrint +=
                "Loan ID: " + loan.getLoanNameID() + "\n" +
                        "Loan Category: " + loan.getLoanCategory() + "\n" +
                        "Loan Initial Amount: " + loan.getLoanStartSum() + "\n" +
                        "TimeUnits Between Payments: " + loan.getTimeUnitsBetweenPayments() + "\n" +
                        "Loan Interest: " + loan.getLoanInterest() + "\n" +
                        "Loan Final Amount(Fund + Interest): " + loan.getSumAmount() + "\n" +
                        "Loan Status: " + loan.getLoanStatus();
        stringToPrint += clientLoanStatusInformation(loan.getLoanStatus(), loan);

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
        //Todo: complete
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
        Set<LoanInformationDTO> loanInfoList = m_Engine.showLoansInformation();

        for (LoanInformationDTO loanDTO : loanInfoList) {
            printLoanInformation(loanDTO);
        }
    }

    private void printLoanInformation(LoanInformationDTO singleLoanInformation) {
        String stringToPrint = "Loan Information:\n";
        stringToPrint +=
                    "Loan ID: " + singleLoanInformation.getLoanNameID() + "\n" +
                     "Loan Owner: " + singleLoanInformation.getBorrowerName() + "\n" +
                    "Loan Category: " + singleLoanInformation.getLoanCategory() + "\n" +
                    "Loan Status: " + singleLoanInformation.getLoanStatus() + "\n" +
                     "Loan Sum: " + singleLoanInformation.getLoanStartSum() + "\n" +
                     "Loan Time Duration: " + singleLoanInformation.getLoanSumOfTimeUnit() + "\n" +
                     "Loan Interest: " + singleLoanInformation.getLoanInterest() + "\n" +
                     "Timeunits between payments: " + singleLoanInformation.getTimeUnitsBetweenPayments();
        stringToPrint += printByLoanStatus(singleLoanInformation.getLoanStatus(), singleLoanInformation);

        System.out.println(stringToPrint);
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
        // TODO: information about previous payments

        return stringToReturn;
    }

    private String riskStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = activeStatus(singleLoanInformation);
        //TODO: information about unpaid payments

        return stringToReturn;
    }

    private String activeStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = lendersPartInLoanString(singleLoanInformation);
        // TODO: information about previous payments

        stringToReturn += "Paid Fund Amount: " + singleLoanInformation.getPaidFund() + "\n";
        stringToReturn += "Paid Interest Amount: " + singleLoanInformation.getPaidInterest() + "\n";
        stringToReturn += "Fund Left To Pay Amount: " + singleLoanInformation.getFundLeftToPay() + "\n";
        stringToReturn += "Interest Left To Pay Amount: " + singleLoanInformation.getInterestLeftToPay() + "\n";

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
        String stringToReturn = "Lenders Information\n";
        int counter = 1;

        for (PartInLoanDTO singlePartInLoanDTO : singleLoanInformation.getLenderSetAndAmounts()) {
            stringToReturn += counter++ + ")";
            stringToReturn += "Lender Name: " + singlePartInLoanDTO.getLenderName() + "\n";
            stringToReturn += "Lender amount in loan: " + singlePartInLoanDTO.getAmountOfLoan() + "\n";
        }

        return stringToReturn;
    }

    private void readFromFile() {
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
