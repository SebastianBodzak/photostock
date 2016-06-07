package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Dell on 2016-05-15.
 */
public class FilePurchaseRepository implements PurchaseRepository {

    private final String path;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public FilePurchaseRepository(String path, ClientRepository clientRepository, ProductRepository productRepository) {
        this.path = path;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Purchase load(String nr) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (checkNumber(nr, line)) {
                    return parsePurchase(line, lineNumber);
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

    //number,owner,items,date
    private Purchase parsePurchase(String line, int lineNumber) throws ParseException {
        String[] components = line.split(",");

        if (!(components.length == 4))
            throw new DataAccessException("Bad product format in " + String.valueOf(lineNumber) + "file line");

        String number = components[0];
        String clientNr = components[1];
        Client client = this.clientRepository.load(clientNr);

        String productsString = components[2];
        List<Product> products = new LinkedList<>();
            String[] productsArray = productsString.split("\\|");
            for (String productNr : productsArray) {
                Product product = this.productRepository.load(productNr);
                products.add(product);
        }
        String date = components[3];
        Purchase purchase = new Purchase(number, client, products, date);
        return purchase;
    }

    @Override
    public void save(Purchase purchase) {
        File file = new File(this.path);
        boolean newRepo = !file.exists();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true)){
            if (newRepo)
                pw.println("number,owner,items,date");
            purchase.setNumber(UUID.randomUUID().toString());
            savePurchase(purchase, pw);
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private void savePurchase(Purchase purchase, PrintWriter printWriter) {
        String csvLine = getStringLine(purchase);
        printWriter.println(csvLine);
    }

    private String getStringLine(Purchase purchase) {
        String[] purchaseExported = purchase.export();
        return String.join(",", purchaseExported);
    }

    @Override
    public List<Product> search(Client client) {
        return null;
    }

    @Override
    public List<Purchase> find(String clientNr) throws DataAccessException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            List<Purchase> purchaseList = new LinkedList<>();
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (checkClientNumber(clientNr, line)) {
                    purchaseList.add(parsePurchase(line, lineNumber));
                }
            }
            return purchaseList;
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
    }

    private boolean checkClientNumber(String clientNr, String line) {
        return line.contains(clientNr);
    }

}
