package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.Assert;
import org.junit.Test;
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

    private static final Client STANDARD_CLIENT = ClientFactory.create("Janusz", "Poland");
    private static final String PATH_TEMP_FILE = "tmp/lightboxes.csv";
    private static final String PATH_TEMP_CLIENT_FILE = "tmp/clients.csv";
    private static final String PATH_TEMP_PRODUCT_FILE= "tmp/products.csv";

    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();

    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();

    @Test
    public void shouldLoadLightBox() {
        //given
        LightBoxRepository lightBoxRepository = new FileLightBoxRepository(getClass().getResource("/fixtures/lightboxes.csv").getPath());
        //when
        LightBox lightBox = lightBoxRepository.load("nr1");
        //then
        assertEquals("nr1", lightBox.getNumber());
        assertEquals("lbxName", lightBox.getName());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        LightBoxRepository lightBoxRepository = new FileLightBoxRepository("fake path");
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
        deleteLightBoxesFile();
        deleteClientFile();
        deleteProductsFile();

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
        deleteLightBoxesFile();
        deleteClientFile();
        deleteProductsFile();

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
        deleteLightBoxesFile();
        deleteClientFile();
        deleteProductsFile();

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
        deleteLightBoxesFile();
        deleteClientFile();
        deleteProductsFile();

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

    private void deleteClientFile() {
        File file = new File(PATH_TEMP_CLIENT_FILE);
        file.delete();
    }

    private void deleteProductsFile() {
        File file = new File(PATH_TEMP_PRODUCT_FILE);
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
