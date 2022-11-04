import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class StockService implements Runnable {

    private Socket s;
    private Scanner in;
    private PrintWriter out;
    private StockBank stockBank;
    private StockDB database;

    /**
     * Runs a service for the stock bank given the object the stock bank and the socket
     * @param s the socket for the server of the StockBank
     * @param stockBank StockBank object for which the service has to run
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public StockService(Socket s, StockBank stockBank) throws IOException, ClassNotFoundException, SQLException {
        this.s = s;
        this.stockBank = stockBank;

        database = new StockDB();
        database.createTable();
    }

    /**
     * run method for this class which scans the input stream from the socket and output stream to provide server response
     */
    public void run() {
        try {
            try {
                in = new Scanner(s.getInputStream()).useDelimiter("!");
                out = new PrintWriter(s.getOutputStream(), true);
                doService();
            }
            finally {
                s.close();
            }
        }
        catch (IOException | URISyntaxException | SQLException | NoSuchElementException e) {

        }
    }

    /**
     * this is the menu service for the server clients that has to run on the server
     * @throws URISyntaxException
     * @throws SQLException
     * @throws NoSuchElementException
     */
    private void doService() throws URISyntaxException, SQLException, NoSuchElementException {
        while(true) {
            out.println("please login by typing just USER only, don't type your username yet, you will be prompted for each data!");
            String command = in.next();
            System.out.println("Received command: " + command);

            StockAccount user = null;

            if (command.equals("USER")) {
                out.println("please type in username!");
                String userName = in.next();

                if (stockBank.getStockAccount(userName) == null) {
                    out.println("Login failed. We are creating a new account with this username");

                    stockBank.addStockAccount(userName);
                    user = stockBank.getStockAccount(userName);

                    out.println("type in commands from ONLY the following: TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                    command = in.next();
                }
                else if (stockBank.getStockAccount(userName) != null) {
                    out.println("Welcome " + userName);
                    user = stockBank.getStockAccount(userName);

                    user.setPortfolio(database.portfolio(userName));

                    out.println("type in commands from ONLY the following: TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                    command = in.next();
                }

                while ((user != null) && (!command.equals("LOGOUT"))) {

                    if (command.equals("TRACK")) {
                        out.println("please type in ticker symbol!");
                        String tickerName = in.next();
                        String s = user.addTicker(tickerName);
                        out.println(s);

                        if(s.equals("added to your portfolio")) {
                            database.addTicker(userName, tickerName);
                        }

                        out.println("please type in commands from TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                        command = in.next();
                    }
                    if (command.equals("FORGET")) {
                        out.println("please type in ticker name!");
                        String tickerName = in.next();
                        String s = user.forgetTicker(tickerName);
                        out.println(s);

                        if(s.equals("removed from your portfolio")) {
                            database.forgetTicker(userName, tickerName);
                        }
                        out.println("please type in commands from TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                        command = in.next();
                    }
                    if (command.equals("PORTFOLIO")) {
                        out.println("here is your portfolio summary: \n");

                        out.println(user.portfolioResult());
                        out.println("please type in commands from TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                        command = in.next();
                    }
                    if((!command.equals("TRACK")) && (!command.equals("FORGET")) && (!command.equals("PORTFOLIO")) && (!command.equals("LOGOUT"))) {
                        out.println("error");
                        out.println("please type in commands from TRACK, FORGET, PORTFOLIO, LOGOUT. You will be prompted for ticker symbol when required!");
                        command = in.next();
                    }
                }
            }
            else {
                out.println("please type in USER to start login process");
            }
        }

    }

}
