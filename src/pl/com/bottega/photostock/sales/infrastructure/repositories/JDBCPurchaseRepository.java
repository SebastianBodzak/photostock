package pl.com.bottega.photostock.sales.infrastructure.repositories;

import com.sun.org.apache.bcel.internal.generic.DREM;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.sql.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Dell on 2016-06-07.
 */
public class JDBCPurchaseRepository implements PurchaseRepository {

    private final String url;
    private final String login;
    private final String password;
//    private ProductRepository productRepository = new JDBCProductRepository();

    public JDBCPurchaseRepository(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public Purchase load(String purchaseNumber) throws DataAccessException  {
        try (Connection c = getConnection()) {

            PreparedStatement ps = c.prepareStatement("SELECT id, clientId, createDate FROM Purchases WHERE number = ?");
            ps.setString(1, purchaseNumber);
            ResultSet rs = ps.executeQuery();
            int purchaseId = rs.getInt("id");
            int clientId = rs.getInt("clientId");
            Date date = rs.getDate("createDate");

            ps = c.prepareStatement("SELECT number, name FROM Clients WHERE id = ?");
            ps.setInt(1, clientId);
            rs = ps.executeQuery();
            String clientNr = rs.getString("number");
            String clientName = rs.getString("name");

            ps = c.prepareStatement("SELECT productId FROM PurchasesProducts WHERE purchaseId = ?");
            ps.setInt(1, purchaseId);
            List<Integer> productsIds = new LinkedList<>();
            while (rs.next())
                productsIds.add(rs.getInt("productId"));

            List<Product> products = new LinkedList<>();
            rs = queryProducts(c, productsIds);
//            while (rs.next())
//                products.add()

//            ps = c.prepareStatement("SELECT FROM Products WHERE id =")

//            return new Purchase(new Client(clientNr, clientName), )

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
        return null;
    }

    private ResultSet queryProducts(Connection c, List<Integer> productsIds) throws SQLException {
        String[] questionMarks = new String[productsIds.size()];
        for(int i = 0; i <  productsIds.size(); i++)
            questionMarks[i] = "?";
        PreparedStatement ps = c.prepareStatement("SELECT number FROM Products WHERE id IN (" + String.join(",", questionMarks) + ")");
        for (int i = 1; i <= productsIds.size(); i++)
            ps.setInt(i, productsIds.get(i - 1));
        return ps.executeQuery();
    }
//
//    @Override
//    public Purchase load(String clientNr) throws DataAccessException  {
//        try (Connection c = getConnection()) {
//            PreparedStatement ps = c.prepareStatement("SELECT id FROM Clients WHERE number = ?");
//            ps.setString(1, clientNr);
//            ResultSet rs = ps.executeQuery();
//            int clientId = rs.getInt("id");
//
//            ps = c.prepareStatement("SELECT id, createDate FROM Purchases WHERE clientId = ?");
//            ps.setInt(1, clientId);
//            rs = ps.executeQuery();
//            int purchasesId = rs.getInt("id");
//            Date date
//
//        } catch (Exception ex) {
//            throw new DataAccessException(ex);
//        }
//        return null;
//    }

    @Override
    public void save(Purchase purchase) {

    }

    @Override
    public List<Product> search(Client client) {
        return null;
    }

    @Override
    public List<Purchase> find(String clientNr) {
        return null;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }
}
