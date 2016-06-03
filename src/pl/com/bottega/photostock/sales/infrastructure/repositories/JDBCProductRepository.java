package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.sql.*;
import java.util.List;

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
            return new Picture(rs.getString("number"), rs.getString("name"), new Money((rs.getInt("priceCents") / 100), currency), rs.getBoolean("available"), null);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void save(Product product) throws DataAccessException {
        try (Connection c = getConnection()) {
            PreparedStatement preparedStatement = c.prepareStatement("INSERT INTO Products (number, name, available, priceCents, priceCurrency, type) VALUES (?, ?, ?, ?, ?, '');");
            preparedStatement.setString(1, product.getNumber());
            preparedStatement.setString(2, product.getTitle());
            preparedStatement.setBoolean(3, product.isAvailable());
            preparedStatement.setInt(4, product.getPrice().cents());
            String currency = String.valueOf(product.getPrice().getCurrency());
            preparedStatement.setString(5, currency);
            preparedStatement.execute();
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
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
}
