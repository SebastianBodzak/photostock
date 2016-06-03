package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.products.*;

import java.util.Arrays;

/**
 * Created by Dell on 2016-04-03.
 */
public class ProductTester {
    public static void main(String[] args) {
        ClientRepository clientRepository = new FakeClientRepository();
        ReservationRepository reservationRepository = new FakeReservationRepository();

        Client janusz = clientRepository.load("nr1");
        Reservation reservation1 = new Reservation(janusz);

        Client takiSobieClient = clientRepository.load("nr4");
        takiSobieClient.recharge(new Money(25.00));
        Reservation reservation2 = new Reservation(takiSobieClient);


        Picture lumberJack = new Picture("nr1ssf", new Money(2), null);
        Picture kitty = new Picture("kjfhkfs", new Money(3), null);
        Picture computer = new Picture("zdjęcie powtarzające się", new Money(4), null);
        Picture tree = new Picture("nr4ss", new Money(1), null);
        Picture lighting = new Picture("nr nieaktywny", new Money(2), null, false, null, Resolution.MALA);
        Picture cellar = new Picture("nr92SF", new Money(3), null);
        Picture door = new Picture("nr7FS", new Money(8), null);
        Picture mustang = new Picture("nr1", new Money(10), Arrays.asList("ford", "mustang"));

//        Clip playingCats = new Clip("Playing Cats", new Money(22), "Mr Zoochosis");
//        Clip noActiveClip = new Clip("No active at all", new Money(12), "Mr Zoochosis", false);

//        Song beatMe = new Song("Eye of Java Tiger", new Money(13), "DJ BLust Rhymes");
//        Song noActiveSong = new Song("Sad but no active", new Money(13), "Dr Dj BJ", false, null, "3:40", Channels.CHANNELS_FIVE_TO_ONE);

//        addAndCountItemsInTheOffer(reservation1, lumberJack, lighting, noActiveClip, noActiveSong);
//        addAndCountItemsInTheOffer(reservation2, mustang, tree, playingCats, beatMe);
    }

    private static void addAndCountItemsInTheOffer(Reservation reservation, Picture picture1, Picture picture2, Clip clip, Song song) {
        reservation.add(picture1);
        reservation.add(picture2);
        reservation.add(clip);
        reservation.add(song);
        Offer offer = reservation.generateOffer();
        int count = offer.getItemsCount();
        System.out.println("Number of positions: " + count);
        System.out.println("Sum of offer: " + offer.getTotalCost() + "\n");
    }
}
