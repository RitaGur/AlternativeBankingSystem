package consoleUI;

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

    private void addMoney() {
    }

    private void showClientsInformation() {
        m_Engine.showClientsInformation();
    }

    private void showLoansInformation() {
        Set<LoanInformationDTO> loanInfo = m_Engine.showLoansInformation();
        for (LoanInformationDTO loanDTO : loanInfo) {
            printLoansInformation();
        }
    }

    private void printLoansInformation() {
        Set<LoanInformationDTO> loansInformationList = m_Engine.showLoansInformation();
        String stringToPrint = "Loan Information:\n";

        for (LoanInformationDTO singleLoanInformation:loansInformationList) {
            stringToPrint +=
                    "Loan ID: " + singleLoanInformation.getLoanNameID() +
                     "Loan Owner: " + singleLoanInformation.getBorrowerName() +
                    "Loan Category: " + singleLoanInformation.getLoanCategory() +
                    "Loan Status: " + singleLoanInformation.getLoanStatus();
            stringToPrint += printByLoanStatus(singleLoanInformation.getLoanStatus(), singleLoanInformation);
        }
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

                break;
            case "RISK":

                break;
            case "FINISHED":

                break;
        }

        return stringToReturn;
    }

    private String pendingStatus(LoanInformationDTO singleLoanInformation) {
        String stringToReturn = "Lenders Information";
        int counter = 1;

        for (PartInLoanDTO singlePartInLoanDTO : singleLoanInformation.getLenderSetAndAmounts()) {
            stringToReturn += counter++ + ")";
            stringToReturn += "Lender Name: " + singlePartInLoanDTO.getLenderName();
            stringToReturn += "Lender amount in loan: " + singlePartInLoanDTO.getAmountOfLoan();
        }

        stringToReturn += "Raised loan amount: " + singleLoanInformation.getPendingMoney();
        stringToReturn += "Remaining loan amount to activate: " + (singleLoanInformation.getFundAmount() - singleLoanInformation.getPendingMoney());

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
