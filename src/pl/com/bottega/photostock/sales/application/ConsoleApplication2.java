package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Picture;
import static pl.com.bottega.photostock.sales.model.products.Resolution.*;

/**
 * Created by Dell on 2016-03-31.
 */
public class ConsoleApplication2 {
    public static void main(String[] args) {
        ClientRepository clientRepository = new FakeClientRepository();
        ProductRepository productRepository = new FakeProductRepository();
        ReservationRepository reservationRepository = new FakeReservationRepository();

        Client janusz = clientRepository.load("nr1");

//        1 dodanie kilku zdjęć do rezerwacji (w tym próba dodanie niedostępnych)
        Picture lumberJack = new Picture("lumberjack (2)", new Money(2), null, false, null, DUZA);
        Picture kitty = new Picture("kitty (2)", new Money(2), null);
        Picture kitty2 = new Picture("kitty (2)", new Money(2), null);
        Picture computer = new Picture("zdjęcie powtarzające się", new Money(4), null);
        Picture tree = new Picture("tree (1)", new Money(1), null);
        Picture lighting = new Picture("nr nieaktywny", new Money(2), null, false, null, MALA);
        Picture cellar = new Picture("cellar (3)", new Money(3), null);
        Picture door = new Picture("door (8)", new Money(8), null);

        Product mustang = productRepository.load("mustang (2)"); //zamiast new Picture
        Product multipla = productRepository.load("multipla (3)"); //zamiast new Picture
//        Product kitty1 = repository.load("nr3"); //zamiast new Picture
//        Product computer1 = repository.load("nr4"); //zamiast new Picture
        try {
            Product itDoesNotExist = productRepository.load("itDoesNotExist");
        }
        catch (RuntimeException ex) {
            System.out.println("This product doesn't exist " + ex);
        }
        Reservation reservation1 = new Reservation(janusz);
//            Reservation reservation1 = reservationRepository.load("nr1");
        try {
//            reservation1.add(lumberJack);
//            reservation1.add(kitty);
//            reservation1.add(kitty2);
//            reservation1.add(computer);
//            reservation1.add(lighting);
//            reservation1.add(cellar);
            reservation1.add(cellar);
            reservation1.add(door);
            reservation1.add(mustang);
            reservation1.add(multipla);
//            reservation1.add(itDoesNotExist);
        }
        catch (ProductNotAvailableException ex) {
            System.out.println(ex.getClazz() + " " + ex.getMessage() + " " + ex.getNumber());
        }
        catch (IllegalArgumentException ex) {
            System.out.println("You cannot add the same picture twice, try again" + ex);
        }

        System.out.println(reservation1.getItemsCount());

//        2 zmiana dostępności niektórych zdjęć
        kitty.changeIsAvailable();
        productRepository.save(mustang);
        productRepository.save(multipla);

//        3 wygenerowanie oferty - nie powinny do niej trafić zdjęcia zmienione w punkcie 2
        Offer offer =  reservation1.generateOffer();
        System.out.println(offer.getItemsCount());
        for (Product p : offer.getItems()){
            System.out.println(p.getNumber());
        }
//        System.out.println(offer.getTotalCost());

//        4. stworzenie zakupu
//        Purchase purchase = new Purchase(janusz, offer.getOfferPictures());
    }
}
