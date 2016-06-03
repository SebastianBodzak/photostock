package pl.com.bottega.photostock.sales.model.exceptions;

/**
 * Created by Dell on 2016-05-14.
 */
public class DataAccessException extends RuntimeException {
    public DataAccessException(Exception e) {
        super(e);
    }

    public DataAccessException(String message) {
        super(message);
    }
}
