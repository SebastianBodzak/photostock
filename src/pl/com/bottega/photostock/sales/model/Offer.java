package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dell on 2016-03-13.
 */
public class Offer {

    private final Client owner;
    private List<Product> items;
    private Money totalCost;

    public Offer(Client owner, List<Product> items) throws IllegalArgumentException {
        Preconditions.checkArgument(items.size() != 0, "Items can't be empty", items);

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
        Money sum = new Money(0);
        for(Product p : items)
            sum = sum.add(p.calculatePrice());
        return sum;
    }

    public Money getTotalCost() {
        return totalCost;
    }
}
