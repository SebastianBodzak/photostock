package pl.com.bottega.photostock.sales.api.infrastructure.repositories;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import pl.com.bottega.photostock.sales.infrastructure.repositories.FileClientRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import static pl.com.bottega.photostock.sales.model.Money.CurrencyValues.*;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Dell on 2016-05-26.
 */
public class FileClientRepositoryTest {

    public static final Client STANDARD_CLIENT = ClientFactory.create("Janusz", "Poland");
    public static final Client VIP_CLIENT = ClientFactory.create("Cleopatra", "Egypt", ClientStatus.VIP, "Rule z.o.o.");
    public static final String PATH_TEMP_FILE = "tmp/clients.csv";

    private ClientRepository clientRepository = new FileClientRepository(PATH_TEMP_FILE);

    @After
    public void shouldRemoveClientFile() {
        deleteClientFile();
    }

    @Test
    public void shouldLoadClient() {
        //given
        ClientRepository clientRepository = new FileClientRepository(getClass().getResource("/fixtures/clients.csv").getPath());
        //when
        Client client = clientRepository.load("nr2");
        //then

        assertEquals("nr2", client.getNumber());
        assertEquals("Cleopatra", client.getName());
        assertEquals(new Money(50, Money.CurrencyValues.USD), client.getAmount());
        assertEquals(new Money(30.5, Money.CurrencyValues.USD), client.getCreditLimit());
        assertEquals("catnip", client.getCompany());
        assertTrue(client.getStatus() == ClientStatus.VIP);
    }

    @Test
    public void shouldThrowFileAccessExceptionWhenFileNotFound() {
        //given
        ClientRepository clientRepository = new FileClientRepository("fake path");
        //when
        DataAccessException ex = null;
        try {
            clientRepository.load("nr2");
        }
        catch(DataAccessException dae) {
            ex = dae;
        }
        //then
        assertNotNull(ex);
    }

    @Test
    public void shouldWriteProduct() {
        //given
        //when
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.save(VIP_CLIENT);

        Client standardClientLoad = clientRepository.load(STANDARD_CLIENT.getNumber());
        Client vipClientLoad = clientRepository.load(VIP_CLIENT.getNumber());

        standardClientLoad.getName();

        //then
        Assert.assertEquals("Janusz", standardClientLoad.getName());
        Assert.assertEquals("Poland", standardClientLoad.getAddress());
        Assert.assertEquals(new Money(200), vipClientLoad.getCreditLimit());
    }

    @Test
    public void shouldOverwriteProduct() {
        //given
        //when
        Client client = ClientFactory.create("Rain", "Earth");

        clientRepository.save(client);
        clientRepository.save(STANDARD_CLIENT);
        clientRepository.save(VIP_CLIENT);

        Client overwrittenClient = clientRepository.load(client.getNumber());
        overwrittenClient.setName("overwriteSun");
        overwrittenClient.recharge(new Money(155.23));
        clientRepository.save(overwrittenClient);

        //then
        Assert.assertEquals(client.getNumber(), overwrittenClient.getNumber());
        Assert.assertEquals("overwriteSun", overwrittenClient.getName());
        Assert.assertEquals(new Money(155.23, PLN), overwrittenClient.getAmount());
    }

    private void deleteClientFile() {
        File file = new File(PATH_TEMP_FILE);
        file.delete();
    }
}
