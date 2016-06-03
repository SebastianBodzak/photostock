package pl.com.bottega.photostock.sales.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016-03-13.
 */
public class Offer {

    private final Client owner;
    private List<Product> items;
    private Money totalCost;

    public Offer(Client owner, List<Product> items) {
        if (items.size() == 0)
            throw new IllegalArgumentException("Items can't be empty");

        this.owner = owner;
        this.items = items;
        this.totalCost = calculateTotalCost();
    }

    public int getItemsCount() {
        return items.size();
    }

    public List<Product> getItems() {
        return items;
    }

    private Money calculateTotalCost() {
        Money sum = new Money(0);//items.get(0).calculatePrice().getZero();

        for(Product p : items)
            sum = sum.add(p.calculatePrice());

        return sum;
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public List<Product> getOfferPictures() {
        return items;
    }
}
