package pl.com.bottega.photostock.sales.model;

import java.util.List;

/**
 * Created by Dell on 2016-04-20.
 */
public interface ReservationRepository {
    public Reservation load(String number);
    public Reservation load(Client client);
    public void save(Reservation reservation);
    public void remove(String nr);
    public Reservation findOpenerPer(Client client);
    public List<Reservation> find(String clientNr);
}
