package pl.com.bottega.photostock.sales.model.products;

import pl.com.bottega.photostock.sales.model.AbstractProduct;
import pl.com.bottega.photostock.sales.model.Money;

import java.util.List;

/**
 * Created by Dell on 2016-04-03.
 */
public class Clip extends AbstractProduct {

    private String duriation;
    private Resolution resolution;

    public Clip (String number, String title, Money price, String author, boolean isAvailable, List<String> tags, String duriation, Resolution resolution) {
        super(number, title, price, author, isAvailable, tags);
        this.duriation = duriation;
        this.resolution = resolution;
    }

    public Clip (String title, Money price, String author, boolean isAvailable, List<String> tags, String duriation, Resolution resolution) {
        super(title, price, author, isAvailable, tags);
        this.duriation = duriation;
        this.resolution = resolution;
    }

//    public Clip (String title, Money price, String author) {
//        this(title, price, author, true, null, null, null);
//    }

    public Clip (String title, Money price, List<String> tags) {
        this(title, price, null, true, tags, null, null);
    }

    public Clip (String number, String title, Money price, List<String> tags) {
        this(number, title, price, null, true, tags, null, null);
    }

    public Clip (String title, Money price, String author, boolean active) {
        this(title, price, author, active, null, null, null);
    }

    public String getDuriation() {
        return duriation;
    }

    public Resolution getResolution(){
        return resolution;
    }

    // TODO: 2016-05-15
    //    number,price,currency,available,tags,resolution,type
    @Override
    public String[] export() {
        String tagsJoined = String.join(" ", this.getTags());
        return new String[] {
                getNumber(), getTitle(), String.valueOf(getPrice().cents()), String.valueOf(getPrice().getCurrency()), String.valueOf(isAvailable()), tagsJoined, "", "Clip"
        };
    }
}
