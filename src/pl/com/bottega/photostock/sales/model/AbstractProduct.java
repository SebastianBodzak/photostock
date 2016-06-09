package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import java.util.Arrays;
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

    /**
     * Constructor only for tests
     * @param number
     * @param title
     */
    public AbstractProduct(String number, String title) {
        this.number = number;
        this.title = title;
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
        return available == that.available &&
                Objects.equal(number, that.number) &&
                Objects.equal(title, that.title) &&
                Objects.equal(price, that.price) &&
                Objects.equal(author, that.author) &&
                Objects.equal(tags, that.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number, title, price, author, available, tags);
    }

    @Override
    public boolean isTagedBy(String tag) {
        return tags.contains(tag);
    }

    @Override
    public boolean isTagedByAnyOf(List<String> tags) {
        Optional<String> tag = Iterables.tryFind(tags, new Predicate<String>() {
            @Override
            public boolean apply(String s) {
                return isTagedBy(s);
            }
        });
        return tag.isPresent();
    }
}
