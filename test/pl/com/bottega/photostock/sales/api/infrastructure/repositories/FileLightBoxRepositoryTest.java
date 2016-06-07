package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FileLightBoxRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dell on 2016-05-26.
 */
public class FileLightBoxRepositoryTest {

    private static final String PATH_TEMP_FILE = "tmp/lightboxes.csv";

    private ClientRepository clientRepository = new FakeClientRepository();
    private ProductRepository productRepository = new FakeProductRepository();

    private LightBoxRepository lightBoxRepository = new FileLightBoxRepository(PATH_TEMP_FILE, clientRepository, productRepository);
    private static final Client STANDARD_CLIENT = ClientFactory.create("nr1", "Janusz", ClientStatus.STANDARD);
    private static final Client STANDARD_CLIENT_2 = ClientFactory.create("nr2", "Janina", ClientStatus.STANDARD);
    private static final Product STANDARD_PRODUCT = new Picture("nr1", "title1", new Money(10), true, null);
    private static final Product STANDARD_PRODUCT_2 = new Picture("nr2", "title2", new Money(8), true, null);
    private static final Product STANDARD_PRODUCT_3 = new Picture("nr3", "title3", new Money(20), true, null);
    private static final Product NOT_AVAILABLE_PRODUCT = new Picture("nr3", "title3", new Money(20), false, null);

    @Before
    public void shouldRemoveRepositoriesAndCreateClientsAndProducts() {
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.save(STANDARD_CLIENT_2);
        productRepository.save(STANDARD_PRODUCT);
        productRepository.save(STANDARD_PRODUCT_2);
        productRepository.save(STANDARD_PRODUCT_3);
        productRepository.save(NOT_AVAILABLE_PRODUCT);
    }

    @After
    public void shouldCleanRepositories() {
        clientRepository.remove(STANDARD_CLIENT.getNumber());
        clientRepository.remove(STANDARD_CLIENT_2.getNumber());
        productRepository.remove(STANDARD_PRODUCT.getNumber());
        productRepository.remove(STANDARD_PRODUCT_2.getNumber());
        productRepository.remove(STANDARD_PRODUCT_3.getNumber());
        productRepository.remove(NOT_AVAILABLE_PRODUCT.getNumber());
        deleteLightBoxesFile();
    }

    @Test
    public void shouldLoadLightBox() {
        //given
        LightBoxRepository lightBoxRepository = new FileLightBoxRepository(getClass().getResource("/fixtures/lightboxes.csv").getPath(), clientRepository, productRepository);
        //when
        LightBox lightBox = lightBoxRepository.load("nr1");
        //then
        assertEquals("nr1", lightBox.getNumber());
        assertEquals("lbxName", lightBox.getName());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        LightBoxRepository lightBoxRepository = new FileLightBoxRepository("fake path", clientRepository, productRepository);
        //when
        DataAccessException ex = null;
        try {
            lightBoxRepository.load("nr1");
        }
        catch(DataAccessException dae) {
            ex = dae;
        }
        //then
        assertNotNull(ex);
    }

    @Test
    public void shouldWriteLightBox() {
        //given
        LightBox lightBox = createLightBox();
        Product product = createProduct();
        //when
        lightBox.add(product);
        lightBoxRepository.save(lightBox);

        LightBox standardLightBox = lightBoxRepository.load(lightBox.getNumber());

        //then
        Assert.assertEquals(1, lightBox.getItemsCounter());
        Assert.assertEquals(lightBox.getNumber(), standardLightBox.getNumber());
    }

    @Test
    public void shouldOverwriteLightBox() {
        //given
        LightBox lightBox = createLightBox();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when

        lightBox.add(product);
        lightBoxRepository.save(lightBox);


        LightBox overwrittenLightBox = lightBoxRepository.load(lightBox.getNumber());
        overwrittenLightBox.add(secondProduct);
        lightBoxRepository.save(overwrittenLightBox);

        //then
        Assert.assertEquals(lightBox.getNumber(), overwrittenLightBox.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenLightBox.getProducts().get(0).getNumber());
        Assert.assertEquals(2, overwrittenLightBox.getItemsCounter());
    }

    @Test
    public void shouldOverwriteLightBoxAndAddTwoProducts() {
        //given
        LightBox lightBox = createLightBox();
        Product product = createProduct();
        Product secondProduct = createSecondProduct();
        //when

        lightBoxRepository.save(lightBox);

        LightBox overwrittenLightBox = lightBoxRepository.load(lightBox.getNumber());
        overwrittenLightBox.add(product);
        overwrittenLightBox.add(secondProduct);
        lightBoxRepository.save(overwrittenLightBox);

        //then
        Assert.assertEquals(lightBox.getNumber(), overwrittenLightBox.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenLightBox.getProducts().get(0).getNumber());
        Assert.assertEquals(secondProduct.getNumber(), overwrittenLightBox.getProducts().get(1).getNumber());
    }

    @Test
    public void shouldntAddNoActiveProduct() {
        //given
        LightBox lightBox = createLightBox();
        Product product = createProduct();
        Product secondProduct = createNotAvailableProduct();
        //when

        lightBoxRepository.save(lightBox);

        LightBox overwrittenLightBox = lightBoxRepository.load(lightBox.getNumber());
        try {
            overwrittenLightBox.add(product);
            overwrittenLightBox.add(secondProduct);
            lightBoxRepository.save(overwrittenLightBox);
            Assert.fail();
        } catch (ProductNotAvailableException ex) {

        }

        //then
        Assert.assertEquals(lightBox.getNumber(), overwrittenLightBox.getNumber());
        Assert.assertEquals(product.getNumber(), overwrittenLightBox.getProducts().get(0).getNumber());

    }

    private void deleteLightBoxesFile() {
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

    private void addProductToLightBox(String lightBoxNr) {

    }
}
