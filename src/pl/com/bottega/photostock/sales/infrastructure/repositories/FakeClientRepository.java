package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.ClientFactory;
import pl.com.bottega.photostock.sales.model.exceptions.DataDoesNotExistsException;

import static pl.com.bottega.photostock.sales.model.ClientStatus.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


/**
 * Created by Dell on 2016-04-20.
 */
public class FakeClientRepository implements ClientRepository {

    private static Map<String, Client> fakeDataBase = new HashMap<>();

//    static {
//        Client janusz = ClientFactory.create("nr1", "Janusz", "Księżyc", STANDARD, "Very Big Company");
//        Client korowiow = ClientFactory.create("nr2", "Korowiow", "Hell", STANDARD, "Very Big Company");
//        Client java = ClientFactory.create("nr3", "Java", "Very Deep Hell", STANDARD, "Small And Totally Different Company");
//        Client zegrzysław = ClientFactory.create("nr4", "Zegrzysław", "secret", SILVER, null);
//        Client maciej = ClientFactory.create("nr5", "Maciej", "NY", VIP, "Another Company");
//        Client stokrotka = ClientFactory.create("nr6", "Stokrotka", "Łąka", VIP, null);
//        Client zenobiusz = ClientFactory.create("nr7", "Zenobiusz", "LA", VIP, null);
//        Client cleopatra = ClientFactory.create("nr8", "Cleopatra", "Teby", PLATINUM, null);
//        Client marylin = ClientFactory.create("nr9", "Marylin", "LA", GOLD, null);
//
//        fakeDataBase.put(janusz.getNumber(), janusz);
//        fakeDataBase.put(korowiow.getNumber(), korowiow);
//        fakeDataBase.put(java.getNumber(), java);
//        fakeDataBase.put(zegrzysław.getNumber(), zegrzysław);
//        fakeDataBase.put(maciej.getNumber(), maciej);
//        fakeDataBase.put(stokrotka.getNumber(), stokrotka);
//        fakeDataBase.put(zenobiusz.getNumber(), zenobiusz);
//        fakeDataBase.put(cleopatra.getNumber(), cleopatra);
//        fakeDataBase.put(marylin.getNumber(), marylin);
//    }

    @Override
    public Client load(String nr) throws DataDoesNotExistsException {
        Client client = fakeDataBase.get(nr);
        if (client == null)
            throw new DataDoesNotExistsException("This Client doesnt exist: ", nr, FakeClientRepository.class);
        return client;
    }

    @Override
    public void save(Client client) {
        if (client.getNumber() == null)
            client.setNumber(UUID.randomUUID().toString());
        fakeDataBase.put(client.getNumber(), client);
    }

    @Override
    public void remove(String nr) throws DataDoesNotExistsException {
        Client client = fakeDataBase.get(nr);
        if (client != null)
            fakeDataBase.remove(client);
        else
            throw new DataDoesNotExistsException("This Client doesnt exist: ", nr, FakeClientRepository.class);
    }
}
