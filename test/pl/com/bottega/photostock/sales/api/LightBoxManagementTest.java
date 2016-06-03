package pl.com.bottega.photostock.sales.api;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeLightBoxRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static pl.com.bottega.photostock.sales.model.ClientRole.*;

/**
 * Created by Dell on 2016-04-28.
 */
public class LightBoxManagementTest {

//    private static final String STANDARD_USER_NR =  "nr1";
//    private static final String STANDARD_SECOND_USER_NR =  "nr2";
//    private static final String STANDARD_THIRD_USER_NR =  "nr3";
//    private static final String AVAILABLE_PRODUCT_NR = "nr1";
//    private static final String AVAILABLE_SECOND_PRODUCT_NR = "nr2";
//    private static final String AVAILABLE_THIRD_PRODUCT_NR = "nr3";

    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();

    private static final String PATH_TEMP_PURCHASES_FILE = "tmp/purchases.csv";
    private static final String PATH_TEMP_LIGHTBOXES_FILE = "tmp/lightboxes.csv";
    private static final String PATH_TEMP_CLIENT_FILE = "tmp/clients.csv";
    private static final String PATH_TEMP_PRODUCT_FILE= "tmp/products.csv";
    private static final String PATH_TEMP_RESERVATION_FILE= "tmp/reservations.csv";

