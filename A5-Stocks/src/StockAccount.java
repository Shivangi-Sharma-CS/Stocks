import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This class handles a user account
 * The user account has the following properties :
 * an owner : its username
 * its portfolio
 * add ticker
 * forget ticker
 * we make them thread safe
 */

public class StockAccount {

    private String userName;
    private List<String> portfolio;
    private StockData stockData;
    private Lock lock;
    private Condition sufficientTickerCondition;

    /**
     * Constructs a stock account given a name
     * @param userName the name of the user of the account
     * @throws URISyntaxException
     */
    public StockAccount(String userName) throws URISyntaxException {
        this.userName = userName;
        portfolio = new ArrayList<>();
        lock = new ReentrantLock();
        sufficientTickerCondition = lock.newCondition();
    }

    /**
     * Gets the username of the stock account
     * @return the username of the account
     */
    public String getUserName() {
        lock.lock();
        String s = "";
        try {
            s = userName;
        }
        finally {
            lock.unlock();
        }
        return s;
    }

    /**
     * Sets the username of the stock account ( is not used for the assignment )
     * @param userName the name of the user of the account
     */
    public void setUserName(String userName) {
        lock.lock();
        try {
            this.userName = userName;
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * Sets the portfolio for the stock account given a list of ticker symbols called portfolio here
     * @param portfolio the list of ticker names
     * @throws URISyntaxException
     */
    public void setPortfolio(List<String> portfolio) throws URISyntaxException {
        lock.lock();
        stockData = new StockData(portfolio);
        try {
            for (int i = 0; i < portfolio.size(); i++) {
                if (stockData.tickerSymbols().get(i) == null) {
                    portfolio.remove(i);
                }
            }
            this.portfolio = portfolio;
            sufficientTickerCondition.signalAll();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * Gets the list of the ticker names called the portfolio of a stock account
     * @return the list of ticker names in the list called portfolio
     */
    public List<String> getPortfolio() {
        lock.lock();
        List<String> a;
        try {
            a = this.portfolio;
        }
        finally {
            lock.unlock();
        }
        return a;
    }

    /**
     * Gets the result of the portfolio of the stock account
     * @return String of the portfolio with ticker and its prices
     */
    public String portfolioResult() {
        lock.lock();
        StringBuilder s = new StringBuilder();
        try {
            if(! portfolio.isEmpty()) {
                stockData = new StockData(portfolio);
                for (int i = 0; i < portfolio.size(); i++) {
                    s.append(stockData.tickerSymbols().get(i)).append(" ").append(stockData.prices().get(i)).append("\n");  // DEBUG
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return s.toString();
    }

    /**
     * Adds a ticker to the portfolio
     * @param tickerName the name of the ticker to add in the portfolio
     * @return statement about whether or not the ticker was added
     * @throws URISyntaxException
     */
    public String addTicker(String tickerName) throws URISyntaxException {
        lock.lock();
        String statement = "";
        try {
            List<String> temp = new ArrayList<>();
            temp.add(tickerName);
            StockData tempCheck = new StockData(temp);
            if (tempCheck.tickerSymbols().get(0) == null) {
                statement =  "invalid ticker";
            } else {
                if (portfolio.contains(tickerName)) {
                    statement = "this ticker is already in your portfolio";
                }
                else {
                    portfolio.add(tickerName);
                    statement = "added to your portfolio";
                    sufficientTickerCondition.signalAll();
                }
            }
        }
        finally {
            lock.unlock();
        }
        return statement;
    }

    /**
     * Forgets a ticker in the portfolio to track
     * @param tickerName the name of the ticker
     * @return statement about whether or not the ticker was forgotten
     */
    public String forgetTicker(String tickerName) {
        lock.lock();
        String statement = "";
        try {
            if(portfolio.size() == 0) {
                statement = "there is nothing in your portfolio";
                sufficientTickerCondition.await();
            }
            List<String> temp = new ArrayList<>();
            temp.add(tickerName);
            StockData tempCheck = new StockData(temp);
            if (tempCheck.tickerSymbols().get(0) == null) {
                statement = "invalid ticker";
            }
            else {
                if(portfolio.remove(tickerName)) {
                    statement = "removed from your portfolio";
                }
                else { statement = "it was not there in your portfolio"; }
            }
        } catch (URISyntaxException | InterruptedException e) {

        }
        finally {
            lock.unlock();
        }
        return statement;
    }

    /**
     * Gives a string for a stock account
     * @return the string of this stock account
     */
    public String toString() {
        System.out.println( "userName: " + userName + "\n\t" );
        StringBuilder s = new StringBuilder();
        if(portfolio.size() != 0) {
            try {
                stockData = new StockData(portfolio);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < portfolio.size(); i++) {
                s.append(stockData.tickerSymbols().get(i)).append(" ").append(stockData.prices().get(i));
            }
        }
        else {
            s.append("Nothing in portfolio");
        }
        return s.toString();
    }


}
