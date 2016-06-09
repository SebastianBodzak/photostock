package pl.com.bottega.photostock.sales.model;

import com.google.common.base.*;
import com.google.common.base.Objects;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.*;

/**
 * Created by Dell on 2016-03-13.
 */
public class LightBox {

    private String number;
    private String name;
//    private Client owner;
    private Set<Permission> permissions = new HashSet<>();
    private List<Product> items = new LinkedList<>();
    private boolean closed = false;

    public LightBox(String number, String name, Set<Permission> permissions, List<Product> items, boolean closed) {
        this.number = number;
        this.name = name;
        this.permissions = permissions;
        this.items = items;
        this.closed = closed;
    }

    public LightBox(Client owner) {
        Permission maker = new Permission(owner, ClientRole.ADMIN);
        this.permissions.add(maker);
    }

    public LightBox(Client owner, String name){
        Permission maker = new Permission(owner, ClientRole.ADMIN);
        this.permissions.add(maker);
        this.name = name;
    }

    //factory method
    public static LightBox cloneLightBox(Client owner, LightBox lightBox) {
        LightBox lightBoxDuplicate = new LightBox(owner);
        List<Product> newList = new LinkedList<>(lightBox.getProducts());
        lightBoxDuplicate.items = newList;
        lightBoxDuplicate.name = lightBox.name;
        return lightBoxDuplicate;
    }

    public void addUser(Client client, ClientRole clientRole) {
        validateUser(client, clientRole);
        Permission newUser = new Permission(client, clientRole);
        this.permissions.add(newUser);
    }

    private boolean validateUser(Client client, ClientRole clientRole) {
        Preconditions.checkState(client != null, "This user doesnt exist!", client);
            Preconditions.checkArgument(hasNotBeenAddEarlier(client), "This user has access to your lightBox already!", client);
                return true;
    }

    private boolean hasNotBeenAddEarlier(Client client) {
        for (Permission per : permissions) {
            if (per.getClient().getName().equals(client.getName()))
                return false;
        }
        return true;
    }

    public void close() {
        this.closed = true;
    }

    public void changeName(String newName) {
        validate();
        this.name = newName;
    }

    public void add(Product productToAdd) throws IllegalStateException, IllegalArgumentException, ProductNotAvailableException {
        validate();
        Preconditions.checkArgument(!items.contains(productToAdd), "already contains");
            if (!productToAdd.isAvailable())
                throw new ProductNotAvailableException("Trying to reserve", productToAdd.getNumber(), Reservation.class);
        items.add(productToAdd);
    }

    private void validate() throws IllegalStateException{
        Preconditions.checkState(!closed, "Lightbox is closed");
//        if (!owner.isActive())
//            throw new IllegalStateException("Owner is not active!");
    }


    public void remove(Product product) throws IllegalArgumentException {
        validate();
        Preconditions.checkArgument(items.remove(product), "Does not contain", product);
    }

    public int getItemsCounter() {
        return items.size();
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getOwners() throws IllegalStateException {
        Preconditions.checkState(permissions != null, "No one has permissions");
            return permissions;
    }

    public boolean isClosed() {
        return closed;
    }

    public String getOwnerName() throws IllegalStateException {
        Preconditions.checkState(permissions == null, "No one has permissions");
           {String result = "";
            StringBuilder sB = new StringBuilder(result);
            int counter = 1;
            for (Permission per : permissions) {
                counter++;
                if (counter == permissions.size())
                    sB.append(per.getClient().getName() + ", ");
                else
                    sB.append(per.getClient().getName());
            }
            result = sB.toString();
            return result;
            }
    }

    public static void mergeList(LightBox mergeLightBox, LightBox...lightBox) {
        for (LightBox lbx : lightBox) {
            for (Product product : lbx.getProducts()) {
                if (product.isAvailable() && !mergeLightBox.checkIfPictureIsRepeated(mergeLightBox, product))
                    mergeLightBox.add(product);
            }
        }
    }

    private boolean checkIfPictureIsRepeated(LightBox mergeLightBox, Product product) {
        boolean productIsRepeated = false;
        for (Product prod : mergeLightBox.getProducts()) {
            if (prod.equals(product))
                productIsRepeated = true;
        }
        return productIsRepeated;
    }

    public int checkLongestNameLength(LightBox lbx) {
        int longestNameLength = 0;
        int length = 1;
        for (Product prod : lbx.getProducts()) {
            if (prod.isAvailable()) {
                length = prod.getNumber().length();
            }
            if (length > longestNameLength)
                longestNameLength = length;
        }
        return longestNameLength;
    }

    public List<Product> getProducts() {
        return items;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LightBox lightBox = (LightBox) o;
        return closed == lightBox.closed &&
                com.google.common.base.Objects.equal(number, lightBox.number) &&
                Objects.equal(name, lightBox.name) &&
                Objects.equal(permissions, lightBox.permissions) &&
                Objects.equal(items, lightBox.items);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(number, name, permissions, items, closed);
    }

    //number,name,clientsAndClientsRole,items,closed
    public String[] export() {
        StringBuilder sBPermissions = new StringBuilder();

        String spliter = "";
        permissionsParseToString(sBPermissions, spliter);

        spliter = "";
        StringBuilder sBProducts = new StringBuilder();
        productsParseToString(spliter, sBProducts);
        return new String[] {getNumber(), getName(), sBPermissions.toString(), sBProducts.toString(), String.valueOf(isClosed())};
    }

    private void productsParseToString(String spliter, StringBuilder sBProducts) {
        for (Product prod : items) {
            String productNr = prod.getNumber();
            sBProducts.append(spliter).append(productNr);
            spliter = "|";
        }
    }

    private void permissionsParseToString(StringBuilder sBPermissions, String spliter) {
        for (Permission perm : permissions) {
            String clientNr = perm.getClient().getNumber();
            ClientRole clientRole = perm.getClientRole();

            sBPermissions.append(spliter).append(clientNr).append(" ").append(String.valueOf(clientRole));
            spliter = "|";
        }
    }
}
