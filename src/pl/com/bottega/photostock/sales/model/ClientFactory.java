package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientStatus;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.client.strategies.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dell on 2016-04-20.
 */
public class ClientFactory {

    private static ClientRepository clientRepository = RepoFactory.createClientRepository();


    public static Client create(String firstName, String secondName, String address) {
        String name = firstName + " " + secondName;
        Client client = create(name, address, ClientStatus.STANDARD, null);
        return client;
    }

    public static Client create(String name, String address) {
        Client client = create(name, address, ClientStatus.STANDARD, null);
        return client;
    }

    public static Client create(String number, String name, ClientStatus clientStatus) {
        Client client = create(number, name, "", clientStatus, null);
        return client;
    }

    public static Client create(String name, String address, ClientStatus clientStatus, String companyName) {
        PayingStrategy payingStrategy;

        Money amount = new Money(0);
        Money debt = new Money(0);
        Money creditLimit = new Money(0);
        Company company = new Company(companyName);

        switch (clientStatus) {
            case VIP:
                payingStrategy = new DebtorPayingStrategy();
                creditLimit = new Money(200);
                break;
            case STANDARD:
                payingStrategy = new StandardPayingStrategy();
                break;
            case GOLD:
                payingStrategy = new GoldPayingStrategy();
                break;
            case SILVER:
                payingStrategy = new SilverPayingStrategy();
                break;
            case PLATINUM:
                payingStrategy = new PlatinumPayingStrategyy();
                break;
            default:
                throw new IllegalArgumentException(clientStatus + " is not supported!");
        }

        Client client = new Client(name, address, clientStatus, debt, amount, creditLimit, company);
        client.setPayingStrategy(payingStrategy);

        return client;
    }

    public static Client create(String number, String name, String address, ClientStatus clientStatus, String companyName) {
        PayingStrategy payingStrategy;

        Money amount = new Money(0);
        Money debt = new Money(0);
        Money creditLimit = new Money(0);
        Company company = new Company(companyName);

        switch (clientStatus) {
            case VIP:
                payingStrategy = new DebtorPayingStrategy();
                creditLimit = new Money(200);
                break;
            case STANDARD:
                payingStrategy = new StandardPayingStrategy();
                break;
            case GOLD:
                payingStrategy = new GoldPayingStrategy();
                break;
            case SILVER:
                payingStrategy = new SilverPayingStrategy();
                break;
            case PLATINUM:
                payingStrategy = new PlatinumPayingStrategyy();
                break;
            default:
                throw new IllegalArgumentException(clientStatus + " is not supported!");
        }

        Client client = new Client(number, name, address, clientStatus, debt, amount, creditLimit, company, payingStrategy);
//        client.setPayingStrategy(payingStrategy);

        return client;
    }

    public static void promoteToVip(String clientNr) {
        Client client = clientRepository.load(clientNr);
        client.setStatus(ClientStatus.VIP);
        client.setPayingStrategy(new DebtorPayingStrategy());
        clientRepository.save(client);
    }

}