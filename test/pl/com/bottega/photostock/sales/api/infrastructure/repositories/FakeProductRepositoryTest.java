package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import com.sun.org.glassfish.external.arc.Taxonomy;
import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dell on 2016-06-08.
 */
public class FakeProductRepositoryTest {

    private ProductRepository productRepository = new FakeProductRepository();
    public static final Product AVAILABLE_CLIP = new Clip("nr1", new Money(100.3), Arrays.asList("tree", "forest"));
    public static final Product NO_AVAILABLE_PICTURE = new Picture("nr2", new Money(20.5), false, Arrays.asList("house", "flat"));
    public static final Product AVAILABLE_PICTURE = new Picture("nr3", new Money(33), Arrays.asList("forest", "queen"));
    public static final Product AVAILABLE_PICTURE2 = new Picture("nr4", new Money(10), Arrays.asList("king", "queen"));
    public static final Product AVAILABLE_PICTURE3 = new Picture("nr5", new Money(60), Arrays.asList("cat", "predator"));

    @Test
    public void shouldSaveAndLoadProduct() {
        //given
        //when
        productRepository.save(AVAILABLE_CLIP);
        Product clip = productRepository.load(AVAILABLE_CLIP.getNumber());
        //then
        Assert.assertEquals(Arrays.asList("tree", "forest"), clip.getTags());
        Assert.assertEquals(new Money(100.3), clip.getPrice());
    }

    @Test
    public void shouldFindProductByTags() {
        //given
        //when
        addProductsToRepository();
        List<Product> products = new LinkedList<>();
        products = productRepository.find(Arrays.asList("forest"), null, null, null, true);

        Assert.assertEquals(2, products.size());
        Assert.assertEquals(Arrays.asList("forest", "queen"), products.get(1).getTags());
    }

    @Test
    public void shouldFindAvailableProductByMaxPrice() {
        //given
        //when
        addProductsToRepository();
        List<Product> products = new LinkedList<>();
        products = productRepository.find(null, null, null, new Money(40), false);

        Money sumOfProductsPrices = products.get(0).getPrice().add(products.get(1).getPrice());
        Assert.assertEquals(2, products.size());
        Assert.assertEquals(new Money(43), sumOfProductsPrices);
    }

    @Test
    public void shouldFindAllProductByMaxPrice() {
        //given
        //when
        addProductsToRepository();
        List<Product> products = new LinkedList<>();
        products = productRepository.find(null, null, null, new Money(40), true);

        Money sumOfProductsPrices = products.get(0).getPrice().add(products.get(1).getPrice()).add(products.get(2).getPrice());
        Assert.assertEquals(3, products.size());
        Assert.assertEquals(new Money(63.5), sumOfProductsPrices);
    }

    private void addProductsToRepository() {
        productRepository.save(AVAILABLE_CLIP);
        productRepository.save(AVAILABLE_PICTURE);
        productRepository.save(AVAILABLE_PICTURE2);
        productRepository.save(AVAILABLE_PICTURE3);
        productRepository.save(NO_AVAILABLE_PICTURE);
    }
}
