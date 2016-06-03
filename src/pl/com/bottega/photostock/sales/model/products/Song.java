package pl.com.bottega.photostock.sales.model.products;

import pl.com.bottega.photostock.sales.model.AbstractProduct;
import pl.com.bottega.photostock.sales.model.Money;

import java.util.List;

/**
 * Created by Dell on 2016-04-07.
 */
public class Song extends AbstractProduct{

    private String duration;
    private Channels channels;

    public Song (String number, String title, Money price, String author, boolean isAvailable, List<String> tags, String duration, Channels channels) {
        super(number, title, price, author, isAvailable, tags);
        this.duration = duration;
        this.channels = channels;
    }

    public Song (String title, Money price, String author, boolean isAvailable, List<String> tags, String duration, Channels channels) {
        super(title, price, author, isAvailable, tags);
        this.duration = duration;
        this.channels = channels;
    }

//    public Song (String title, Money price, String author) {
//        this(title, price, author, true, null, null, null);
//    }

    public Song (String number, String title, Money price, List<String> tags) {
        this(number, title, price, null, true, tags, null, null);
    }

    public Song (String title, Money price, List<String> tags) {
        this(title, price, null, true, tags, null, null);
    }

    public Song (String title, Money price, String author, boolean active) {
        this(title, price, author, active, null, null, null);
    }

    public String getDuration() {
        return  duration;
    }

    public Channels channels() {
        return  channels;
    }

    //    number,price,currency,available,tags,resolution,type
    @Override
    public String[] export() {
        String tagsJoined = String.join(" ", this.getTags());
        return new String[] {
                getNumber(), getTitle(), String.valueOf(getPrice().cents()), String.valueOf(getPrice().getCurrency()), String.valueOf(isAvailable()), tagsJoined, "", "Song"
        };
    }
}
