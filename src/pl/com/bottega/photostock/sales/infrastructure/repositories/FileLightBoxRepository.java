package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.io.*;
import java.util.*;

/**
 * Created by Dell on 2016-05-24.
 */
public class FileLightBoxRepository implements LightBoxRepository {

    private static final String TEMP_FILE_PATH = "tmp/tempslightboxes.csv";
    private final String path;

    public FileLightBoxRepository(String path) {
        this.path = path;
    }

    @Override
    public LightBox load(String nr) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (checkNumber(nr, line)) {
                    return parseLightBox(line, lineNumber);
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

    //number,name,clientsAndClientsRole,items,closed
    private LightBox parseLightBox(String line, int lineNumber) {
        LightBox lightBox;
        String[] components = line.split(",");

        if (!(components.length == 5))
            throw new DataAccessException("Bad product format in " + String.valueOf(lineNumber) + "file line");

        String number = components[0];
        String name = components[1];

        Set<Permission> permissions = new HashSet<>();

        String[] permissionComponents = components[2].split("\\|");
        parsePermissions(permissions, permissionComponents);

        String productsString = components[3];
        List<Product> products = new LinkedList<>();
        if (existsProducts(productsString)) {
            String[] productsArray = components[3].split("\\|");
            parseAndAddProducts(products, productsArray);
        } else
        products = new LinkedList<>();

        boolean closed = Boolean.parseBoolean(components[4]);
        lightBox = new LightBox(number, name, permissions, products, closed);
        return lightBox;
    }

    private boolean existsProducts(String productsString) {
        return productsString.trim().length() != 0;
    }

    private void parseAndAddProducts(List<Product> products, String[] productsArray) {
        for (String productNr : productsArray) {
            Product product = RepoFactory.createProductRepository().load(productNr);
            products.add(product);
        }
    }

    private void parsePermissions(Set<Permission> permissions, String[] permissionComponents) {
        for (String pC : permissionComponents) {
            String[] pCParts = pC.split(" ");
            String clientNr = pCParts[0];
            Client client = RepoFactory.createClientRepository().load(clientNr);
            ClientRole clientRole = ClientRole.valueOf(pCParts[1].toUpperCase());
            permissions.add(new Permission(client, clientRole));
        }
    }

    @Override
    public void save(LightBox lightBox) {
        File file = new File(this.path);
        File tempFile = new File(TEMP_FILE_PATH);
        boolean newRepo = !file.exists();
        String lightBoxNr = lightBox.getNumber();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true)){
            if (newRepo)
                pw.println("number,name,clientsAndClientsRole,items,closed");
            if (lightBoxNr == null) {
                lightBox.setNumber(UUID.randomUUID().toString());
                saveLightBox(lightBox, pw);
            } else {
                printTempFile(lightBox, tempFile, lightBoxNr, pw);
            }

            file.delete();
            tempFile.renameTo(file);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private void saveLightBox(LightBox lightBox, PrintWriter printWriter) {
        String csvLine = getStringLine(lightBox);
        printWriter.println(csvLine);
    }

    private String getStringLine(LightBox lightBox) {
        String[] lightBoxExported = lightBox.export();
        return String.join(",", lightBoxExported);
    }

    private void printTempFile(LightBox lightBox, File tempFile, String lightBoxNr, PrintWriter pw) {
        pw.close();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.path))); PrintWriter pw2 = new PrintWriter(new FileOutputStream(tempFile, true), true)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkNumber(lightBoxNr, line)) {
                    String csvLine = getStringLine(lightBox);
                    line = line.replace(line, csvLine);
                }
                pw2.println(line);
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    @Override
    public void remove(String nr) {

    }
}
