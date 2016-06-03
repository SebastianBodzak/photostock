package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientRepository;
import pl.com.bottega.photostock.sales.model.ClientStatus;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.sql.*;
import java.util.UUID;

/**
 * Created by Dell on 2016-06-03.
 */
public class JDBCClientRepository implements ClientRepository {


    private final String url;
    private final String login;
    private final String password;

    public JDBCClientRepository(String url, String login, String password) {
        this.url = url;
        this.login = login;
        this.password = password;
    }

    @Override
    public Client load(String number) throws DataAccessException {
        try (Connection c = getConnection()) {
            PreparedStatement preparedStatement = c.prepareStatement("SELECT number, name, address, amountCents, debtCents, creditLimitCents, amountCurrency, active, statusId " +
                    "FROM Clients WHERE number = ?");
            preparedStatement.setString(1, number);
            ResultSet rs = preparedStatement.executeQuery();
            if (!rs.next())
                return null;
            Money.CurrencyValues currency = Money.CurrencyValues.valueOf(rs.getString("amountCurrency"));
            ClientStatus clientsStatus = parseClientStatus(rs.getInt("statusId"));
            return new Client(rs.getString("number"), rs.getString("name"), rs.getString("address"), clientsStatus, new Money(rs.getInt("debtCents")/100, currency), new Money(rs.getInt("amountCents")/100, currency),
                    new Money(rs.getInt("creditLimitCents")/100, currency), rs.getBoolean("active"));

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, login, password);
    }

    private ClientStatus parseClientStatus(int statusId) throws IllegalArgumentException {
        switch (statusId) {
            case 0:
                return ClientStatus.STANDARD;
            case 1:
                return ClientStatus.VIP;
            case 2:
                return ClientStatus.SILVER;
            case 3:
                return ClientStatus.GOLD;
            case 4:
                return ClientStatus.PLATINUM;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void save(Client client) throws DataAccessException {
        try (Connection c = getConnection()) {

            boolean ifClientExists = checkIfClientExists(client);
            String query = ifClientExists ?
                    "UPDATE Clients SET number = ?, name = ?, address = ?, amountCents = ?, debtCents = ?, creditLimitCents = ?, amountCurrency = ?, active = ?, statusId = ? WHERE number = ?"
                    : "INSERT INTO Clients (number, name, address, amountCents, debtCents, creditLimitCents, amountCurrency, active, statusId) VALUES (?,?,?,?,?,?,?,?,?);";

            PreparedStatement preparedStatement = c.prepareStatement(query);

            if (client.getNumber() == null)
                client.setNumber(UUID.randomUUID().toString());
            preparedStatement.setString(1, client.getNumber());
            preparedStatement.setString(2, client.getName());
            preparedStatement.setString(3, client.getAddress());
            preparedStatement.setInt(4, client.getAmount().cents());
            preparedStatement.setInt(5, client.getDebt().cents());
            preparedStatement.setInt(6, client.getCreditLimit().cents());
            String currency = String.valueOf(client.getAmount().getCurrency());
            preparedStatement.setString(7, currency);
            preparedStatement.setBoolean(8, client.isActive());
            preparedStatement.setInt(9, checkIdOfStatus(client));
            if (ifClientExists)
                preparedStatement.setString(10, client.getNumber());
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }

    }

    private boolean checkIfClientExists(Client client) {
        if (load(client.getNumber()) == null)
            return false;
        else
            return true;
    }

    private int checkIdOfStatus(Client client) throws IllegalArgumentException {
        switch (String.valueOf(client.getStatus()).toLowerCase()) {
            case "standard":
                return 0;
            case "vip":
                return 1;
            case "silver":
                return 2;
            case "gold":
                return 3;
            case "platinum":
                return 4;
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public void remove(String nr) {

    }
}
