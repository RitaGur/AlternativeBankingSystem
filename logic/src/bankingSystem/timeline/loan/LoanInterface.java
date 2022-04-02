package bankingSystem.timeline.loan;

import bankingSystem.timeline.bankAccount.BankAccount;
import bankingSystem.timeline.bankClient.BankClient;

import java.util.Set;

public interface LoanInterface {
    public int howManyTimeUnitsLeftForLoan(int i_CurrentTimeUnit);
    public Set<PartInLoan> lendersList();
}
