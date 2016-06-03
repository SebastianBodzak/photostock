package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.api.AdminPanel;
import pl.com.bottega.photostock.sales.api.ClientManagement;
import pl.com.bottega.photostock.sales.api.PurchaseProcess;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FilePurchaseRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Dell on 2016-05-26.
 */
public class FilePurchaseRepositoryTest {
    private static final Client STANDARD_CLIENT = ClientFactory.create("Janusz", "Poland");
    private static final Client VIP_CLIENT = ClientFactory.create("Cleopatra", "Egypt", ClientStatus.VIP, "Rule z.o.o.");
    private static final String PATH_TEMP_PURCHASES_FILE = "tmp/purchases.csv";
    private static final String PATH_TEMP_LIGHTBOXES_FILE = "tmp/lightboxes.csv";
    private static final String PATH_TEMP_CLIENT_FILE = "tmp/clients.csv";
    private static final String PATH_TEMP_PRODUCT_FILE= "tmp/products.csv";
    private static final String PATH_TEMP_RESERVATION_FILE= "tmp/reservations.csv";
//    private static final LightBox AVAILABLE_LIGHTBOX = new LightBox(STANDARD_CLIENT, "SomeLbx");

    private PurchaseRepository purchaseRepository = RepoFactory.createPurchaseRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();

    private PurchaseProcess purchaseProcess = new PurchaseProcess();
    private ClientManagement clientManagement = new ClientManagement();
    private AdminPanel adminPanel = new AdminPanel();

    @Test
    public void shouldLoadPurchase() {
        //given
        PurchaseRepository purchaseRepository = new FilePurchaseRepository(getClass().getResource("/fixtures/purchases.csv").getPath());
        //when
        Purchase purchase = purchaseRepository.load("nr2");
        //then
        assertEquals("nr2", purchase.getNumber());
        assertEquals(2, purchase.getItems().size());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        PurchaseRepository purchaseRepository = new FilePurchaseRepository("fake path");
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
        deletePurchaseFile();
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservationWithProduct();
        reservationRepository.save(reservation);
        String clientNr = reservation.getOwner().getNumber();
        //when
        purchaseProcess.confirm(clientNr);

        List<Purchase> listOfPurchases = clientManagement.findPurchases(clientNr);
        Purchase writtenPurchase = listOfPurchases.get(0);
        String numberOfProductFromReservation = reservation.getProducts().get(0).getNumber();
        String numberofProductFromPurchase = writtenPurchase.getItems().get(0).getNumber();

        //then
        Assert.assertEquals(numberOfProductFromReservation, numberofProductFromPurchase);
    }

    @Test
    public void shouldAddTwoProducts() {
        //given
        deletePurchaseFile();
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservation();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when
        reservation.add(product);
        reservation.add(secondProduct);
        reservationRepository.save(reservation);
        String clientNr = reservation.getOwner().getNumber();
        purchaseProcess.confirm(clientNr);

        List<Purchase> listOfPurchases = clientManagement.findPurchases(clientNr);
        Purchase writtenPurchase = listOfPurchases.get(0);

        //then
        Assert.assertEquals(2, reservation.getItemsCount());
        Assert.assertEquals(2, writtenPurchase.getItems().size());
    }

    private Reservation createReservation() {
        Client client = STANDARD_CLIENT;
        client.recharge(new Money(10000000));
        clientRepository.save(client);
        clientRepository.load(client.getNumber());
        Reservation reservation = new Reservation(client);

        return reservation;
    }

    private Reservation createReservationWithProduct() {
        Reservation reservation = createReservation();
        Product product = createProduct();
        reservation.add(product);
        return reservation;
    }

    private void deletePurchaseFile() {
        File file = new File(PATH_TEMP_PURCHASES_FILE);
        file.delete();
    }

    private void deleteLightBoxesFile() {
        File file = new File(PATH_TEMP_LIGHTBOXES_FILE);
        file.delete();
    }

    private void deleteClientFile() {
        File file = new File(PATH_TEMP_CLIENT_FILE);
        file.delete();
    }

    private void deleteProductsFile() {
        File file = new File(PATH_TEMP_PRODUCT_FILE);
        file.delete();
    }

    private void deleteReservationFile() {
        File file = new File(PATH_TEMP_RESERVATION_FILE);
        file.delete();
    }

    private LightBox createLightBox() {
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.load(STANDARD_CLIENT.getNumber());
        LightBox lightBox = new LightBox(STANDARD_CLIENT, "SomeLbx");

        return lightBox;
    }

    private Product createProduct() {
        Product product = new Clip("nr1", new Money(100.3), Arrays.asList("tree", "forest"));
        productRepository.save(product);
        productRepository.load(product.getNumber());
        return product;
    }

    private Product createSecondProduct() {
        Product product = new Picture("nr2", new Money(20.5), true, Arrays.asList("house", "flat"));
        productRepository.save(product);
        productRepository.load(product.getNumber());
        return product;
    }
    private Product createNotAvailableProduct() {
        Product product = new Picture("nr10", new Money(20.5), false, Arrays.asList("java", "monster"));
        productRepository.save(product);
        productRepository.load(product.getNumber());
        return product;
    }

    private void addProductToLightBox(String lightBoxNr) {

    }
}
