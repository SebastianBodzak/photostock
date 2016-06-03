package pl.com.bottega.photostock.sales.model.exceptions;

/**
 * Created by Dell on 2016-04-20.
 */
public class DataDoesNotExistsException extends RuntimeException {

    private String nameOrNumber;
    private Class clazz;

    public DataDoesNotExistsException(String message, String nameOrNumber, Class clazz) {
        super (message);
        this.nameOrNumber = nameOrNumber;
        this.clazz = clazz;
    }

    public String getNameOrNumber() {
        return nameOrNumber;
    }

    public Class getClazz() {
        return clazz;
    }
}
