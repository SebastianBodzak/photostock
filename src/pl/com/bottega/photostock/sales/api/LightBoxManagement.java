package pl.com.bottega.photostock.sales.api;


import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeLightBoxRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Created by Dell on 2016-04-23.
 */
public class LightBoxManagement {
    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private LightBoxRepository lightBoxRepository = RepoFactory.createLightBoxRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();
    private ReservationRepository reservationRepository = RepoFactory.createReservationRepository();

    PurchaseProcess purchaseProcess = new PurchaseProcess();

    public String create(String clientNr, String name) {
        Client client = clientRepository.load(clientNr);
        LightBox lightBox = new LightBox(client);
        lightBox.changeName(name);
        lightBoxRepository.save(lightBox);
        return lightBox.getNumber();
    }

    public void deleteLightBox(String lightBoxNr) {
        lightBoxRepository.remove(lightBoxNr);
    }

    public void add(String lightBoxNr, String productNr) {
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        Product product = productRepository.load(productNr);

        lightBox.add(product);
        lightBoxRepository.save(lightBox);
        productRepository.save(product);
    }

    public void remove(String lightBoxNr, String productNr) {
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        Product product = productRepository.load(productNr);

        lightBox.remove(product);
        lightBoxRepository.save(lightBox);
        productRepository.save(product);
    }

    public void share(String lightBoxNr, String ownerNr, String clientNr, ClientRole clientRole) throws IllegalStateException {
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        Client owner = clientRepository.load(ownerNr);
        Client client = clientRepository.load(clientNr);

        Preconditions.checkState(usersWorksSameCompany(owner, client) && ownerIsAdmin(owner, lightBox), "You can not add this user");
            lightBox.addUser(client, clientRole);
        lightBoxRepository.save(lightBox);
    }

    private boolean usersWorksSameCompany(Client owner, Client client) {
        if (owner.getCompany().equals(client.getCompany()))
            return true;
        else
            return false;
    }
//    private boolean usersWorksSameCompany(Client owner, Client client) {
//        Set<Company> adminCompanies = owner.getCompanies();
//        Set<Company> clientCompanies = client.getCompanies();
//
//        for (Company com : adminCompanies) {
//            for (Company comp : clientCompanies) {
//                if (com.getName() == comp.getName()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }

    private boolean ownerIsAdmin(Client owner, LightBox lightBox) {
        Set<Permission> owners = lightBox.getOwners();
        for (Permission per : owners) {
            if (per.getClient().getName().equals(owner.getName())) {
                if (per.getClientRole().equals(ClientRole.ADMIN))
                    return true;
            }
        }
        return false;
    }

    public void addAllToReservation(String lightboxId, String clientId){
        LightBox lightBox = lightBoxRepository.load(lightboxId);

        for (Product product : lightBox.getProducts()){
            purchaseProcess.add(clientId, product.getNumber());
        }

        lightBoxRepository.save(lightBox);
    }

    public void reserve(String lightBoxId, String reservationNr, String ... productNr) {
        LightBox lightBox = lightBoxRepository.load(lightBoxId);
        Reservation reservation = reservationRepository.load(reservationNr);

        for (String prodNr : productNr) {
            Product product = productRepository.load(prodNr);
            reservation.add(product);
            lightBox.getProducts().remove(product);
            productRepository.save(product);
        }

        lightBoxRepository.save(lightBox);
        reservationRepository.save(reservation);
    }

//    public void addToReservation(String lightboxId, String pictureId){
//        LightBox lightBox = lightBoxRepository.load(lightboxId);
//        String clientNr = lightBox.getOwners().getNumber();
//
//        Collection<Picture> products = Collections2.filter(lightBox.getProducts(), new Predicate<Picture>() {
//            @Override
//            public boolean apply(Picture picture) {
//                return false;
//            }
//        });
//
//        for (Picture picture : lightBox.getItems()){
//            if (picture.getNumber().equals(pictureId)) {
//                purchaseProcess.add(clientNr, picture.getNumber());
//                lightBoxRepository.save(lightBox);
//                return;
//            }
//        }
//
//        throw new IllegalArgumentException(lightboxId + " does not contain " + pictureId);
//    }

    public String clone(String lightBoxNr, String clientNr) {
        LightBox lightBox = lightBoxRepository.load(lightBoxNr);
        Client client = clientRepository.load(clientNr);
        LightBox lightBoxCopy = lightBox.cloneLightBox(client, lightBox);
        lightBoxRepository.save(lightBoxCopy);
        return lightBoxCopy.getNumber();
    }
}
