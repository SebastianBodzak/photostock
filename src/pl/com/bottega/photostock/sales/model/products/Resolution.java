package pl.com.bottega.photostock.sales.model.products;

/**
 * Created by Dell on 2016-04-07.
 */
public enum Resolution {
    BARDZO_MALA, MALA, SREDNIA, DUZA, BARDZO_DUZA;

    public static final String[] DICTIONARY =
            {"Bardzo mała: 640x480", "Mała: 800x600", "Średnia: 1024x768", "Duża: 1366x768", "Bardzo duża: 1920x1080"};

    public String getPolishString(Resolution resolution) {
        return DICTIONARY[resolution.ordinal()];
    }
}
