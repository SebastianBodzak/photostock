package pl.com.bottega.photostock.sales.infrastructure.repositories;

import pl.com.bottega.photostock.sales.model.Money;
import pl.com.bottega.photostock.sales.model.Product;
import pl.com.bottega.photostock.sales.model.ProductRepository;
import pl.com.bottega.photostock.sales.model.exceptions.DataAccessException;
import pl.com.bottega.photostock.sales.model.products.Clip;
import pl.com.bottega.photostock.sales.model.products.Picture;
import pl.com.bottega.photostock.sales.model.products.Song;

import java.io.*;
import java.util.*;

/**
 * Created by Dell on 2016-05-14.
 */
public class FileProductRepository implements ProductRepository {

    private static final String TEMP_FILE_PATH = "tmp/tempproducts.csv";
    private final String path;

    public FileProductRepository(String path) {
        this.path = path;
    }

    //Line Format
    //number, title, price,currency,isAvailable,tags,resolution,type
    @Override
    public Product load(String nr) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line)) return null;
                lineNumber++;
                if (checkNumber(nr, line)) {
                    return parseProduct(line, lineNumber);
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

    //Line Format
    //number, title, price,currency,isAvailable,tags,resolution,type
    private Product parseProduct(String line, int lineNumber) throws DataAccessException {
        Product product;
        String[] components = line.split(",");
        if (!(components.length == 8))
            throw new DataAccessException("Bad product format in " + String.valueOf(lineNumber) + " file line");
        String number = components[0];
        String title = components[1];
        int priceCents = Integer.parseInt(components[2]);
        Money price = new Money(priceCents/100, priceCents % 100, Money.CurrencyValues.valueOf((components[3])));
        boolean available = Boolean.parseBoolean(components[4]);
        List<String> tags = new LinkedList<>();
        if (components[7].startsWith("Picture")) {
            tags = Arrays.asList(components[5].split(" "));
            product = new Picture(number, title, price, available, tags);
        } else if (components[7].startsWith("Clip")){
            tags = Arrays.asList(components[5].split(" "));
            product = new Clip(number, title, price, tags); //// TODO: 2016-05-14
        } else {
            tags = Arrays.asList(components[5].split(" "));
            product = new Song(number, title, price, tags);
        }
        return product;
    }

    @Override
    public void save(Product product) throws DataAccessException {
        File file = new File(this.path);
        File tempFile = new File(TEMP_FILE_PATH);
        boolean newRepo = !file.exists();
        String productNr = product.getNumber();
        try (PrintWriter pw = new PrintWriter(new FileOutputStream(file, true), true)){
            if (newRepo)
                pw.println("number,title,price,currency,available,tags,resolution,type");
            if (productNr == null) {
                product.setNumber(UUID.randomUUID().toString());

                String[] productExported = product.export();
                String csvLine = String.join(",", productExported);
                pw.println(csvLine);
            } else {
                printTempFile(product, tempFile, productNr, pw);
            }

            file.delete();
            tempFile.renameTo(file);

        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }
    }

    private void printTempFile(Product product, File tempFile, String productNr, PrintWriter pw) {
        pw.close();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.path))); PrintWriter pw2 = new PrintWriter(new FileOutputStream(tempFile, true), true)) {
            String line;
            while ((line = br.readLine()) != null) {
                if (checkNumber(productNr, line)) {
                    String[] productExported = product.export();
                    String csvLine = String.join(",", productExported);
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
    public List<Product> find(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) {
        List<Product> result = new LinkedList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)))) {
            String line;
            int lineNumber = 0;
            breakpoint : while ((line = br.readLine()) != null) {
                if (ifFileIsEmpty(line)) {
                    return null;
                }
                if(lineNumber == 0) {
                    lineNumber++;
                    continue breakpoint;
                }
                lineNumber++;
                Product product = parseProduct(line, lineNumber);

                if (EmptyFilter(tags, author, minPrice, maxPrice, acceptIsNotAvailable))
                    result.add(product);
                else {
                        if (! (acceptIsNotAvailable || product.isAvailable()))
                            continue;

                    compareMinPrice(minPrice, result, product);
                    compareMaxPrice(maxPrice, result, product);
                    compareTags(tags, result, product);
                    compareAuthor(author, result, product);
                    }
                }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return result;
    }

    private void compareAuthor(String author, List<Product> result, Product product) {
        if (author != null && product.getAuthor().equals(author)) {
                result.add(product);
            }
    }

    private void compareMinPrice(Money minPrice, List<Product> result, Product product) {
        if (minPrice != null && product.calculatePrice().ge(minPrice)) {
            result.add(product);
        }
    }

    private void compareMaxPrice(Money maxPrice, List<Product> result, Product product) {
        if (maxPrice != null && product.calculatePrice().le(maxPrice)) {
            result.add(product);
        }
    }

    private void compareTags(List<String> tags, List<Product> result, Product product) {
        if (tags != null) {
            breakPoint : for (String tag : tags) {
                for (String prodTag : product.getTags())
                    if (tag == prodTag) {
                        result.add(product);
                        break breakPoint;
                    }
            }
        }
    }

    private boolean EmptyFilter(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) {
        return acceptIsNotAvailable && (tags == null || tags.size() == 0) && author == null && minPrice == null && maxPrice == null;
    }

    @Override
    public void removeAllProducts() {

    }
}
