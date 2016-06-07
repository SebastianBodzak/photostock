package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeLightBoxRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
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

    private static final String PATH_TEMP_FILE= "tmp/reservations.csv";

    private ClientRepository clientRepository = new FakeClientRepository();
    private ProductRepository productRepository = new FakeProductRepository();
    private LightBoxRepository lightBoxRepository = new FakeLightBoxRepository();
    private ReservationRepository reservationRepository = new FileReservationRepository(PATH_TEMP_FILE, clientRepository, productRepository);

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
        deleteReservationsFile();
    }

//    private static final LightBox AVAILABLE_LIGHTBOX = new LightBox(STANDARD_CLIENT, "SomeLbx");

    @Test
    public void shouldLoadReservation() {
        //given
        ReservationRepository reservationRepository = new FileReservationRepository(getClass().getResource("/fixtures/reservations.csv").getPath(), clientRepository, productRepository);
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
        ReservationRepository reservationRepository = new FileReservationRepository("fake path", clientRepository, productRepository);
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

    private void deleteReservationsFile() {
        File file = new File(PATH_TEMP_FILE);
        file.delete();
    }

    private LightBox createLightBox() {
        clientRepository.load(STANDARD_CLIENT.getNumber());
        return new LightBox(STANDARD_CLIENT, "SomeLbx");
    }

    private Reservation createReservation() {
        clientRepository.load(STANDARD_CLIENT.getNumber());
        return new Reservation(STANDARD_CLIENT);
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
}
