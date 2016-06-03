package pl.com.bottega.photostock.sales.model;

/**
 * Created by Dell on 2016-04-20.
 */
public interface ClientRepository {
    public Client load(String number);
    public void save(Client client);
    public void remove(String nr);
}
