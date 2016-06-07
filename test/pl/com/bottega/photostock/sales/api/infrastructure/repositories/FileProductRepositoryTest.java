package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FileProductRepository;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.io.File;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dell on 2016-05-14.
 */
public class FileProductRepositoryTest {

    public static final Product AVAILABLE_CLIP = new Clip("nr1", new Money(100.3), Arrays.asList("tree", "forest"));
    public static final Product NO_AVAILABLE_PICTURE = new Picture("nr2", new Money(20.5), false, Arrays.asList("house", "flat"));
    public static final String PATH_TEMP_FILE = "tmp/products.csv";

    private ProductRepository productRepository = new FileProductRepository(PATH_TEMP_FILE);

    @After
    public void shouldDeleteProductFile() {
        deleteProductsFile();
    }

    @Test
    public void shouldLoadProduct() {
        //given
        ProductRepository productRepository = new FileProductRepository(getClass().getResource("/fixtures/products.csv").getPath());
        //when
        Product product = productRepository.load("nr1");
        //then

        assertEquals("nr1", product.getNumber());
        assertEquals(Picture.class, product.getClass());
        assertEquals(new Money(100.0, Money.CurrencyValues.PLN), product.calculatePrice());
        assertTrue(product.isAvailable());
        assertEquals(Arrays.asList("tag1", "tag2", "tag3", "tag4"), ((Picture) product).getTags());
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        ProductRepository productRepository = new FileProductRepository("fake path");
        //when
        DataAccessException ex = null;
        try {
            productRepository.load("nr2");
        }
        catch(DataAccessException dae) {
            ex = dae;
        }
        //then
        assertNotNull(ex);
    }

    @Test
    public void shouldThrowDataAccessException() {
        //given
        ProductRepository productRepository = new FileProductRepository(getClass().getResource("/fixtures/products.csv").getPath());
        //when
        DataAccessException ex = null;
        try {
            productRepository.load("badFormatNr");
            Assert.fail();
        }
        catch (DataAccessException e) {
            ex = e;
        }
//        then
        assertNotNull(ex);
    }

    @Test
    public void shouldWriteProduct() {
        //given
        //when
        productRepository.save(AVAILABLE_CLIP);
        productRepository.save(NO_AVAILABLE_PICTURE);

        Product clipRead = productRepository.load(AVAILABLE_CLIP.getNumber());
        Product pictureRead = productRepository.load(NO_AVAILABLE_PICTURE.getNumber());

        clipRead.getTags();

        //then
        Assert.assertEquals("nr1", clipRead.getTitle());
        Assert.assertEquals("nr2", pictureRead.getTitle());
        Assert.assertEquals(new Money(100.3), clipRead.calculatePrice());
        Assert.assertEquals(Arrays.asList("tree", "forest"), ((Clip) clipRead).getTags());
        Assert.assertEquals(Arrays.asList("house", "flat"), ((Picture) pictureRead).getTags());
        Assert.assertEquals(false, pictureRead.isAvailable());
    }

    @Test
    public void shouldOverwriteProduct() {
        //given
        //when
        Product product = new Picture("nr3", new Money(22), true, Arrays.asList("house", "flat"));

        productRepository.save(product);
        productRepository.save(AVAILABLE_CLIP);
        productRepository.save(NO_AVAILABLE_PICTURE);

        Product overwrittenPicture = productRepository.load(product.getNumber());
        overwrittenPicture.setTitle("overwrite");
        productRepository.save(overwrittenPicture);

        //then
        Assert.assertEquals(product.getNumber(), overwrittenPicture.getNumber());
        Assert.assertEquals("overwrite", overwrittenPicture.getTitle());
    }

    private void deleteProductsFile() {
        File file = new File(PATH_TEMP_FILE);
        file.delete();
    }
}
