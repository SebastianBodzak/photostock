package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Dell on 2016-04-20.
 */
public class FakeLightBoxRepository implements LightBoxRepository {

    private static Map<String, LightBox> fakeDataBase = new HashMap<>();

//    static {
//        ClientRepository clientRepository= new FakeClientRepository();
//        ProductRepository productRepository = new FakeProductRepository();
//
//        LightBox lightBoxJanusz = new LightBox(clientRepository.load("Janusz"), "random");
//        Product picture = productRepository.load("tree (1)");
//        lightBoxJanusz.add((Picture) picture);
//        LightBox lightBoxJanusz2 = new LightBox(clientRepository.load("Janusz"), "basement");
//
//        fakeDataBase.put(lightBoxJanusz.getName(), lightBoxJanusz);
//        fakeDataBase.put(lightBoxJanusz2.getName(), lightBoxJanusz2);
//    }

    @Override
    public LightBox load(String nr) {
        LightBox lightBox = fakeDataBase.get(nr);
        if (lightBox == null)
            throw new DataDoesNotExistsException("This lightbox doesn't exist: ", nr, FakeLightBoxRepository.class);
        return lightBox;
    }

    @Override
    public void save(LightBox lightBox) {
        if (lightBox.getNumber() == null)
            lightBox.setNumber(UUID.randomUUID().toString());
        fakeDataBase.put(lightBox.getNumber(), lightBox);
    }

    @Override
    public void remove(String nr) {
        LightBox lightBox = fakeDataBase.get(nr);
        if (lightBox != null)
            fakeDataBase.remove(lightBox);
    }
}
