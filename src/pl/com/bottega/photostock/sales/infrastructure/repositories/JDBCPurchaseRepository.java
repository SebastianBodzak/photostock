package pl.com.bottega.photostock.sales.infrastructure.repositories;

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

    public JDBCPurchaseRepository(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public Purchase load(String purchaseNumber) throws DataAccessException  {
        try (Connection c = getConnection()) {

            PreparedStatement ps = c.prepareStatement("SELECT pp.purchaseId, purch.number, c.number, c.name, c.address, s.name, c.debtCents, c.amountCents,\n" +
                    "  c.creditLimitCents, c.amountCurrency, c.active\n" +
                    "FROM PurchasesProducts pp\n" +
                    "  JOIN Purchases purch ON pp.purchaseId = purch.id\n" +
                    "  JOIN Clients c ON c.id = purch.clientId\n" +
                    "  JOIN Statuses s ON s.id = c.statusId\n" +
                    "WHERE purch.number = ?;");

            ps.setString(1, purchaseNumber);
            ResultSet rs = ps.executeQuery();

            if (!rs.next())
                return null;
            int purchaseId = rs.getInt("purchaseId");
            String clientNr = rs.getString("Clients.number");
            String clientName = rs.getString("clients.name");
            String clientAddress = rs.getString("address");
            ClientStatus clientStatus = ClientStatus.valueOf(rs.getString("statuses.name").toUpperCase());
            Money.CurrencyValues currency = Money.CurrencyValues.valueOf(rs.getString("amountCurrency").toUpperCase());
            Money debt = new Money(rs.getInt("debtCents"), currency);
            Money amount = new Money(rs.getInt("amountCents"), currency);
            Money creditLimit = new Money(rs.getInt("creditLimitCents"), currency);
            boolean active = rs.getBoolean("active");

            Client owner = new Client(clientNr, clientName, clientAddress, clientStatus, debt, amount, creditLimit, active);


            ps = c.prepareStatement("SELECT pp.productId, prod.number, prod.name, prod.priceCents, prod.priceCurrency, prod.available\n" +
                    "FROM PurchasesProducts pp\n" +
                    "  JOIN Products prod ON prod.id = pp.productId\n" +
                    "WHERE pp.purchaseId = ?;");
            ps.setInt(1, purchaseId);
            List<Product> products = new LinkedList<>();
            rs = ps.executeQuery();
            while (rs.next()) {
                int productId = rs.getInt("PurchasesProducts.productId");
                String productNumber = rs.getString("number");
                String productName = rs.getString("name");
                Money.CurrencyValues productCurrency = Money.CurrencyValues.valueOf(rs.getString("priceCurrency").toUpperCase());
                Money price = new Money(rs.getInt("priceCents"), productCurrency);
                boolean available = rs.getBoolean("available");
                products.add(new Picture(productNumber, productName, price, available, loadTags(c, productId)));
            }

            return new Purchase(purchaseNumber, owner, products);

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

    @Override
    public void save(Purchase purchase) throws  DataAccessException {
        try (Connection c = getConnection()) {
            ResultSet rs = clientIdQuery(c, purchase.getOwner().getNumber());
            rs.next();
                int clientId = rs.getInt("id");

            PreparedStatement ps = c.prepareStatement("INSERT INTO Purchases (number, clientId) VALUES (?, ?);");
            if (purchase.getNumber() == null)
                ps.setString(1, UUID.randomUUID().toString());
            ps.setString(1, purchase.getNumber());
            ps.setInt(2, clientId);
            ps.executeUpdate();

            rs = purchaseIdQuery(c, purchase.getNumber());
            rs.next();
            int purchaseId = rs.getInt("id");

            Set<Integer> productsId = getProductsId(c, purchase.getItems());
            linkProducts(c, purchaseId, productsId);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private ResultSet clientIdQuery(Connection c, String number) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT id FROM Clients WHERE number = ?;");
        ps.setString(1, number);
        return ps.executeQuery();
    }

    private ResultSet purchaseIdQuery(Connection c, String number) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT id FROM Purchases WHERE number = ?;");
        ps.setString(1, number);
        return ps.executeQuery();
    }

    private Set<Integer> getProductsId(Connection c, List<Product> items) throws SQLException {
        String[] questionMarks = new String[items.size()];
        for (int mark = 0; mark < items.size(); mark++ )
            questionMarks[mark] = "?";
        PreparedStatement ps = c.prepareStatement("SELECT id FROM Products WHERE number IN (" + String.join(",", questionMarks) +");");
        for (int i = 1; i <= items.size(); i++)
            ps.setString(i, items.get(i - 1).getNumber());
        ResultSet rs = ps.executeQuery();

        Set<Integer> productsId = new LinkedHashSet<>();
        while (rs.next())
            productsId.add(rs.getInt("id"));
        return productsId;
    }

    private void linkProducts(Connection c, int purchaseId, Set<Integer> productsId) throws SQLException {
        for (int prodId : productsId)
                linkProduct(c, purchaseId, prodId);
    }

    private void linkProduct(Connection c, int purchaseId, int prodId) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO PurchasesProducts VALUES (?,?)");
        ps.setInt(1, purchaseId);
        ps.setInt(2, prodId);
        ps.executeUpdate();
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
