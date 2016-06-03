/*
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

*/
/**
 * Created by Dell on 2016-05-14.
 *//*

public class FileProductRepository2 implements ProductRepository {

    private final String path;
    private static Map<String, Product> fakeDataBase = new HashMap<>();

    public FileProductRepository2(String path) {
        this.path = path;
    }

    //Line Format
    //number, title, price,currency,isAvailable,tags,resolution,type
    @Override
    public Product load(String nr) {
        try (InputStream inputStream = new FileInputStream(path)) {
            readLine(inputStream);
            String line;
            while ((line = readLine(inputStream)) != null) {
                if (line.trim().length() == 0)
                    return null;
                Product product;
                product = parseProduct(line);
                if (product.getNumber().equals(nr))
                    return product;
            }
        } catch (Exception e) {
            throw new DataAccessException(e);
        }
        return null;
    }

    //Line Format
    //number, title, price,currency,isAvailable,tags,resolution,type
    private Product parseProduct(String line) {
        Product product;
        String[] components = line.split(",");
        String number = components[0];
//                Currency currency = Currency.getInstance(String.valueOf(components[2]));
        String title = components[1];
        int priceCents = Integer.parseInt(components[2]);
//        Money price = new Money(priceCents, priceCents % 100, Money.CurrencyValues.valueOf((components[3])));
        Money price = new Money(priceCents, priceCents % 100, Money.CurrencyValues.valueOf((components[3])));
        boolean available = Boolean.parseBoolean(components[4]);
        if (components[7].startsWith("Picture")) {
//            String[] tags = components[4].split(" ");
            List<String> tags = new LinkedList<>();
            tags = Arrays.asList(components[5].split(" "));
            product = new Picture(number, title, price, available, tags);
        }
        else if (components[7].startsWith("Clip"))
            product = new Clip(number, title, price, null); //// TODO: 2016-05-14
        else
            product = new Song(number, title, price, null);
        return product;
    }

    private String readLine(InputStream inputStream) throws IOException {
        int ch;
        StringBuilder sB = new StringBuilder();
        while(((ch = inputStream.read()) != '\n') && ch != -1) {
            sB.append((char) ch);
        }
        return sB.toString();
    }

    @Override
    public void save(Product product) {
        File file = new File(this.path);
        boolean newRepo = !file.exists();
        try (OutputStream outputStream = new FileOutputStream(this.path, true)){
            if (newRepo)
                outputStream.write(("number,title,price,currency,available,tags,resolution,type"+System.lineSeparator()).getBytes());
            if (product.getNumber() == null)
                product.setNumber(UUID.randomUUID().toString());

            String[] productExported = product.export();
            String csvLine = String.join(",", productExported) + "\r\n";
            outputStream.write(csvLine.getBytes());
        } catch (Exception ex) {
            throw new DataAccessException(ex);
        }

//        if (product.getNumber() == null)
//            product.setNumber(UUID.randomUUID().toString());
//        fakeDataBase.put(product.getNumber().toLowerCase(), product);
    }

//    @Override
//    public List<Product> search(String name) {
//        return null;
//    }

    @Override
    public void remove(String nr) {

    }

    @Override
    public List<Product> find(List<String> tags, String author, Money minPrice, Money maxPrice, boolean acceptIsNotAvailable) {
        return null;
    }

    @Override
    public void removeAllProducts() {

    }
}
*/