    @Test
    public void shouldCreateEmptyLightBox() {
        //given
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        //when
        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        //then
        Assert.assertNotEquals(lightBoxNr, "");

        clientRepository.remove(client.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldDeleteLightBox() { //todo sth wrong
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        lightBoxManagement.deleteLightBox(lightBoxNr);
        lightBoxRepository.load(lightBoxNr);

        clientRepository.remove(client.getNumber());
    }

    @Test
    public void shouldAddAvailableProduct() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Product product = makeFirstAvailableProduct();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");

        lightBoxManagement.add(lightBoxNr, product.getNumber());

        clientRepository.remove(client.getNumber());
        productRepository.remove(product.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldRemoveAvailableProduct() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Product product = makeFirstAvailableProduct();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");

        lightBoxManagement.add(lightBoxNr, product.getNumber());
        lightBoxManagement.remove(lightBoxNr, product.getNumber());
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        assert lightBox.getProducts().isEmpty();

        clientRepository.remove(client.getNumber());
        productRepository.remove(product.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void cannotAddAlreadyAddedProduct() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Product product = makeFirstAvailableProduct();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        lightBoxManagement.add(lightBoxNr, product.getNumber());

        try {
            lightBoxManagement.add(lightBoxNr, product.getNumber());
            Assert.fail();
        }
        catch (IllegalArgumentException ex) {
//            excepted
        }

        clientRepository.remove(client.getNumber());
        productRepository.remove(product.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldShareLightBox() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Client client2 = makeSecondClient();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");

        lightBoxManagement.share(lightBoxNr, client.getNumber(), client2.getNumber(), ADMIN);

        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        Set<Permission> owners = lightBox.getOwners();

        for (Permission per : owners) {
            if (per.getClient().getName() == clientRepository.load(client2.getNumber()).getName()) {
                assert per.getClientRole() == ClientRole.ADMIN;
            }
        }

        clientRepository.remove(client.getNumber());
        clientRepository.remove(client2.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldntShareLightBoxBecauseUserWorksInDifferentCompany() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Client differentCompanyClient = makeThirdClient();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");

        try {
            lightBoxManagement.share(lightBoxNr, client.getNumber(), differentCompanyClient.getNumber(), ADMIN);
            Assert.fail();
        }
        catch (IllegalStateException ex) {

        }

        clientRepository.remove(client.getNumber());
        clientRepository.remove(differentCompanyClient.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldntShareLightBoxBecauseUserIsNotAdmin() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Client client2 = makeSecondClient();
        Client client3 = makeThirdClient();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        lightBoxManagement.share(lightBoxNr, client.getNumber(), client2.getNumber(), EDITOR);

        try {
            lightBoxManagement.share(lightBoxNr, client2.getNumber(), client3.getNumber(), ADMIN);
            Assert.fail();
        }
        catch (IllegalStateException ex) {

        }

        clientRepository.remove(client.getNumber());
        clientRepository.remove(client2.getNumber());
        clientRepository.remove(client3.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldntShareAlreadyAddedUser() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Client client2 = makeSecondClient();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        lightBoxManagement.share(lightBoxNr, client.getNumber(), client2.getNumber(), ADMIN);

        try {
            lightBoxManagement.share(lightBoxNr, client2.getNumber(), client.getNumber(), ADMIN);
            Assert.fail();
        }
        catch (IllegalStateException ex) {

        }

        clientRepository.remove(client.getNumber());
        clientRepository.remove(client2.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

//    @Test
//    public void shouldMoveProductToReservation() {//// TODO: 2016-05-14  
//        Client client = makeFirstClient();
//        Product product = makeFirstAvailableProduct();
//
//        LightBoxManagement lightBoxManagement = new LightBoxManagement();
//        String lightBoxNr = lightBoxManagement.create(client.getNumber());
//        lightBoxManagement.add(lightBoxNr, product.getNumber());
//
//        ClientManagement clientManagement = new ClientManagement();
////        String reservationNr = clientManagement.findReservation(client.getNumber());
//        List<Reservation> list = clientManagement.findReservation(client.getNumber());
//        String reservationNr = list.get(0).getNumber();
//        lightBoxManagement.reserve(lightBoxNr, reservationNr, product.getNumber());
//
//        Reservation reservation = reservationRepository.load(reservationNr);
//        assert reservation.getItemsCount() == 1;
//
//        clientRepository.remove(client.getNumber());
//        productRepository.remove(product.getNumber());
//        lightBoxRepository.remove(lightBoxNr);
//        reservationRepository.remove(reservationNr);
//    }

//    @Test
//    public void shouldRemoveProductFromLightBoxAfterReservation() { //// TODO: 2016-05-14  
//        LightBoxManagement lightBoxManagement = new LightBoxManagement();
//        Client client = makeFirstClient();
//        Product product = makeFirstAvailableProduct();
//
//        String lightBoxNr = lightBoxManagement.create(client.getNumber());
//        lightBoxManagement.add(lightBoxNr, product.getNumber());
//
//        ClientManagement clientManagement = new ClientManagement();
////        String reservationNr = clientManagement.findReservation(client.getNumber());
//        List<Reservation> list = clientManagement.findReservation(client.getNumber());
//        String reservationNr = list.get(0).getNumber();
//        lightBoxManagement.reserve(lightBoxNr, reservationNr, product.getNumber());
//
//        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
//        assert lightBox.getProducts().isEmpty();
//
//        clientRepository.remove(client.getNumber());
//        productRepository.remove(product.getNumber());
//        lightBoxRepository.remove(lightBoxNr);
//        reservationRepository.remove(reservationNr);
//    }

    @Test
    public void shouldCloneLightBox() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Product product = makeFirstAvailableProduct();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        lightBoxManagement.add(lightBoxNr, product.getNumber());
        String lightBoxCloneNr = lightBoxManagement.clone(lightBoxNr, client.getNumber());
        LightBox lightBoxClone = lightBoxRepository.load(lightBoxCloneNr);

        assert lightBoxClone.getProducts().size() == 1;

        clientRepository.remove(client.getNumber());
        productRepository.remove(product.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    @Test
    public void shouldChangeProductInCloneInstance() {
        deleteAllFiles();
        LightBoxManagement lightBoxManagement = new LightBoxManagement();
        Client client = makeFirstClient();
        Product product = makeFirstAvailableProduct();
        Product product2 = makeSecondAvailableProduct();
        Product product3 = makeThirdAvailableProduct();

        String lightBoxNr = lightBoxManagement.create(client.getNumber(), "name");
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        lightBoxManagement.add(lightBoxNr, product.getNumber());
        lightBox = lightBoxRepository.load(lightBoxNr);

        String lightBoxCloneNr = lightBoxManagement.clone(lightBoxNr, client.getNumber());
        lightBoxManagement.add(lightBoxCloneNr, product2.getNumber());
        lightBoxManagement.add(lightBoxCloneNr, product3.getNumber());
        LightBox lightBoxClone = lightBoxRepository.load(lightBoxCloneNr);

        Assert.assertEquals(3, lightBoxClone.getProducts().size());
        Assert.assertEquals(1, lightBox.getProducts().size());

        clientRepository.remove(client.getNumber());
        productRepository.remove(product.getNumber());
        productRepository.remove(product2.getNumber());
        productRepository.remove(product3.getNumber());
        lightBoxRepository.remove(lightBoxNr);
    }

    private Product makeFirstAvailableProduct() {
        Product nr1 = new Picture("nr1", new Money(1), Arrays.asList("tree", "green"));
        productRepository.save(nr1);
        return productRepository.load(nr1.getNumber());
    }

    private Product makeSecondAvailableProduct() {
        Product cellar = new Picture("nr2", new Money(3), Arrays.asList("cellar", "basement", "dark"));
        productRepository.save(cellar);
        return productRepository.load(cellar.getNumber());
    }

    private Product makeThirdAvailableProduct() {
        Product door = new Picture("nr3", new Money(8), Arrays.asList("door", "dark"));
        productRepository.save(door);
        return productRepository.load(door.getNumber());
    }

    private Client makeFirstClient() {
        Client nr1 = ClientFactory.create("Janusz", "Księżyc", ClientStatus.STANDARD, "Very Big Company");
        clientRepository.save(nr1);
        return clientRepository.load(nr1.getNumber());
    }

    private Client makeSecondClient() {
        Client nr2 = ClientFactory.create("Korowiow", "Hell", ClientStatus.STANDARD, "Very Big Company");
        clientRepository.save(nr2);
        return clientRepository.load(nr2.getNumber());
    }

    private Client makeThirdClient() {
        Client nr3 = ClientFactory.create("Java", "Very Deep Hell", ClientStatus.STANDARD, "Small And Totally Different Company");
        clientRepository.save(nr3);
        return clientRepository.load(nr3.getNumber());
    }

    private void removeClientsFromRepository(Client ...clients) {
        for (Client client : clients)
            productRepository.remove(client.getNumber());
    }

    private void removeProductsFromRepository(Product ...products) {
        for (Product prod : products)
            productRepository.remove(prod.getNumber());
    }

    private void deleteAllFiles() {
        deletePurchaseFile();
        deleteClientFile();
        deleteReservationFile();
        deleteLightBoxesFile();
        deleteProductsFile();
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
}
