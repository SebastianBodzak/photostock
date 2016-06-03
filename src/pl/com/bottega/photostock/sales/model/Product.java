package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.model.Client;

import java.util.List;

/**
 * Created by Dell on 2016-04-03.
 */
public interface Product {
    public boolean isAvailable();
    public Money calculatePrice();
    public void cancel();
    public void reservePer(Client client);
    public void unreservePer(Client client);
    public Money getPrice();
    public String getNumber();
    public List<String> getTags();
    public String getTitle();
    public void setTitle(String title);
    public void setNumber(String number);
    public String getAuthor();

    public void activate();
    public void deactivate();
    String[] export();
}