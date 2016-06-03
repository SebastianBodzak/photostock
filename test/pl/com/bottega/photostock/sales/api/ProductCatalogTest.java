package pl.com.bottega.photostock.sales.api;

import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dell on 2016-05-08.
 */
public class ProductCatalogTest {

//    private static final String AVAILABLE_PRODUCT_NR = "nr1"; //"nr1", new Money(1), Arrays.asList("tree", "green")
//    private static final String AVAILABLE_SECOND_PRODUCT_NR = "nr2"; //"nr2", new Money(3), Arrays.asList("cellar", "basement", "dark")
//    private static final String AVAILABLE_THIRD_PRODUCT_NR = "nr3"; //"nr3", new Money(8), Arrays.asList("door", "dark")
//    private static final String AVAILABLE_FOURTH_PRODUCT_NR = "multipla (3)";

    private ProductRepository productRepository = new FakeProductRepository();

//    @Test
//    public void shouldFindProductsByName() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//        Product productNr4 = makeFourthAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", null, null, null);
//        assert !productList.isEmpty();
//        assert productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert productList.contains(productNr3);
//        assert !productList.contains(productNr4);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3, productNr4);
//    }
//
//    @Test
//    public void shouldNotFindProductsByName() {
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("kjjjshsjksjkfjgre5tgfrgedgeedee4tgerrgksfjksfjk", null, null, null);
//        assert productList.isEmpty();
//    }
//
//    @Test  //// TODO: 2016-05-14  
//    public void shouldThrowExceptionsBecauseOfLackName() {
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        try {
//            productList = productsCatalog.find(null, null, null, null);
//            Assert.fail();
//        }
//        catch (IllegalArgumentException ex) {
//
//        }
//    }

//    @Test
//    public void shouldFindProductsByPrice() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", new Money(3), new Money(7), null);
//        for (Product product : productList) {
//            assert (product.getPrice().ge(new Money(3)) && product.getPrice().le(new Money(7)));
//        }
//
//        assert !productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert !productList.contains(productNr3);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3);
//    }
//
//    @Test
//    public void shouldFindProductsAboveOrEqualMinPrice() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", new Money(3), null, null);
//        for (Product product : productList) {
//            assert (product.getPrice().ge(new Money(3)));
//        }
//
//        assert !productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert productList.contains(productNr3);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3);
//    }
//
//    @Test
//    public void shouldFindProductsUnderOrEqualMaxPrice() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", null, new Money(6), null);
//        for (Product product : productList) {
//            assert (product.getPrice().le(new Money(6)));
//        }
//
//        assert productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert !productList.contains(productNr3);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3);
//    }
//
//    @Test
//    public void shouldFindProductsByTags() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", null, null, Arrays.asList("dark"));
//
//        assert !productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert productList.contains(productNr3);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3);
//    }
//
//    @Test
//    public void shouldFindProductsByAllParameters() {
//        Product productNr1 = makeFirstAvailableProduct();
//        Product productNr2 = makeSecondAvailableProduct();
//        Product productNr3 = makeThirdAvailableProduct();
//
//        ProductsCatalog productsCatalog = new ProductsCatalog();
//        List<Product> productList;
//        productList = productsCatalog.find("nr", new Money(2), new Money(10), Arrays.asList("dark", "cellar"));
//
//        assert !productList.contains(productNr1);
//        assert productList.contains(productNr2);
//        assert !productList.contains(productNr3);
//
//        removeProductsFromRepository(productNr1, productNr2, productNr3);
//    }

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

    private Product makeFourthAvailableProduct() {
        Product multipla = new Picture("multipla (3)", new Money(3), null);
        productRepository.save(multipla);
        return productRepository.load(multipla.getNumber());
    }

    private void removeProductsFromRepository(Product ...products) {
        for (Product prod : products)
            productRepository.remove(prod.getNumber());
    }
}
