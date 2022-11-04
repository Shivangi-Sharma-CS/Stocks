import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * It is a bank of multiple stock accounts
 */
public class StockBank {

    private List<StockAccount> accounts;

    /**
     * Creates a Stock Bank which is a collection of Stock accounts
     */
    public StockBank() {
        accounts = new ArrayList<>();
    }

    /**
     * Creates a stock bank given the list of user names
     * @param userNames the list of user names that have a stock account in the stock bank
     * @throws URISyntaxException
     */
    public void addStockBank(List<String> userNames) throws URISyntaxException {
        for (String userName : userNames) {
            accounts.add(new StockAccount(userName));
        }
    }

    /**
     * Adds a stock account in the stock bank given a user name
     * @param userName the name of the user
     * @throws URISyntaxException
     */
    public void addStockAccount (String userName) throws URISyntaxException {
        StockAccount newAccount = new StockAccount(userName);
        accounts.add(newAccount);
    }

    /**
     * Returns a stock account in the stock bank given a user name
     * @param userName the name of the user who is owner of the stock account
     * @return StockAccount of the user contained in the bank
     */
    public StockAccount getStockAccount (String userName) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getUserName().equals(userName)) {
                return accounts.get(i);
            }
        }
        return null;
    }

    /**
     * It removes the stock account from the bank given a user name
     * @param userName the name of the user who is the owner of the stock account in the bank
     */
    public void removeStockAccount(String userName) {
        for(int i = 0; i< accounts.size(); i++) {
            if(accounts.get(i).getUserName().equals(userName)) {
                accounts.remove(i);
            }
        }
    }

    /**
     * The statement for the accounts in the stock bank
     * @return the statement of the stock accounts in the bank
     */
    public String toString() {
        String s = "";
        for(StockAccount w : accounts ) {
            s += w.toString() + "\n";
        }
        return s;
    }

}
