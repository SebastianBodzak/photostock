package pl.com.bottega.photostock.sales.api;

import com.google.common.base.Preconditions;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakePurchaseRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import java.util.List;

/**
 * Created by Dell on 2016-05-09.
 */
public class ClientManagement {

    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private PurchaseRepository purchaseRepository = RepoFactory.createPurchaseRepository();

    public String register(String firstName, String secondName, String address) {
        Client client = ClientFactory.create(firstName, secondName, address);
        clientRepository.save(client);
        return client.getNumber();
    }

    public String register(String firstName, String secondName, String address, String login, String email) { //// TODO: 2016-05-14
        Client client = ClientFactory.create(firstName, secondName, address);
        clientRepository.save(client);
        return client.getNumber();
    }

    public List<Reservation> findReservation(String clientNr) {
        return reservationRepository.find(clientNr);
    }

    public List<Purchase> findPurchases(String clientNr){
        return purchaseRepository.find(clientNr);
    }

    public void recharge(String clientNr, Money value) {
        Client client = clientRepository.load(clientNr);
        client.recharge(value);
        clientRepository.save(client);
    }

    public List<Product> findPurchase(String clientNr) throws IllegalArgumentException {
        Client client = clientRepository.load(clientNr);
        List<Product> productList = purchaseRepository.search(client);
        Preconditions.checkArgument(productList.isEmpty(), "You do not have any purchases");

        return productList;
    }
}
