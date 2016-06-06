package pl.com.bottega.photostock.sales.api;

import com.google.common.base.Preconditions;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeProductRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakePurchaseRepository;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeReservationRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;
import pl.com.bottega.photostock.sales.model.products.Song;

import java.util.LinkedList;
import java.util.List;

import static pl.com.bottega.photostock.sales.model.AbstractProduct.KindOfProduct.*;

/**
 * Created by Dell on 2016-05-09.
 */
public class AdminPanel {

    private ClientRepository clientRepository = RepoFactory.createClientRepository();
    private ProductRepository productRepository = RepoFactory.createProductRepository();

    public String addProduct(AbstractProduct.KindOfProduct kindOfProduct, String name, Money price, String ...tags) throws IllegalArgumentException {
        List<String> productTags = new LinkedList<>();
        if (tags != null)
            for (String tag : tags) {
                productTags.add(tag);
        }
        Product product;
        switch (kindOfProduct) {
            case PICTURE:
                product = new Picture(name, price, productTags);
                break;
            case CLIP:
                product = new Clip(name, price, productTags);
                break;
            case SONG:
                product = new Song(name, price, productTags);
                break;
            default:
                throw new IllegalArgumentException("Invalid kind of product");
        }
        productRepository.save(product);
        return product.getNumber();
    }

//    public void promoteClientToVip(String clientNr) throws IllegalStateException {
//        Client client = clientRepository.load(clientNr);
//        if (client.getStatus() != ClientStatus.VIP) {
//            ClientFactory.promoteToVip(clientNr);
//            client = clientRepository.load(clientNr);
//            clientRepository.save(client);
//        } else {
//            throw new IllegalStateException("Client is already VIP");
//        }
//    }

    public void promoteClientToVip(String clientNr) throws IllegalStateException {
        Client client = clientRepository.load(clientNr);
        Preconditions.checkState(client.getStatus() != ClientStatus.VIP, "%s is already VIP", clientNr);
            ClientFactory.promoteToVip(clientNr);
            client = clientRepository.load(clientNr);
            clientRepository.save(client);
    }

    public void changeCreditLimit(String clientNr, Money newCreditLimit) throws IllegalStateException {
        Client client = clientRepository.load((clientNr));
        Preconditions.checkState(client.getStatus() == ClientStatus.VIP, "%s is not a VIP", clientNr);
            client.modifyCreditLimit(newCreditLimit);
            clientRepository.save(client);
    }

    public void changeAvailability(String productNr, boolean available) {
        Product product = productRepository.load(productNr);
        if (available)
            product.activate();
        else
            product.deactivate();
        productRepository.save(product);
    }

    public void removeProduct(String productId) {
        productRepository.remove(productId);
    }
}
