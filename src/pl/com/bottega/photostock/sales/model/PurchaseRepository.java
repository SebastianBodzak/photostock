package pl.com.bottega.photostock.sales.model;

import java.util.List;

/**
 * Created by Dell on 2016-04-23.
 */
public interface PurchaseRepository {
    public Purchase load(String nr);
    public void save(Purchase purchase);
    public List<Product> search(Client client);
    public List<Purchase> find(String clientNr);
}
