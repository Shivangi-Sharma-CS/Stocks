import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Class to parse a json response containing a stock price
 * Dependant on the json-simple package
 * (add as library the json-simple-1.1.1.jar file)
 */
public class StockPriceParser {

    /**
     * Parse out the price from a json string
     * @param jsonString the string containing price : value
     * @return the price
     */
    public static double parsePrice(String jsonString)  {
        double price = 0.0;
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            price = (Double) (jsonObject.get("price"));
        }catch(ParseException e) {
            System.out.println("Invalid JSON to parse");
            //e.printStackTrace();
            return 0.0;
        }catch(NullPointerException e) {
            System.out.println("No JSON string passed in");
            //e.printStackTrace();
            return 0.0;
        }
        return price;
    }

    /**
     * Parse out the ticker symbol from a json string
     * @param jsonString the string containing symbol : ticker name
     * @return the ticker symbol
     */
    public static String parseTicker(String jsonString)  {
        String ticker = "";
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
            ticker = (String) (jsonObject.get("symbol"));
        }catch(ParseException e) {
            System.out.println("Invalid JSON to parse");
            //e.printStackTrace();
            return "error!";
        }catch(NullPointerException e) {
            System.out.println("No JSON string passed in");
            //e.printStackTrace();
            return "No ticker name given";
        }
        return ticker;
    }



    /**
     * Test the parsePrice method
     * @param args
     */
    public static void main(String[] args) throws ParseException {

        //example in the format returned from a call to the stock api
        //https://financialmodelingprep.com/api/v3/stock/real-time-price/aapl
        String jsonString = "[{\n" +
                "  \"symbol\" : \"AAPL\",\n" +
                "  \"price\" : 262.11\n" +
                "}]";

        System.out.println("String as returned from website: ");
        System.out.println(jsonString);
        // Used for debugging
//        JSONParser parser = new JSONParser();
//        JSONArray jsonArray = (JSONArray) parser.parse(jsonString);
//        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
//        String ticker = (String) (jsonObject.get("symbol"));
//        System.out.println(ticker);
//        double price = (Double) (jsonObject.get("price"));

        String ticker = parseTicker(jsonString);
        System.out.println(ticker);

        double price = parsePrice(jsonString);
        System.out.println(price);

    }

}
