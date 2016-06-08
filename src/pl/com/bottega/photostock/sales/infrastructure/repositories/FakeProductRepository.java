package pl.com.bottega.photostock.sales.infrastructure.repositories;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Offer;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.ProductRepository;

import java.util.*;

/**
 * Created by Dell on 2016-04-17.
 */
public class FakeProductRepository implements ProductRepository {

    private static Map<String, Product> fakeDataBase = new LinkedHashMap<>();

//    static {
//        Picture tree = new Picture("nr1", "nr1", new Money(1), Arrays.asList("tree", "green"));
//        Picture cellar = new Picture("nr2", "nr2", new Money(3), Arrays.asList("cellar", "basement", "dark"));
//        Picture door = new Picture("nr3", "nr3", new Money(8), Arrays.asList("door", "dark"));
//        Picture lumberJack = new Picture("lumberjack (2)", "lumberjack (2)", new Money(2), null, false, null, DUZA);
//        Picture kitty = new Picture("kitty (2)", "kitty (2)", new Money(2), null);
//        Picture kitty2 = new Picture("kitty (2)", "kitty (2)", new Money(2), null);
//        Picture computer = new Picture("zdjęcie powtarzające się", "zdjęcie powtarzające się", new Money(4), null);
//        Picture lighting = new Picture("nr nieaktywny", "nr nieaktywny", new Money(2), null, false, null, MALA);
//        Picture mustang = new Picture("mustang (2)", "mustang (2)", new Money(2), null); //zamiast new Picture
//        Picture multipla = new Picture("multipla (3)", "multipla (3)", new Money(3), null); //zamiast new Picture
//
//        fakeDataBase.put(tree.getNumber().toLowerCase(), tree); //nr1
//        fakeDataBase.put(cellar.getNumber().toLowerCase(), cellar); //nr2
//        fakeDataBase.put(door.getNumber().toLowerCase(), door); //nr3
//        fakeDataBase.put(kitty.getNumber().toLowerCase(), kitty);
//        fakeDataBase.put(multipla.getNumber().toLowerCase(), multipla);
//        fakeDataBase.put(mustang.getNumber().toLowerCase(), mustang);
//        fakeDataBase.put(computer.getNumber().toLowerCase(), computer);
//
//    }

    @Override
    public Product load(String nr) {
        Product product = fakeDataBase.get(nr);
        if (product == null)
            throw new ProductNotAvailableException("Product " + nr + " doesn't exist", nr, FakeProductRepository.class);//TODO wprowadzić wyjątek ProductDoesntExistException
        return product;
    }

    @Override
    public void save(Product product) {
        if (product.getNumber() == null)
            product.setNumber(UUID.randomUUID().toString());
        fakeDataBase.put(product.getNumber().toLowerCase(), product);
    }

//    @Override
//    public List<Product> search(String name) {
//        List<Product> searchingProducts = new LinkedList<>();
//        Iterator iterator = fakeDataBase.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Product> pairs = (Map.Entry)iterator.next();
//            if (pairs.getValue().getTitle().contains(name.toLowerCase()))
//                searchingProducts.add(pairs.getValue());
//        }
//        return searchingProducts;
//    }

    @Override
    public void remove(String nr) {
        Product product = fakeDataBase.get(nr);
        if (product != null)
            fakeDataBase.remove(product);
        else
            throw new DataDoesNotExistsException("This Client doesnt exist: ", nr, FakeClientRepository.class);
    }

    @Override
    public List<Product> find(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) { //todo
        List<Product> result;

        if (EmptyFilter(tags, author, minPrice, maxPrice, acceptIsNotAvailable))
            result = new ArrayList<>(fakeDataBase.values());
        else{
            result = new LinkedList<>();

            for(Product product : fakeDataBase.values()) {
                if (! (acceptIsNotAvailable || product.isAvailable()))
                    continue;

                if (minPrice != null && product.calculatePrice().ge(minPrice)) {
                    result.add(product);
                }

                if (maxPrice != null && product.calculatePrice().le(maxPrice)) {
                    result.add(product);
                }

                if (tags != null && product.isTagedByAnyOf(tags)) {
                    result.add(product);
                }

                if (author != null && product.getAuthor().equals(author)) {
                    result.add(product);
                }
            }
        }
        return result;
    }
//
//    public Offer generateOffer() {
//        List<Product> offerItems = Lists.newLinkedList(Iterables.filter(items, new Predicate<Product>() {
//            @Override
//            public boolean apply(Product product) {
//                return product.isAvailable();
//            }
//        }));

    @Override
    public void removeAllProducts() {

    }

    private boolean EmptyFilter(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) {
        return acceptIsNotAvailable && (tags == null || tags.size() == 0) && author == null && minPrice == null && maxPrice == null;
    }

    private List<Product> compareTags(List<Product> productList, List<String> tags) {
        Iterator<Product> iterator = productList.iterator();
        while (iterator.hasNext()){
            Product prod = iterator.next();
            List<String> prodTag = prod.getTags();
            for (String tag : tags) {
                if (!prodTag.contains(tag))
                    iterator.remove();
            }
        }
        return productList;
    }
}