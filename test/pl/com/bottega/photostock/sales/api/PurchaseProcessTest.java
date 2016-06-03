package pl.com.bottega.photostock.sales.api;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import java.util.List;

/**
 * Created by Dell on 2016-04-24.
 */
public class PurchaseProcessTest {
//    private static final String STANDARD_USER_NR = "nr1";
//    private static final String AVAILABLE_PRODUCT_NR = "nr1";

    private ProductRepository productRepository = new FakeProductRepository();
    private ClientRepository clientRepository = new FakeClientRepository();
    private ReservationRepository reservationRepository = new FakeReservationRepository();

 /*   @Test
    public void shouldAddAvailableProduct() {
        //given
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        PurchaseProcess.add(STANDARD_USER_NR, AVAILABLE_PRODUCT_NR);

        Reservation reservation = makeReservationandAddProduct();
        Assert.assertEquals(reservation.getItemsCount(), 1);
        reservationRepository.remove(reservation.getNumber());

    }

    @Test
    public void shouldLoadReservationForStandardClient() {
        Reservation reservation = makeReservationandAddProduct();
        Assert.assertNotEquals(reservation.getNumber(), "");
    }*/

//    @Test
//    public void canNotAddAlreadyAddedProduct() {
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        Product product = createFirstAvailableProduct();
//        Client client = createDefaultClient();
//        try {
//            purchaseProcess.add(client.getNumber(), product.getNumber());
//            purchaseProcess.add(client.getNumber(), product.getNumber());
//            Assert.fail();
//        }
//        catch (IllegalArgumentException ex) {
//
//        }
//    }
//
//    @Test
//    public void shouldPromoteClientToVipAutomatically() {
//        String reservationNr = makeReservationWithOneProductWithRichClient("Janusz", "Er", "Kałuża", AbstractProduct.KindOfProduct.PICTURE, "GoldProduct", new Money(1000));
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        purchaseProcess.confirm(reservationNr);
//
//        Reservation reservation = reservationRepository.load(reservationNr);
//        Client client = reservation.getOwner();
//
//        Assert.assertEquals(client.getStatus(), ClientStatus.VIP);
//    }
//
//    @Test
//    public void shouldCalculateOffer() {
//        String reservationNr = makeReservationWithOneProductWithRichClient("Janusz", "Er", "Kałuża", AbstractProduct.KindOfProduct.PICTURE, "GoldProduct", new Money(1000));
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//
//        Offer offer = purchaseProcess.calculateOffer(reservationNr);
//
//        Assert.assertEquals(offer.getTotalCost(), new Money(1000));
//    }
//
//    @Test
//    public void shouldCalculateOfferWithTwoProducts() {
//        String reservationNr = makeReservationWithOneProductWithRichClient("Janusz", "Er", "Kałuża", AbstractProduct.KindOfProduct.PICTURE, "GoldProduct", new Money(1000));
//        Product secondProduct = createAvailableProduct(AbstractProduct.KindOfProduct.PICTURE, "SilverProduct", new Money(555.75));
//
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        Reservation reservation = reservationRepository.load(reservationNr);
//        Client client = reservation.getOwner();
//        purchaseProcess.add(client.getNumber(), secondProduct.getNumber());
//
//        Offer offer = purchaseProcess.calculateOffer(reservationNr);
//
//        Assert.assertEquals(new Money(1555.75), offer.getTotalCost());
//    }
//
//    @Test
//    public void shouldConfirmAndFindPurchase() {
//        String reservationNr = makeReservationWithOneProductWithRichClient("Janusz", "Er", "Kałuża", AbstractProduct.KindOfProduct.PICTURE, "GoldProduct", new Money(1000));
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        ClientManagement clientManagement = new ClientManagement();
//
//        Reservation reservation = reservationRepository.load(reservationNr);
//        Client client = reservation.getOwner();
//
//        purchaseProcess.confirm(reservationNr);
//        try {
//            Reservation reservation1 = reservationRepository.load(client);
//            Assert.fail();
//        }
//        catch (DataDoesNotExistsException ex) {
//            List<Product> productList = clientManagement.findPurchase(client.getNumber());
//            Assert.assertTrue(productList.size() == 1);
//        }
//    }
//
//    @Test
//    public void shouldCloseReservation() {
//        String reservationNr = makeReservationWithOneProductWithRichClient("Janusz", "Er", "Kałuża", AbstractProduct.KindOfProduct.PICTURE, "GoldProduct", new Money(1000));
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//
//        Reservation reservation = reservationRepository.load(reservationNr);
//        Client client = reservation.getOwner();
//
//        purchaseProcess.closeReservation(reservationNr);
//        try {
//            Reservation reservation1 = reservationRepository.load(client);
//            Assert.fail();
//        }
//        catch (DataDoesNotExistsException ex) {
//
//        }
//    }
//
//    @Test
//    public void shouldChangeCreditLimit() {
//        Client client = createClient("Elżbieta", "Batory", "Slovakia");
//        AdminPanel adminPanel = new AdminPanel();
//        adminPanel.promoteClientToVip(client.getNumber());
//        adminPanel.changeCreditLimit(client.getNumber(), new Money(300));
//        Assert.assertEquals(new Money(300), client.getCreditLimit());
//    }
//
//    @Test
//    public void shouldntChangeCreditLimit() {
//        Client client = createClient("Elżbieta", "Batory", null);
//        AdminPanel adminPanel = new AdminPanel();
//        try {
//            adminPanel.changeCreditLimit(client.getNumber(), new Money(300));
//            Assert.fail();
//        }
//        catch (IllegalStateException ex) {
//
//        }
//    }
//
//    private Reservation makeReservationandAddProduct() { //todo find
//        ClientManagement clientManagement = new ClientManagement();
//        String clientNr = clientManagement.register("Janusz", "Kowalski", "Zagrzeb");
//        PurchaseProcess purchaseProcess = new PurchaseProcess();
//        AdminPanel adminPanel = new AdminPanel();
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        //dodajemy usera i produkty
//        //when
//        Client client = clientRepository.load(clientNr);
//        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "Tree", new Money(8), null);
//        List<Product> productList = productsCatalog.find("tree", null, null, null);
//        Product product = productList.get(0);
//
//        purchaseProcess.add(clientNr, product.getNumber());
//        Reservation reservation = reservationRepository.findOpenerPer(client);
//        //then
//        return reservation;
//    }



