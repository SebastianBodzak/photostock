package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;

import java.io.*;
import java.util.*;

/**
 * Created by Dell on 2016-05-24.
 */
public class FileReservationRepository implements ReservationRepository {

    private static final String TEMP_FILE_PATH = "tmp/tempsReservations.csv";
    private final String path;
    private final ClientRepository clientRepository;
    private final ProductRepository productRepository;

    public FileReservationRepository(String path, ClientRepository clientRepository, ProductRepository productRepository) {
        this.path = path;
        this.clientRepository = clientRepository;
        this.productRepository = productRepository;
    }

    @Override
    public Reservation load(String nr) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (checkNumber(nr, line)) {
                    return parseReservation(line, lineNumber);
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

    //number,owner,items,getClose
    private Reservation parseReservation(String line, int lineNumber) {
        Reservation reservation;
        String[] components = line.split(",");

        if (!(components.length == 4))
            throw new DataAccessException("Bad product format in " + String.valueOf(lineNumber) + "file line");

        String number = components[0];
        String clientNr = components[1];
        Client client = this.clientRepository.load(clientNr);

        String productsString = components[2];
        List<Product> products = new LinkedList<>();
        if (productsExists(productsString)) {
            String[] productsArray = productsString.split("\\|");
            for (String productNr : productsArray) {
                Product product = this.productRepository.load(productNr);
                products.add(product);
            }
        } else
            products = new LinkedList<>();

        boolean closed = Boolean.parseBoolean(components[3]);
        reservation = new Reservation(number, client, products, closed);
        return reservation;
    }

    private boolean productsExists(String productsString) {
        return productsString.trim().length() != 0;
    }

    @Override
    public Reservation load(Client client) {
        return null;
    }

    @Override
    public void save(Reservation reservation) {
        File file = new File(this.path);
        File tempFile = new File(TEMP_FILE_PATH);
        boolean newRepo = !file.exists();
        String reservationNr = reservation.getNumber();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true)){
            if (newRepo)
                pw.println("number,owner,items,getClose");
            if (reservationNr == null) {
                reservation.setNumber(UUID.randomUUID().toString());
                saveReservation(reservation, pw);
            } else {
                printTempFile(reservation, tempFile, reservationNr, pw);
            }

            file.delete();
            tempFile.renameTo(file);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private void saveReservation(Reservation reservation, PrintWriter printWriter) {
        String csvLine = getStringLine(reservation);
        printWriter.println(csvLine);
    }

    private String getStringLine(Reservation reservation) {
        String[] reservationExported = reservation.export();
        return String.join(",", reservationExported);
    }

    private void printTempFile(Reservation reservation, File tempFile, String reservationNr, PrintWriter pw) {
        pw.close();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.path))); PrintWriter pw2 = new PrintWriter(new FileOutputStream(tempFile, true), true)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkNumber(reservationNr, line)) {
                    String csvLine = getStringLine(reservation);
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

    @Override
    public Reservation findOpenerPer(Client client) throws DataAccessException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            String clientNr = client.getNumber();
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line))
                    return null;
                lineNumber++;
                if (compareClientNumber(clientNr, line)) {
                    return parseReservation(line, lineNumber);
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return null;
    }

    private boolean compareClientNumber(String clientNr, String line) {
        return line.contains(clientNr);
    }

    @Override
    public List<Reservation> find(String clientNr) {
        return null;
    }
}
