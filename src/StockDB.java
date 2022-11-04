import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a class that creates a database for the Stock service
 */
public class StockDB {
    private Connection conn;
    private Statement stat;
    public StockDB() throws IOException, ClassNotFoundException, SQLException {
        SimpleDataSource.init("src/database.properties");
        conn = SimpleDataSource.getConnection();
        stat = conn.createStatement();
    }

    /**
     * Creates a table if the table does not exists
     * @throws SQLException
     */
    public void createTable() throws SQLException {
        if(!tableExists()) {
            stat.execute("CREATE TABLE Stocks1 (" +
                    "UserName VARCHAR(40)," +
                    "Portfolio VARCHAR(10)" +
                    ")");
        }

    }

    /**
     * Adds a ticker and the username to the database table; both are foreign keys
     * @param userName the name of the user
     * @param tickerName the name of the ticker name
     * @throws SQLException
     */
    public void addTicker(String userName, String tickerName) throws SQLException {
            stat.execute("INSERT INTO Stocks1" +
                    " VALUES ('" + userName + "','" + tickerName + "')");

    }

    /**
     * Forget a ticker for that user name in the database table
     * @param userName the name of the user
     * @param tickerName the name of the ticker
     * @throws SQLException
     */
    public void forgetTicker (String userName, String tickerName) throws SQLException {
            stat.execute(
                    "DELETE FROM Stocks1 WHERE UserName ='" + userName + "' AND Portfolio ='" + tickerName + "'");
    }

    /**
     * Given the user name, selects the ticker names for that user and returns the list called portfolio which contains the ticker names
     * @param userName the name of the user
     * @return
     * @throws SQLException
     */
    public List<String> portfolio (String userName) throws SQLException {
        List<String> portfolioList = new ArrayList<>();
        ResultSet result = stat.executeQuery("SELECT Portfolio FROM Stocks1 WHERE UserName ='" + userName + "'");
        while (result.next()) {
            portfolioList.add(result.getString("Portfolio"));
        }
        return portfolioList;
    }

    /**
     * Checks whether or not the table exists
     * @return true is table exists, false of it doesn't
     * @throws SQLException
     */
    public boolean tableExists() throws SQLException {

        ResultSet rs = conn.getMetaData().getTables("SCHEMA_NAME", null, "STOCKS1", new String[] {"TABLE"});
        while (rs.next()) {
            return true;
        }
        return false;
    }

}
