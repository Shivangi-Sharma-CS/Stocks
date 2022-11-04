import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.NoSuchElementException;

/**
 * The main method for the Stock Server which creates the stock bank and accepts the client port from the socket and runs the server
 */
public class StockServer {
    public static void main(String[] args) throws IOException, URISyntaxException, ClassNotFoundException, SQLException, NoSuchElementException {

        StockBank stockBank = new StockBank();

        String[] users = {"Andrew", "Sophia", "Julia", "Martin"};
        stockBank.addStockBank(Arrays.asList(users));

        final int PORT = 8890;

        ServerSocket server = new ServerSocket(PORT);

        System.out.println("Waiting for clients to connect.....");

        while(true) {
            Socket s = server.accept();
            System.out.println("Client connected.");
            StockService service = new StockService(s,stockBank);
            Thread t = new Thread(service);
            t.start();
        }

    }
}
