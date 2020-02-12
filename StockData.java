import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This is a class that retrieves the stock data
 * Takes in a portfolio which is a list of ticker names to track
 */

public class StockData {

    private HttpClient client;
    private final String baseURIString = "https://financialmodelingprep.com/api/v3/stock/real-time-price/";;
    private List<URI> track;

    /**
     * Constructs a stock data class given a list of ticker names
     * it further creates the List of URI given the ticker names in the portfolio
     * @param portfolio the list of strings of ticker symbols called the portfolio
     * @throws URISyntaxException
     */
    public StockData(List<String> portfolio) throws URISyntaxException {

        client = HttpClient.newHttpClient();

        track = portfolio.parallelStream()
                .map(s -> baseURIString + s )
                .map(m -> {
                    try {
                        return new URI(m);
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.toList());

    }


    /**
     * this method turns the list of URI's to access the API and parses the actual ticker symbols
     * @return the List of strings of the ticker symbols and list may contain null items if the ticker symbol was invalid
     */
    public List<String> tickerSymbols() {
        return track.parallelStream()
                .map( c -> client.sendAsync(HttpRequest.newBuilder(c).build(), HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(StockPriceParser::parseTicker)
                        .join())
                .collect(Collectors.toList());

    }

    /**
     * this method produces the List of prices from the list of URI
     * @return the list of prices of the ticker symbols and the list may contain 0.00 price if URI invalid
     */
    public List<Double> prices() {
        return track.parallelStream()
                .map( c -> client.sendAsync(HttpRequest.newBuilder(c).build(), HttpResponse.BodyHandlers.ofString())
                        .thenApply(HttpResponse::body)
                        .thenApply(StockPriceParser::parsePrice)
                        .join())
                .collect(Collectors.toList());
    }

    /**
     * this is a tester class to check the data or output of above methods
     * @param args
     * @throws URISyntaxException
     */
    public static void main(String[] args) throws URISyntaxException {
        String[] portfolio = {"aapl", "Hees"};
        StockData s = new StockData(Arrays.asList(portfolio));
        System.out.println(s.tickerSymbols() + " " + s.prices());
    }
}
