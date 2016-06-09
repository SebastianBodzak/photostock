package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.model.products.Picture;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dell on 2016-03-13.
 */
public class Purchase {

    private String number;
    private Client owner;
    private List<Product> items = new LinkedList<>();
    private Date date;

    public Purchase(String number, Client owner, List<Product> items, String dateString) throws ParseException {
        this.number = number;
        this.owner = owner;
        this.items = items;
        String date = dateString;
    }

    public Purchase(Client owner, List<Product> items) {
        this.owner = owner;
        this.items = items;
        this.date = new Date();
    }

    public Purchase(String purchaseNumber, Client owner, List<Product> items) {

        number = purchaseNumber;
        this.owner = owner;
        this.items = items;
    }

    public String getNumber() {
        return number;
    }


    public void setNumber(String number) {
        this.number = number;
    }

    public Client getOwner() {
        return owner;
    }

    public List<Product> getItems() {
        return items;
    }

    public Date getDate() {
        return date;
    }

    //number,owner,items,date
    public String[] export() {
        StringBuilder sBProducts = new StringBuilder();
        String spliter = "";
        for (Product prod : items) {
            String productNr = prod.getNumber();
            sBProducts.append(spliter).append(productNr);
            spliter = "|";
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = formatter.format(getDate());
        //wyciągnąć z date longa

        return new String[] {getNumber(), getOwner().getNumber(), sBProducts.toString(), date};
    }
}
