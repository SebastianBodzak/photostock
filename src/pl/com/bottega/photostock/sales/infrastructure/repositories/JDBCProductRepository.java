package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.sql.*;
import java.util.*;

/**
 * Created by Dell on 2016-05-29.
 */
public class JDBCProductRepository implements ProductRepository {

    private final String url;
    private final String login;
    private final String password;

    public JDBCProductRepository(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public Product load(String nr) throws DataAccessException {
        try (Connection c = getConnection()) {
            PreparedStatement preparedStatement  = c.prepareStatement("SELECT number, name, priceCents, priceCurrency, available FROM Products WHERE number = ?");
            preparedStatement.setString(1, nr);
            ResultSet rs = preparedStatement.executeQuery();

            if (!rs.next())
                return null; //dopóki coś zwraca
            Money.CurrencyValues currency = Money.CurrencyValues.valueOf(rs.getString("priceCurrency"));
            return new Picture(rs.getString("number"), rs.getString("name"), new Money((rs.getInt("priceCents") / 100), currency), rs.getBoolean("available"), loadTags(c, nr));

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private List<String> loadTags(Connection c, String nr) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT Tags.name\n" +
                "FROM Tags\n" +
                "  JOIN ProductsTags ON ProductsTags.tagId = Tags.id\n" +
                "  JOIN Products ON Products.id = ProductsTags.productId\n" +
                "WHERE Products.number = ?;");
        ps.setString(1, nr);
        ResultSet rs = ps.executeQuery();
//        Set<String> tags = new HashSet<>();
        List<String> tags = new LinkedList<>();
        while (rs.next())
            tags.add(rs.getString("name"));
        return tags;
    }

    @Override
    public void save(Product product) throws DataAccessException {
        try (Connection c = getConnection()) {
            try {
                c.setAutoCommit(false);
                c.setTransactionIsolation(Connection.TRANSACTION_REPEATABLE_READ);
                boolean ifProductExists = ifProductExist(product);
                String query = ifProductExists ?
                        "UPDATE PRODUCTS SET number = ?, name = ?, available = ?, priceCents = ?, priceCurrency = ?, type = '' WHERE number = ?"
                        : "INSERT INTO Products (number, name, available, priceCents, priceCurrency, type) VALUES (?, ?, ?, ?, ?, '');";

                PreparedStatement ps = c.prepareStatement(query);
                if (ifProductExists)
                    ps.setString(6, product.getNumber());

                if (product.getNumber() == null)
                    product.setNumber(UUID.randomUUID().toString());
                ps.setString(1, product.getNumber());
                ps.setString(2, product.getTitle());
                ps.setBoolean(3, product.isAvailable());
                ps.setInt(4, product.getPrice().cents());
                String currency = String.valueOf(product.getPrice().getCurrency());
                ps.setString(5, currency);
                ps.executeUpdate();
                if (!(product.getTags() == null))
                    insertTags(c, product);
                c.commit();
            } catch (Exception ex) {
                c.rollback();
                throw ex;
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private boolean ifProductExist(Product product) {
        if (load(product.getNumber()) == null)
            return false;
        return true;
    }

    private void insertTags(Connection c, Product product) throws SQLException {
        if (product instanceof Picture) {
            Picture picture = (Picture) product;
            List<String> tags = picture.getTags();

            ResultSet rs = queryTags(c, tags);
            //String[] existingTags = new String[rs.getFetchSize()];
            Set<String> existingTags = new HashSet<>();
            while (rs.next()) {
                existingTags.add(rs.getString("name"));
            }

            for (String tag : tags) {
                if (!existingTags.contains(tag))
                    insertTag(c, tag);
            }
            linkOrRemoveTags(c, picture);
        }
    }

    private void insertTag(Connection c, String tag) throws SQLException {
        PreparedStatement ps = c.prepareStatement("INSERT INTO Tags (name) VALUES (?)");
        ps.setString(1, tag);
        ps.executeUpdate();
    }

    private void linkOrRemoveTags(Connection c, Product product) throws SQLException {
        PreparedStatement ps = c.prepareStatement("SELECT id FROM Products WHERE number = ?");
        ps.setString(1, product.getNumber());
        ResultSet rs = ps.executeQuery();
        rs.next();
        int productId = rs.getInt("id");

        rs = queryTags(c, product.getTags());
        Set<Integer> tagIds = new HashSet<>();
        while (rs.next()) {
            tagIds.add(rs.getInt("id"));
        }

        ps = c.prepareStatement("SELECT productId, tagId FROM ProductsTags WHERE productId = ?");
        ps.setInt(1, productId);
        rs = ps.executeQuery();

        Set<Integer> alreadyConnectedTagIds = new HashSet<>();
        while (rs. next()) {
            alreadyConnectedTagIds.add(rs.getInt("tagId"));
        }

        removeNoLongerExistProductsTagConnections(c, productId, tagIds, alreadyConnectedTagIds);
        linkTags(c, productId, tagIds, alreadyConnectedTagIds);
    }

    private void linkTags(Connection c, int productId, Set<Integer> tagIds, Set<Integer> alreadyConnectedTagIds) throws SQLException {
        for (int tagId : tagIds) {
            if (!(ifTagIsConnect(tagId, alreadyConnectedTagIds)))
                linkTag(c, productId, tagId);
        }
    }

    private void removeNoLongerExistProductsTagConnections(Connection c, int productId, Set<Integer> tagIds, Set<Integer> alreadyConnectedTagIds) throws SQLException {
        for (int alConnTagId : alreadyConnectedTagIds) {
            if (ifTagIsRemoved(alConnTagId, tagIds)) {
                removeConnection(c, alConnTagId, productId);
            }
        }
    }

    private void removeConnection(Connection c, int alConnTagId, int prodId) throws SQLException{
        PreparedStatement ps = c.prepareStatement("DELETE FROM ProductsTags WHERE productId = ? AND tagId = ?");
        ps.setInt(1, prodId);
        ps.setInt(2, alConnTagId);
        ps.execute();
    }

    private boolean ifTagIsRemoved(int alConnTagId, Set<Integer> tagIds) {
        for (int tag : tagIds) {
            if (tag == alConnTagId)
                return false;
        }
        return true;
    }

    private boolean ifTagIsConnect(Integer tagId, Set<Integer> alreadyConnectedTagIds) {
        for (int tag : alreadyConnectedTagIds) {
            if (tagId == tag)
                return true;
        }
        return false;
    }

    private void linkTag(Connection c, int productId, Integer tagId) throws SQLException {
        PreparedStatement ps =c.prepareStatement("INSERT INTO ProductsTags (productId, tagId) VALUES (?, ?)");
        ps.setInt(1, productId);
        ps.setInt(2, tagId);
        ps.executeUpdate();
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    @Override
    public void remove(String nr) {

    }

    @Override
    public List<Product> find(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) {
        return null;
    }

    @Override
    public void removeAllProducts() {

    }

    /**
     * It prints appropriate quantity of question marks
     * @param c
     * @param tags
     * @return
     * @throws SQLException
     */
    private ResultSet queryTags(Connection c, List<String> tags) throws SQLException {
        String[] questionMarks = new String[tags.size()];
        for(int i = 0; i <tags.size(); i++)
            questionMarks[i] = "?";
        PreparedStatement ps = c.prepareStatement("SELECT id, name FROM Tags WHERE name IN (" + String.join(",", questionMarks) + ")");
        for (int i = 1; i <= tags.size(); i++)
            ps.setString(i, tags.get(i - 1));
        return ps.executeQuery();
    }
}
