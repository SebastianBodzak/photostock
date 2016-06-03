package pl.com.bottega.photostock.sales.model.exceptions;

/**
 * Created by Dell on 2016-04-16.
 */
public class ProductNotAvailableException extends RuntimeException {

    private String number;
    private  Class clazz;

    /**
     *
     * @param message
     * @param number businnes number
     * @param clazz thrower type
     */
    public ProductNotAvailableException(String message, String number, Class clazz) {
        super(message);
        this.number = number;
        this.clazz = clazz;
    }

    public Class getClazz() {
        return clazz;
    }

    public String getNumber() {
        return number;
    }
}
