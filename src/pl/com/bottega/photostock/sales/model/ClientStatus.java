package pl.com.bottega.photostock.sales.model;

/**
 * Created by Dell on 2016-04-03.
 */
public enum ClientStatus {
    STANDARD, GOLD, SILVER, PLATINUM, VIP;

    public static final String[] DICTIONARY = {"Standardowy", "Złoty", "Srebrny", "Platynowy", "VIP"};

    public String getPolishString(ClientStatus status) {
        return DICTIONARY[status.ordinal()];
    }
}
