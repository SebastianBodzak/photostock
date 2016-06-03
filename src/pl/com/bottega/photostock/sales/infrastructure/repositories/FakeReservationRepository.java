package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import java.util.*;

/**
 * Created by Dell on 2016-04-20.
 */
public class FakeReservationRepository implements ReservationRepository {

    public static Map<String, Reservation> fakeDataBase = new HashMap<>();

//    static {
//        ClientRepository repository = new FakeClientRepository();
//
//        Client client1 = repository.load("Janusz");
//        Client client2 = repository.load("Zegrzysław");
//        Client client3 = repository.load("Cleopatra");
//        Client client4 = repository.load("Java");
//
//        Reservation reservation1 = new Reservation(client1);
//        Reservation reservation2 = new Reservation(client2);
//        Reservation reservation3 = new Reservation(client3);
//        Reservation reservation4 = new Reservation(client4);
//
//        fakeDataBase.put(reservation1.getNumber(), reservation1);
//        fakeDataBase.put(reservation2.getNumber(), reservation2);
//        fakeDataBase.put(reservation3.getNumber(), reservation3);
//        fakeDataBase.put(reservation4.getNumber(), reservation4);
//    }

    @Override
    public Reservation load(String nr) throws DataDoesNotExistsException {
        Reservation reservation = fakeDataBase.get(nr);
        if (reservation == null)
            throw new DataDoesNotExistsException("This reservation doesn't exist: ", nr, FakeReservationRepository.class);
        return reservation;
    }

    @Override
    public Reservation load(Client client)  {//loadOpenPer todo
        String clientNr = client.getNumber();
        Iterator iterator = fakeDataBase.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Reservation> pairs = (Map.Entry)iterator.next();
            if (pairs.getValue().getOwner().getNumber().equals(clientNr) && pairs.getValue().getOpen())
                return fakeDataBase.get(pairs.getValue().getNumber());
        }
//        throw new DataDoesNotExistsException("Client's reservation doesn't exist: ", clientNr, FakeReservationRepository.class); //null
        return null;
    }

    @Override
    public void save(Reservation reservation) {
        if (reservation.getNumber() == null)
            reservation.setNumber(UUID.randomUUID().toString());//symulacja generowania ID przez bazę danych
        fakeDataBase.put(reservation.getNumber(), reservation);
    }

    @Override
    public void remove(String nr) throws DataDoesNotExistsException {
        Reservation reservation = fakeDataBase.get(nr);
        if (reservation != null)
            fakeDataBase.remove(reservation);
        else
            throw new DataDoesNotExistsException("This reservation doesn't exist", nr, FakeReservationRepository.class);
    }

    @Override
    public Reservation findOpenerPer(Client client) {
        for (Reservation reservation : fakeDataBase.values()) {
            if (reservation.getOwner().equals(client) && !(reservation.getClose()))
                return reservation;
        }
        return  null;
    }

    @Override
    public List<Reservation> find(String clientNr) {
        return null;
    }
}