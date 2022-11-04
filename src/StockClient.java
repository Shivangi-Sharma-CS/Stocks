import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This is the stock client class which connects to the server port and remains connected while the connection is not terminated from the client side
 */
public class StockClient {

    public static void main(String[] args) throws IOException, NoSuchElementException {
        final int PORT = 8890;

        try(Socket s = new Socket("localhost", PORT)) {
            Scanner in = new Scanner(s.getInputStream()).useDelimiter("!");
            PrintWriter out = new PrintWriter(s.getOutputStream(), true);

            Scanner console = new Scanner(System.in);

            String serverResponse = "";
            String clientCommand = "";

            while (s.isConnected()) {
                serverResponse = in.next();
                System.out.println("Server: " + serverResponse);
                clientCommand = console.nextLine();
                out.print(clientCommand + "!");
                out.flush();
            }
        }
    }
}
