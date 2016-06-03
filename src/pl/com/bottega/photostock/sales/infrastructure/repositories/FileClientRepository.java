package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.client.strategies.PayingStrategy;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.io.*;
import java.util.*;

/**
 * Created by Dell on 2016-05-24.
 */
public class FileClientRepository implements ClientRepository {

    private static final String TEMP_FILE_PATH = "tmp/tempsclient.csv";
    private final String path;

    public FileClientRepository(String path) {
        this.path = path;
    }

    @Override
    public Client load(String nr) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (checkNumber(nr, line)) {
                    return parseClient(line, lineNumber);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return null;
    }

    private boolean ifFileIsEmpty(String line) {
        if (line.trim().length() == 0)
            return true;
        return false;
    }

    private boolean checkNumber(String nr, String line) {
        return line.startsWith(nr);
    }

    //number,name,address,status,debt,amount,creditLimit,currency,company,payingStrategy
    private Client parseClient(String line, int lineNumber) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Client client;
        String[] components = line.split(",");
        if (!(components.length == 10))
            throw new DataAccessException("Bad product format in " + String.valueOf(lineNumber) + "file line");

        String number = components[0];
        String name = components[1];
        String address = components[2];
        ClientStatus clientStatus = ClientStatus.valueOf(components[3].toUpperCase());
        int debtCents = Integer.parseInt(components[4]);
        int amountCents = Integer.parseInt(components[5]);
        int creditLimitCents = Integer.parseInt(components[6]);
        Money.CurrencyValues currency = Money.CurrencyValues.valueOf(components[7]);
        Company company = new Company(components[8]);
        Class payingStrategyClass = Class.forName("pl.com.bottega.photostock.sales.model.client.strategies."+components[9]);

        PayingStrategy payingStrategy = (PayingStrategy) payingStrategyClass.newInstance();
        Money debt = new Money(debtCents/100, debtCents % 100, currency);
        Money amount = new Money(amountCents/100, amountCents % 100, currency);
        Money creditLimit = new Money(creditLimitCents/100, creditLimitCents % 100, currency);

        client = new Client(number, name, address, clientStatus, debt, amount, creditLimit, company, payingStrategy);
        return client;
    }


    @Override
    public void save(Client client) {
        File file = new File(this.path);
        File tempFile = new File(TEMP_FILE_PATH);
        boolean newRepo = !file.exists();
        String clientNr = client.getNumber();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true)){
            if (newRepo)
                pw.println("number,name,address,status,debt,amount,creditLimit,currency,company,payingStrategy");
            if (clientNr == null) {
                client.setNumber(UUID.randomUUID().toString());
                saveClient(client, pw);
            } else {
                printTempFile(client, tempFile, clientNr, pw);
            }

            file.delete();
            tempFile.renameTo(file);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private void saveClient(Client client, PrintWriter printWriter) {
        String csvLine = getStringLine(client);
        printWriter.println(csvLine);
    }

    private void printTempFile(Client client, File tempFile, String clientNr, PrintWriter pw) {
        pw.close();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.path))); PrintWriter pw2 = new PrintWriter(new FileOutputStream(tempFile, true), true)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkNumber(clientNr, line)) {
                    String csvLine = getStringLine(client);
                    line = line.replace(line, csvLine);
                }
                pw2.println(line);
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private String getStringLine(Client client) {
        String[] clientExported = client.export();
        return String.join(",", clientExported);
    }

    @Override
    public void remove(String nr) {

    }
}