    private Client createDefaultClient() {
        Client nr1 = ClientFactory.create("Janusz", "Księżyc", ClientStatus.STANDARD, "Very Big Company");
        clientRepository.save(nr1);
        return clientRepository.load(nr1.getNumber());
    }

    private Client createClient(String firstName, String secondName, String address) {
        ClientManagement clientManagement = new ClientManagement();
        String clientNr = clientManagement.register(firstName, secondName, address);
        Client client = clientRepository.load(clientNr);
        clientRepository.save(client);
        return clientRepository.load(clientNr);
    }

/*    private Product createFirstAvailableProduct() {
        ClientManagement clientManagement = new ClientManagement();
        String clientNr = clientManagement.register("Janusz", "Kowalski", "Zagrzeb");
        AdminPanel adminPanel = new AdminPanel();
        ProductsCatalog productsCatalog = new ProductsCatalog();
        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "Doordadawwaddwadwawdawdadwadwa", new Money(5), null);
        List<Product> productList = productsCatalog.find("Doordadawwaddwadwawdawdadwadwa", null, null, null);
        Product product = productList.get(0);
        return product;
    }

    private Product createAvailableProduct(AbstractProduct.KindOfProduct kindOfProduct, String name, Money price) {
        ClientManagement clientManagement = new ClientManagement();
        String clientNr = clientManagement.register("Janusz", "Kowalski", "Zagrzeb");
        AdminPanel adminPanel = new AdminPanel();
        adminPanel.addProduct(kindOfProduct, name, price, null);
        ProductsCatalog productsCatalog = new ProductsCatalog();
        List<Product> productList = productsCatalog.find(name, null, null, null);
        Product product = productList.get(0);
        return product;
    }

    private String makeReservationWithOneProductWithRichClient(String clientFirstName, String clientSecondName, String clientAdress, AbstractProduct.KindOfProduct kindOfProduct, String productName, Money price) {
        ClientManagement clientManagement = new ClientManagement();
        String clientNr = clientManagement.register(clientFirstName, clientSecondName, clientAdress);

        clientRepository.load(clientNr).recharge(new Money(50000));

        AdminPanel adminPanel = new AdminPanel();
        adminPanel.addProduct(kindOfProduct, productName, price, null);

        ProductsCatalog productsCatalog = new ProductsCatalog();
        List<Product> productList = productsCatalog.find(productName, null, null, null);

        PurchaseProcess purchaseProcess = new PurchaseProcess();
        purchaseProcess.add(clientNr, productList.get(0).getNumber());

//        String reservationNr = clientManagement.findReservation(clientNr);
        List<Reservation> list = clientManagement.findReservation(clientNr);
        Reservation reservation = list.get(0);

        return reservation.getNumber();
    }*/

    private void removeProductsFromRepository(Product ...products) {
        for (Product prod : products)
            productRepository.remove(prod.getNumber());
    }

    private void removeClientsFromRepository(Client ...clients) {
        for (Client client : clients)
            productRepository.remove(client.getNumber());
    }
}
