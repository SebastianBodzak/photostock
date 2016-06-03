package pl.com.bottega.photostock.sales.model;

import java.util.Comparator;

/**
 * Created by Dell on 2016-04-17.
 */
public class PriceAndNameProductComparator implements Comparator<Product> {
    @Override
    public int compare(Product o1, Product o2) {
        if (o1.calculatePrice() == o2.calculatePrice()){//TODO jak zmienimy na Money to equals
            return o1.getNumber().compareTo(o2.getNumber());
        }
        else{
            if (o1.calculatePrice().gt(o2.calculatePrice()))
                return 1;
            return -1;
        }
    }
}
//    @Override
//    public int compare(Product o1, Product o2) {
//        if (o1.calculatePrice() == o2.calculatePrice()){ //TODO jak zmienimy na MOney to equals
//            return o1.getNumber().compareTo(o2.getNumber());
//        } else {
//            if (o1.calculatePrice() > o2.calculatePrice())
//                return 1;
//        }
//    }
//}