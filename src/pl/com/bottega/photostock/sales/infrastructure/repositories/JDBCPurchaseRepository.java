package pl.com.bottega.photostock.sales.infrastructure.repositories;

import com.sun.org.apache.bcel.internal.generic.DREM;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.sql.*;
import java.sql.Date;
import java.util.*;

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

            PreparedStatement ps = c.prepareStatement("SELECT pp.purchaseId, purch.number, purch.createDate, c.number, c.name, c.address, s.name, c.debtCents, c.amountCents,\n" +
                    "  c.creditLimitCents, c.amountCurrency, c.active\n" +
                    "FROM PurchasesProducts pp\n" +
                    "  JOIN Purchases purch ON pp.purchaseId = purch.id\n" +
                    "  JOIN Clients c ON c.id = purch.clientId\n" +
                    "  JOIN Statuses s ON s.id = c.statusId\n" +
                    "WHERE purch.number = ?;");

            ps.setString(1, purchaseNumber);
            ResultSet rs = ps.executeQuery();
            int purchaseId = rs.getInt("pp.purchaseId");
            Date date = rs.getDate("purch.createDate");
            String clientNr = rs.getString("c.number");
            String clientName = rs.getString("c.name");
            String clientAddress = rs.getString("c.address");
            ClientStatus clientStatus = ClientStatus.valueOf(rs.getString("s.name"));
            Money.CurrencyValues currency = Money.CurrencyValues.valueOf("c.amountCurrency");
            Money debt = new Money(rs.getInt("c.debtCents"), currency);
            Money amount = new Money(rs.getInt("c.amountCents"), currency);
            Money creditLimit = new Money(rs.getInt("c.creditLimitCents"), currency);
            boolean active = rs.getBoolean("c.active");

            Client owner = new Client(clientNr, clientName, clientAddress, clientStatus, debt, amount, creditLimit, active);


            ps = c.prepareStatement("SELECT pp.purchaseId, pp.productId, prod.number, prod.name, prod.priceCents, prod.priceCurrency, prod.available\n" +
                    "FROM PurchasesProducts pp\n" +
                    "  JOIN Products prod ON prod.id = pp.productId\n" +
                    "WHERE pp.purchaseId = ?;");
            ps.setInt(1, purchaseId);
            List<Product> products = new LinkedList<>();
            while (rs.next()) {
                int productId = rs.getInt("pp.productId");
                String productNumber = rs.getString("prod.number");
                String productName = rs.getString("prod.number");
                Money.CurrencyValues productCurrency = Money.CurrencyValues.valueOf("c.amountCurrency");
                Money price = new Money(rs.getInt("c.priceCents"), productCurrency);
                boolean available = rs.getBoolean("prod.available");
                products.add(new Picture(productNumber, productName, price, available, loadTags(c, productId)));
            }

            return new Purchase(purchaseNumber, owner, products, String.valueOf(date));

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private List<String> loadTags(Connection c, int nr) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT Tags.name\n" +
                "FROM Tags\n" +
                "  JOIN ProductsTags ON ProductsTags.tagId = Tags.id\n" +
                "  JOIN Products ON Products.id = ProductsTags.productId\n" +
                "WHERE Products.id = ?;");
        ps.setInt(1, nr);
        ResultSet rs = ps.executeQuery();
        List<String> tags = new LinkedList<>();
        while (rs.next())
            tags.add(rs.getString("name"));
        return tags;
    }


//    @Override
//    public Purchase load(String purchaseNumber) throws DataAccessException  {
//        try (Connection c = getConnection()) {
//
//            PreparedStatement ps = c.prepareStatement("SELECT id, clientId, createDate FROM Purchases WHERE number = ?");
//            ps.setString(1, purchaseNumber);
//            ResultSet rs = ps.executeQuery();
//            int purchaseId = rs.getInt("id");
//            int clientId = rs.getInt("clientId");
//            Date date = rs.getDate("createDate");
//
//            ps = c.prepareStatement("SELECT number, name FROM Clients WHERE id = ?");
//            ps.setInt(1, clientId);
//            rs = ps.executeQuery();
//            String clientNr = rs.getString("number");
//            String clientName = rs.getString("name");
//
//            ps = c.prepareStatement("SELECT productId FROM PurchasesProducts WHERE purchaseId = ?");
//            ps.setInt(1, purchaseId);
//            List<Integer> productsIds = new LinkedList<>();
//            while (rs.next())
//                productsIds.add(rs.getInt("productId"));
//
//            List<Product> products = new LinkedList<>();
//            rs = queryProducts(c, productsIds);
////            while (rs.next())
////                products.add()
//
////            ps = c.prepareStatement("SELECT FROM Products WHERE id =")
//
////            return new Purchase(new Client(clientNr, clientName), )
//
//        } catch (Exception ex) {
//            throw new DataAccessException(ex);
//        }
//        return null;
//    }

    @Override
    public void save(Purchase purchase) throws  DataAccessException {
        try (Connection c = getConnection()) {
            PreparedStatement ps = c.prepareStatement("INSERT INTO Purchases (number) VALUES (?);");
            ps.setString(1, UUID.randomUUID().toString());

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
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
