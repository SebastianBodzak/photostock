package pl.com.bottega.photostock.sales.model.products;

import pl.com.bottega.photostock.sales.model.AbstractProduct;
import pl.com.bottega.photostock.sales.model.Money;

import java.util.List;

/**
 * Created by Dell on 2016-03-13.
 */
public class Picture extends AbstractProduct {

    private Resolution resolution;

    public Picture(String number, String title, Money price, String author, boolean isAvailable, List<String> tags, Resolution resolution) {
        super(number, title, price, author, isAvailable, tags);
        this.resolution = resolution;
    }

    public Picture(String title, Money price, String author, boolean isAvailable, List<String> tags, Resolution resolution) {
        super(title, price, author, isAvailable, tags);
        this.resolution = resolution;
    }

    public Picture(String title, Money price, List<String> tags) {
        this(title, price, null, true, tags, null);
    }

    public Picture(String number, String title, Money price, boolean isAvailable, List<String> tags) {
        this(number, title, price, null, isAvailable, tags, null);
    }

    public Picture(String title, Money price, boolean isAvailable, List<String> tags) {
        this(title, price, null, isAvailable, tags, null);
    }

    public Resolution getResolution() {
        return resolution;
    }

//    number,title,price,currency,available,tags,resolution,type
    @Override
    public String[] export() {
        String tagsJoined = String.join(" ", this.getTags());
        return new String[] {
                getNumber(), getTitle(), String.valueOf(getPrice().cents()), String.valueOf(getPrice().getCurrency()), String.valueOf(isAvailable()), tagsJoined, "", "Picture"
        };
    }



/*    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;//wyrzucić getClass, jeśli chcę sięgnąć do klasy bazowej (pytanie, czy na pewno)(picture i logo co innego)

//        if (! (o instanceof Picture))
//            return false;
//
//        Picture picture = (Picture) o;
//
//        return title == picture.title;

        Picture picture = (Picture) o;

        return resolution == picture.resolution;

    }

    @Override
    public int hashCode() {
        return resolution != null ? resolution.hashCode() : 0;
    }*/
}
