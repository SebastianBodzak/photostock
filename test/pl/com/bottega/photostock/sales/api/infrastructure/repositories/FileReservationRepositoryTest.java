package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FileReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Created by Dell on 2016-05-26.
 */
public class FileReservationRepositoryTest {
    private static final Client STANDARD_CLIENT = ClientFactory.create("Janusz", "Poland");
    private static final Client VIP_CLIENT = ClientFactory.create("Cleopatra", "Egypt", ClientStatus.VIP, "Rule z.o.o.");
    private static final String PATH_TEMP_LIGHTBOXES_FILE = "tmp/lightboxes.csv";
    private static final String PATH_TEMP_CLIENT_FILE = "tmp/clients.csv";
    private static final String PATH_TEMP_PRODUCT_FILE= "tmp/products.csv";
    private static final String PATH_TEMP_RESERVATION_FILE= "tmp/reservations.csv";
//    private static final LightBox AVAILABLE_LIGHTBOX = new LightBox(STANDARD_CLIENT, "SomeLbx");

    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();

    @Test
    public void shouldLoadReservation() {
        //given
        ReservationRepository reservationRepository = new FileReservationRepository(getClass().getResource("/fixtures/reservations.csv").getPath());
        //when
        Reservation reservation = reservationRepository.load("nr2");
        //then
        assertEquals("nr2", reservation.getNumber());
        assertEquals(2, reservation.getItemsCount());
        assertEquals(false, reservation.getClose());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        ReservationRepository reservationRepository = new FileReservationRepository("fake path");
        //when
        DataAccessException ex = null;
        try {
            reservationRepository.load("nr2");
        }
        catch(DataAccessException dae) {
            ex = dae;
        }
        //then
        assertNotNull(ex);
    }

    @Test
    public void shouldWriteReservation() {
        //given
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservation();
        Product product = createProduct();
        //when
        reservation.add(product);
        reservationRepository.save(reservation);

        Reservation loadedReservation = reservationRepository.load(reservation.getNumber());

        //then
        Assert.assertEquals(1, reservation.getItemsCount());
        Assert.assertEquals(product.getNumber(), reservation.getProducts().get(0).getNumber());
        Assert.assertEquals(reservation.getNumber(), loadedReservation.getNumber());
    }

    @Test
    public void shouldOverwriteReservation() {
        //given
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservation();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when
        reservation.add(product);
        reservationRepository.save(reservation);

        Reservation overwrittenReservation = reservationRepository.load(reservation.getNumber());
        overwrittenReservation.add(secondProduct);
        reservationRepository.save(overwrittenReservation);

        //then
        Assert.assertEquals(reservation.getNumber(), overwrittenReservation.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenReservation.getProducts().get(0).getNumber());
        Assert.assertEquals(2, overwrittenReservation.getItemsCount());
    }

    @Test
    public void shouldOverwriteReservationAndAddTwoProducts() {
        //given
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservation();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when

        reservationRepository.save(reservation);

        Reservation overwrittenReservation = reservationRepository.load(reservation.getNumber());
        overwrittenReservation.add(product);
        overwrittenReservation.add(secondProduct);
        reservationRepository.save(overwrittenReservation);

        //then
        Assert.assertEquals(reservation.getNumber(), overwrittenReservation.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenReservation.getProducts().get(0).getNumber());
        Assert.assertEquals(secondProduct.getNumber(), overwrittenReservation.getProducts().get(1).getNumber());
    }

    @Test
    public void shouldntAddNoActiveProduct() {
        //given
        deleteReservationFile();
        deleteClientFile();
        deleteProductsFile();

        Reservation reservation = createReservation();
        Product product = createProduct();
        Product secondProduct = createNotAvailableProduct();
        //when

        reservationRepository.save(reservation);

        Reservation overwrittenReservation = reservationRepository.load(reservation.getNumber());
        try {
            overwrittenReservation.add(product);
            overwrittenReservation.add(secondProduct);
            reservationRepository.save(overwrittenReservation);
            Assert.fail();
        } catch (ProductNotAvailableException ex) {

        }

        //then
        Assert.assertEquals(reservation.getNumber(), overwrittenReservation.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenReservation.getProducts().get(0).getNumber());

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

    private Reservation createReservation() {
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.load(STANDARD_CLIENT.getNumber());
        Reservation reservation = new Reservation(STANDARD_CLIENT);

        return reservation;
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
