package pl.com.bottega.photostock.sales.model;

import java.util.List;

/**
 * Created by Dell on 2016-04-17.
 */
public interface ProductRepository {
    public Product load(String nr);
    public void save(Product product);
//    public List<Product> search(String name);
    public void remove(String nr);
    public List<Product> find(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable);
    public void removeAllProducts();
}
