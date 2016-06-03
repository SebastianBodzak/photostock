package pl.com.bottega.photostock.sales.api;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

/**
 * Created by Dell on 2016-04-23.
 */
public class PurchaseProcess {
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();
    private PurchaseRepository purchaseRepository = RepoFactory.createPurchaseRepository();
    private static final Money PROMOTE_VIP_SUM = new Money(1000, Money.CurrencyValues.PLN);

    public void add(String clientNr, String productNr) {

        //dodanie zalogowania, uwierzetylnienie, zapisać w historii dla potrzeb własnych etc. -> Spring
        Client client = clientRepository.load(clientNr);
        Reservation reservation;
        try {
            reservation = reservationRepository.findOpenerPer(client);
            if (reservation == null) {
                reservation = create(clientNr);
            }
        } catch (DataAccessException ex) {
            reservation = create(clientNr);
        }

        Product product = productRepository.load(productNr);
        reservation.add(product);
        reservationRepository.save(reservation);
    }

    private Reservation create(String clientNr) throws IllegalArgumentException {
        Client client = clientRepository.load(clientNr);
        Reservation reservation = new Reservation(client);
        reservation.open();
        reservationRepository.save(reservation);
        return reservation;
    }

      public Offer calculateOffer(String clientNr) throws DataDoesNotExistsException {
          Client client = clientRepository.load(clientNr);
          Reservation reservation = reservationRepository.findOpenerPer(client);
          if (reservation == null)
              throw new IllegalStateException("client does not have opened reservation");
          return reservation.generateOffer();
      }



    public void closeReservation(String reservationNr) {
        Reservation reservation = reservationRepository.load(reservationNr);
        reservation.getClose();
    }

    public void confirm(String clientNr) throws IllegalStateException {
        Client client = clientRepository.load(clientNr);
        Reservation reservation = reservationRepository.findOpenerPer(client);

        if (reservation == null)
            throw new IllegalStateException ("Client does not have reservation");

        confirm(client, reservation);
    }

/*    public void confirm(String reservationNr, String payerNr) {
        Reservation reservation = reservationRepository.load(reservationNr);
        Client client = clientRepository.load(payerNr);
        confirm(client, reservation);
    }*/

    private void confirm(Client client, Reservation reservation) {
        AdminPanel adminPanel = new AdminPanel();
        Offer offer = reservation.generateOffer();
        Money totalSum = offer.getTotalCost();
        if (client.canAfford(totalSum)) {
            client.charge(totalSum, "za coś");
            Purchase purchase = new Purchase(client, offer.getItems());
            purchaseRepository.save(purchase);
            reservation.getClose();
            if (totalSum.ge(PROMOTE_VIP_SUM)) {
                adminPanel.promoteClientToVip(client.getNumber());
            }
        }
        reservationRepository.save(reservation);
        clientRepository.save(client);
    }
}
