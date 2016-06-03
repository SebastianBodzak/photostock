package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import java.util.*;

/**
 * Created by Dell on 2016-04-23.
 */
public class FakePurchaseRepository implements PurchaseRepository {
    public static Map<String, Purchase> fakeDataBase = new HashMap<>();


    @Override
    public Purchase load(String nr) {
        Purchase purchase = fakeDataBase.get(nr);
        if (purchase == null)
            throw new DataDoesNotExistsException("This reservation doesn't exist: ", nr, FakePurchaseRepository.class);
        return purchase;
    }

    @Override
    public void save(Purchase purchase) {
        if (purchase.getNumber() == null)
            purchase.setNumber(UUID.randomUUID().toString());//symulacja generowania ID przez bazę danych
        fakeDataBase.put(purchase.getNumber(), purchase);
    }

//    @Override
//    public List<Product> search(Client client) {
//        String clientNr = client.getNumber();
//        List<Product> productList = new LinkedList<>();
//        Iterator iterator = fakeDataBase.entrySet().iterator();
//        while (iterator.hasNext()) {
//            Map.Entry<String, Purchase> pairs = (Map.Entry)iterator.next();
//            if (pairs.getValue().getOwner().getNumber().equals(clientNr)) {
//                for (Product prod : pairs.getValue().getItems())
//                    productList.add(prod);
//            }
//            return  productList;
//        }
//        return null;
//    }
//
    @Override
    public List<Product> search(Client client) {
        String clientNr = client.getNumber();
        List<Product> productList = new LinkedList<>();
        Iterator iterator = fakeDataBase.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Purchase> pairs = (Map.Entry)iterator.next();
            if (pairs.getValue().getOwner().getNumber().equals(clientNr)) {
                for (Product prod : pairs.getValue().getItems())
                    productList.add(prod);
            }
            return  productList;
        }
        return null;
    }

//    @Override
//    public Reservation findOpenerPer(Client client) {
//        for (Reservation reservation : fakeDataBase.values()) {
//            if (reservation.getOwner().equals(client) && !(reservation.getClose()))
//                return reservation;
//        }
//        return  null;
//    }

    @Override
    public List<Purchase> find(String clientNr) {
        List<Purchase> result = new LinkedList<>();

        for(Purchase purchase : fakeDataBase.values())
            if(purchase.getOwner().getNumber().equals(clientNr))
                result.add(purchase);

        return result;
    }

}
