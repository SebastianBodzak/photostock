package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * Created by Dell on 2016-03-13.
 */
public class Reservation {

    private String number;
    private Client owner;
    private List<Product> items = new LinkedList<>();
    private boolean isOpen;
    private boolean close;

    public Reservation(Client owner) {
        this.owner = owner;
        this.close = false;

    }

    public Reservation(String number, Client owner, List<Product> items, boolean close) {
        this.number = number;
        this.owner = owner;
        this.items = items;
        this.close = close;
    }

    public void add(Product productToAdd) throws IllegalArgumentException, IllegalStateException {
        validate();
        if (items.contains(productToAdd))
            throw new IllegalArgumentException("already contains");
        if (productToAdd.isAvailable())
            items.add(productToAdd);
        else
            throw new ProductNotAvailableException("Trying to reserve", productToAdd.getNumber(), Reservation.class);
    }

    private void validate() throws  IllegalStateException {
        if (!owner.isActive())
            throw new IllegalStateException("Owner is not active!");
    }

    public void remove(Product productToRemove) {
        boolean removed = items.remove(productToRemove);
        if (!removed)
            throw new IllegalArgumentException("Product has been never added!");
    }

    public Offer generateOffer() {
        List<Product> offerItems = Lists.newLinkedList(Iterables.filter(items, new Predicate<Product>() {
            @Override
            public boolean apply(Product product) {
                return product.isAvailable();
            }
        }));


        Comparator<Product> comparator = new PriceAndNameProductComparator();
        Collections.sort(offerItems, comparator);
        return new Offer(owner, offerItems);
    }

//    public Offer generateOffer() {
//        List<Product> offerItems = new LinkedList<>();
//        for (Product product : items) {
//            if (product.isAvailable())
//                offerItems.add(product);
//        }
//
//        Comparator<Product> comparator = new PriceAndNameProductComparator();
//        Collections.sort(offerItems, comparator);
//        return new Offer(owner, offerItems);
//    }

    public int getItemsCount() {
        return items.size();
    }

    public Client getOwner() {
        return owner;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public boolean getOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public void open() {
        this.close = false;
    }

    public boolean getClose() {
        return close;
    }

    //number,owner,items,getClose
    public String[] export() {
        StringBuilder sBProducts = new StringBuilder();
        String spliter = "";
        for (Product prod : items) {
            String productNr = prod.getNumber();
            sBProducts.append(spliter).append(productNr);
            spliter = "|";
        }
        return new String[] {getNumber(), getOwner().getNumber(), sBProducts.toString(), String.valueOf(getClose())};
    }

    public List<Product> getProducts() {
        return items;
    }

//    public void setClose(boolean getClose) {
//        this.getClose = getClose;
//    }

}
