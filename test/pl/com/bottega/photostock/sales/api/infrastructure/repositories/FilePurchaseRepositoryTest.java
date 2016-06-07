package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.api.AdminPanel;
import pl.com.bottega.photostock.sales.api.ClientManagement;
import pl.com.bottega.photostock.sales.api.PurchaseProcess;
import pl.com.bottega.photostock.sales.infrastructure.repositories.*;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Dell on 2016-05-26.
 */
public class FilePurchaseRepositoryTest {

    private static final String PATH_TEMP_FILE = "tmp/purchases.csv";
    private ReservationRepository reservationRepository = new FakeReservationRepository();
    private LightBoxRepository lightBoxRepository = new FakeLightBoxRepository();
    private ClientRepository clientRepository = new FakeClientRepository();
    private ProductRepository productRepository = new FakeProductRepository();
    private PurchaseRepository purchaseRepository = new FilePurchaseRepository(PATH_TEMP_FILE, clientRepository, productRepository);

    private static final Client STANDARD_CLIENT = ClientFactory.create("owner1", "Janusz", ClientStatus.STANDARD);
    private static final Client VIP_CLIENT = ClientFactory.create("owner2", "Janina", ClientStatus.VIP);
    private static final Product STANDARD_PRODUCT = new Picture("nr1", "title1", new Money(10), true, null);
    private static final Product STANDARD_PRODUCT_2 = new Picture("nr2", "title2", new Money(8), true, null);
    private static final Product NOT_AVAILABLE_PRODUCT = new Picture("nr3", "title3", new Money(20), false, null);

    @Before
    public void shouldAddClientsAndProducts() {
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.save(VIP_CLIENT);
        productRepository.save(STANDARD_PRODUCT);
        productRepository.save(STANDARD_PRODUCT_2);
        productRepository.save(NOT_AVAILABLE_PRODUCT);
    }

    @After
    public void shouldCleanRepositories() {
        clientRepository.remove(STANDARD_CLIENT.getNumber());
        clientRepository.remove(VIP_CLIENT.getNumber());
        productRepository.remove(STANDARD_PRODUCT.getNumber());
        productRepository.remove(STANDARD_PRODUCT_2.getNumber());
        productRepository.remove(NOT_AVAILABLE_PRODUCT.getNumber());
        deletePurchaseFile();
    }

    @Test
    public void shouldLoadClient() {
        Client client = clientRepository.load(STANDARD_CLIENT.getNumber());
    }

    @Test
    public void shouldLoadPurchase() {
        //given
        PurchaseRepository purchaseRepository = new FilePurchaseRepository(getClass().getResource("/fixtures/purchases.csv").getPath(), clientRepository, productRepository);
        //when
        Purchase purchase = purchaseRepository.load("nr2");
        //then
        assertEquals("nr2", purchase.getNumber());
        assertEquals(2, purchase.getItems().size());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        PurchaseRepository purchaseRepository = new FilePurchaseRepository("fake path", clientRepository, productRepository);
        //when
        DataAccessException ex = null;
        try {
            purchaseRepository.load("nr2");
        }
        catch(DataAccessException dae) {
            ex = dae;
        }
        //then
        assertNotNull(ex);
    }

    @Test
    public void shouldWritePurchase() {
        //given
        Reservation reservation = createReservationWithProduct();
        reservationRepository.save(reservation);
        reservation = reservationRepository.load(reservation.getNumber());
        Client client = reservation.getOwner();
        //when
        confirm(client);

        List<Purchase> listOfPurchases = purchaseRepository.find(client.getNumber());
        Purchase writtenPurchase = listOfPurchases.get(0);
        String numberOfProductFromReservation = reservation.getProducts().get(0).getNumber();
        String numberofProductFromPurchase = writtenPurchase.getItems().get(0).getNumber();

        //then
        Assert.assertEquals(numberOfProductFromReservation, numberofProductFromPurchase);
    }

    @Test
    public void shouldAddTwoProducts() {
        //given
        Reservation reservation = createReservation();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when
        reservation.add(product);
        reservation.add(secondProduct);
        reservationRepository.save(reservation);
        Client client = reservation.getOwner();
        confirm(client);

        List<Purchase> listOfPurchases = purchaseRepository.find(client.getNumber());
        Purchase writtenPurchase = listOfPurchases.get(0);

        //then
        Assert.assertEquals(2, reservation.getItemsCount());
        Assert.assertEquals(2, writtenPurchase.getItems().size());
    }

    private Reservation createReservation() {
        Client client = STANDARD_CLIENT;
        client.recharge(new Money(100_000));
        clientRepository.save(client);
        client = clientRepository.load(client.getNumber());
        return new Reservation(client);
    }

    private Reservation createReservationWithProduct() {
        Reservation reservation = createReservation();
        Product product = createProduct();
        reservation.add(product);
        return reservation;
    }

    private void deletePurchaseFile() {
        File file = new File(PATH_TEMP_FILE);
        file.delete();
    }

    private LightBox createLightBox() {
        clientRepository.load(STANDARD_CLIENT.getNumber());
        return new LightBox(STANDARD_CLIENT, "SomeLbx");
    }

    private Product createProduct() {
        Product product = productRepository.load(STANDARD_PRODUCT.getNumber());
        return product;
    }

    private Product createSecondProduct() {
        Product product = productRepository.load(STANDARD_PRODUCT_2.getNumber());
        return product;
    }
    private Product createNotAvailableProduct() {
        Product product = productRepository.load(NOT_AVAILABLE_PRODUCT.getNumber());
        return product;
    }

    private void confirm(Client client) {
        Reservation reservation = reservationRepository.findOpenerPer(client);
        Offer offer = reservation.generateOffer();
        Money totalSum = offer.getTotalCost();
        if (client.canAfford(totalSum)) {
            client.charge(totalSum, "za coś");
            Purchase purchase = new Purchase(client, offer.getItems());
            purchaseRepository.save(purchase);
            reservation.getClose();
        }
        reservationRepository.save(reservation);
    }
}
