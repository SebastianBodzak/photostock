package pl.com.bottega.photostock.sales.model;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by Dell on 2016-04-07.
 */
public abstract class AbstractProduct implements Product {

    public enum KindOfProduct {
        PICTURE, CLIP, SONG;
    }

    private String number;
    private String title;
    private Money price;
    private String author;
    private boolean available;
    private List<String> tags = new LinkedList<>();

    public AbstractProduct(String title, Money price, String author, boolean available, List<String> tags) {
        this.title = title.toLowerCase();
        this.price = price;
        this.available = available;
        this.author = author;
        if (tags != null) {
            ListIterator<String> iterator = tags.listIterator();
            while (iterator.hasNext())
                iterator.set(iterator.next().toLowerCase());
        }
        this.tags = tags;
    }

    public AbstractProduct(String number, String title, Money price, String author, boolean available, List<String> tags) {
        this.number = number;
        this.title = title;
        this.price = price;
        this.available = available;
        this.author = author;
        if (tags != null) {
            ListIterator<String> iterator = tags.listIterator();
            while (iterator.hasNext())
                iterator.set(iterator.next().toLowerCase());
        }
        this.tags = tags;
    }

    @Override
    public boolean isAvailable(){
        return available;
    }

    @Override
    public Money calculatePrice() {
        return price;
    }

    @Override
    public void cancel() {
        available = false;
    }

    @Override
    public void reservePer(Client client) {

    }

    @Override
    public void unreservePer(Client client) {

    }

    @Override
    public Money getPrice() {
        return price;
    }

    @Override
    public String getNumber() {
        return number;
    }

    @Override
    public List<String> getTags() {
        return tags;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public String getAuthor() {
        return author;
    }

    @Override
    public void activate() {
        available = true;
    }

    @Override
    public void deactivate() {
        available = false;
    }

    public void soldPer(Client reservingClient) {

    }

    public boolean changeIsAvailable() {
        if (available == true) {
            available = false;
        } else {
            available = true;
        }
        return available;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractProduct that = (AbstractProduct) o;

        if (available != that.available) return false;
        if (number != null ? !number.equals(that.number) : that.number != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (author != null ? !author.equals(that.author) : that.author != null) return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;

    }

    @Override
    public int hashCode() {
        int result = number != null ? number.hashCode() : 0;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (available ? 1 : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }
}
