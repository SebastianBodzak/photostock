package pl.com.bottega.photostock.sales.api;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.model.*;

import java.io.File;
import java.util.List;

/**
 * Created by Dell on 2016-05-14.
 */
public class AcceptanceStory {

    private static final Money INITIAL_MONEY = new Money(50);

    private static final Money PRICE_1 = new Money(10);
    private static final Money PRICE_2 = new Money(20);
    private static final Money PRICE_3 = new Money(30);

    private static final Money TOTAL_COST = PRICE_1.add(PRICE_2).add(PRICE_3).add(PRICE_3);

    //tyle udzielimy limity kredytu aby wysatrczyło na różnicą mędzy kosztem a tym co ma klient
    private static final Money CREDIT_LIMIT = TOTAL_COST.subtract(INITIAL_MONEY);

    private ProductsCatalog productsCatalog = new ProductsCatalog();
    private PurchaseProcess purchaseProcess = new PurchaseProcess();
    private ClientManagement clientManagement = new ClientManagement();
    private LightBoxManagement lightBoxManagement = new LightBoxManagement();
    private AdminPanel adminPanel = new AdminPanel();

    private ProductRepository productRepository = RepoFactory.createProductRepository();
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private PurchaseRepository purchaseRepository = RepoFactory.createPurchaseRepository();

    private static final String PATH_TEMP_PURCHASES_FILE = "tmp/purchases.csv";
    private static final String PATH_TEMP_LIGHTBOXES_FILE = "tmp/lightboxes.csv";
    private static final String PATH_TEMP_CLIENT_FILE = "tmp/clients.csv";
    private static final String PATH_TEMP_PRODUCT_FILE= "tmp/products.csv";
    private static final String PATH_TEMP_RESERVATION_FILE= "tmp/reservations.csv";

    @Test
    public void story(){
        deleteAllFiles();
        //admin dodaje produkty do oferty
        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "mustang", PRICE_1, "ford", "mustang");
        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "bmw",PRICE_2, "bmw", "m6");
        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "multipla", PRICE_3, "fiat", "multipla");
        //ten produkt stanie sie pozniej niedostepny
        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "lamborghini", PRICE_3, "lamborghini", "huracan");

//        Client client = STANDARD_CLIENT;
//        client.recharge(new Money(10000000));
//        clientRepository.save(client);
//        clientRepository.load(client.getNumber());

        //użytkownik się rejestruje i doładowuje konto
        String clientNr = clientManagement.register("nazwa 1", "nazwa 2", "addresss");
        clientManagement.recharge(clientNr, INITIAL_MONEY);

        //admin awansuje klienta i daje mi limit kredytu
        adminPanel.promoteClientToVip(clientNr);
        adminPanel.changeCreditLimit(clientNr, CREDIT_LIMIT);

        //użytkownik przeszukuje katalog dostępnych produktów
        List<Product> products = productsCatalog.find(null, null, null, null, true);

        //użytkownik dodaje pierwszy produkt do lbx
        String lightBoxNr = lightBoxManagement.create(clientNr, "lightbox 1");
        lightBoxManagement.add(lightBoxNr, products.get(0).getNumber());

        //użytkonik dodaje drugi i trzeci produkt do rezerwacji
        //oraz czwarty, ktory stanie sie niedostepny
        purchaseProcess.add(clientNr, products.get(1).getNumber());
        purchaseProcess.add(clientNr, products.get(2).getNumber());
        //jego niedługo usuniemy z oferty
        purchaseProcess.add(clientNr, products.get(3).getNumber());


        //użytkownik dodaje do rezerwacji jeszcze produkty z lightboxa
        lightBoxManagement.addAllToReservation(lightBoxNr, clientNr);

        //admin usuwa czwarty produkt
        adminPanel.changeAvailability(products.get(3).getNumber(), false);

        //użytkownik przegląda ofertę - w rezerwacji ma 4, ale w ofercie znajdą się 3 elementy
        //gdyż jeden wlasnie usunieto
        Offer offer = purchaseProcess.calculateOffer(clientNr);
        Assert.assertEquals(TOTAL_COST.subtract(products.get(3).calculatePrice()), offer.getTotalCost());

        //użytkownik zatwierdza ofertę
        purchaseProcess.confirm(clientNr);

        //użytkownik przegląda swoje zakupy
        List<Purchase> purchases = clientManagement.findPurchases(clientNr);
        Assert.assertEquals(1, purchases.size());

        //clear database (need rebuild)
    }

//    @Test
//    public void story2() {
//        //adminPanel.add(Product);
//        //adminPanel.add(Product);
//        //adminPanel.add(Product);
//        adminPanel.addProduct(AbstractProduct.KindOfProduct.PICTURE, "er", new Money(1), null);
//
//        String clientNr = clientManagement.register("Janusz", "Kowalski", "Address");
//        clientManagement.recharge(clientNr, INITIAL_MONEY);
//        List<Product> products = productsCatalog.find(null, null, null, null, true);
//
//
//        for (Product product : products)
//            purchaseProcess.add(clientNr, product.getNumber());
//
//            //dodanie do LBX i przeniesienie do rezerwacji
//
//        purchaseProcess.calculateOffer(clientNr);
//        purchaseProcess.confirm(clientNr);
//    }

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
