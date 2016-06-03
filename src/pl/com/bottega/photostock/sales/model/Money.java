package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.commons.math.Fraction;

import java.util.Currency;
import java.util.HashMap;
import java.util.IllegalFormatException;
import java.util.Map;


/**
 * Created by Dell on 2016-03-13.
 */
public class Money {

    public enum CurrencyValues {
        AUD, BRL, CAD, CHF, CNH, CZK, DKK, EUR, GBP, HKD, HUF, ILS, INR, JPY, KRW, MXN, MYR, NOK, NZD, PLN, RUB, SEK, SGD, THB, TRY, TWD, USD, ZAR;

    }

    private final Fraction value;
    private final Currency currency;

    public Money(double fractionalValue, CurrencyValues currency) throws IllegalArgumentException { //typ String, żeby uniknąć problemów operacji na typie double (dopóki nie używamy BigDecimals)
        if (fractionalValue < 0)
            throw new IllegalArgumentException("Amount can not be less then zero");
        int integerValue = (int) fractionalValue;
        int cents = 0;
        if (fractionalValue % 1 != 0) { //w takiej formie (na obecną sytuację) dla nielicznych walut z trzema miejscami po przecinku zrobiłbym specjalnie dla nich dodatkową instrukcję
            String temporaryFractionalValue = String.valueOf(fractionalValue);
            temporaryFractionalValue = temporaryFractionalValue.substring(temporaryFractionalValue.indexOf(".") + 1);
            if (temporaryFractionalValue.length() == 1)
                cents = Integer.parseInt(temporaryFractionalValue) * 10;
            else if (temporaryFractionalValue.length() == 2)
                cents = Integer.parseInt(temporaryFractionalValue);
            else
                throw new IllegalArgumentException("Too much decimals!");
        }
        Fraction result = new Fraction((integerValue * 100) + cents, 100);
        this.value = result;
        this.currency = Currency.getInstance(String.valueOf(currency));
    }

    public Money(int integerValue, int cents, CurrencyValues currency) throws IllegalArgumentException {
        if (cents >= 100 || cents < 0 || integerValue < 0)
            throw new IllegalArgumentException("Amount can not be less then zero");
        Fraction result = new Fraction((integerValue * 100) + cents, 100);
        this.value = result;
        this.currency = Currency.getInstance(String.valueOf(currency));
    }

    public Money(double fractionalValue) throws IllegalArgumentException {
        if (fractionalValue < 0)
            throw new IllegalArgumentException("Amount can not be less then zero");
        int integerValue = (int) fractionalValue;
        int cents = 0;
        if (fractionalValue % 1 != 0) {//j.w.
            String temporaryFractionalValue = String.valueOf(fractionalValue);
            temporaryFractionalValue = temporaryFractionalValue.substring(temporaryFractionalValue.indexOf(".") + 1);
            if (temporaryFractionalValue.length() == 1)
                cents = Integer.parseInt(temporaryFractionalValue) * 10;
            else if (temporaryFractionalValue.length() == 2)
                cents = Integer.parseInt(temporaryFractionalValue);
            else
                throw new IllegalArgumentException("Too much decimals!");
        }
        Fraction result = new Fraction((integerValue * 100) + cents, 100);
        this.value = result;
        this.currency = Currency.getInstance("PLN");
    }

    private Money (Fraction value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    private Money(double fractionalValue, Currency currency) {
        int integerValue = (int) fractionalValue;
        int cents = 0;
        if (fractionalValue % 1 != 0) {
            String temporaryFractionalValue = String.valueOf(fractionalValue);
            temporaryFractionalValue = temporaryFractionalValue.substring(temporaryFractionalValue.indexOf(".") + 1);
            if (temporaryFractionalValue.length() == 1)
                cents = Integer.parseInt(temporaryFractionalValue) * 10;
            else
                cents = Integer.parseInt(temporaryFractionalValue);
        }
        Fraction result = new Fraction((integerValue * 100) + cents, 100);
        this.value = result;
        this.currency = currency;
    }

//    public CurrencyValues getCurrency(String currency) {
//        return CurrencyValues.valueOf(currency);
//    }

    public Money add(Money amount) throws IllegalArgumentException {
        if (!currency.equals(amount.currency))
            throw new IllegalArgumentException("Can not add if different currency");
        return new Money(value.add(amount.value), currency);
    }

    public Money subtract(Money amount){
        if (!currency.equals(amount.currency))
            throw new IllegalArgumentException("Can not add if different currency");
        return new Money(value.subtract(amount.value), currency);
    }

    public Money multiple(int ratio){
        double sum = value.getNumerator() * ratio;
        Money result = new Money(sum/100, currency);
        return result;
    }

    public Money multiple(double ratio){
        double sum = (value.getNumerator() * ratio) / 100;
        Money result = new Money(sum, Currency.getInstance(String.valueOf(currency)));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Money money = (Money) o;

        if (!value.equals(money.value)) return false;
        return currency.equals(money.currency);
    }

//    public boolean equals(Object m2) {
//        if (this == m2) return true;
//        if (m2 == null || getClass() != m2.getClass()) return false;
//
//        Money money2 = (Money) m2;
//        int v1 = value.getNumerator();
//        int v2 = money2.value.getNumerator();
//
//        if (this.currency.equals(money2.currency)){
//            int smaller = (v1 < v2) ? v1 : v2;
//            double delta = (smaller > 100) ? 0.001 : 0.01;
//
//            return  (Math.abs(v1 - v2) < delta);
//        }
//        return false;
//    }

    @Override
    public int hashCode() {
        int result = value.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }

    /**
     *
     * @param val
     * @return true if this is grater or equals than val
     */
    public boolean ge(Money val) {
        return value.ge(val.value);
    }

    /**
     *
     * @param val
     * @return true if this is less or equals than val
     */
    public boolean le(Money val) {
        return value.le(val.value);
    }

    /**
     *
     * @param val
     * @return  true if this is less than val
     */
    public boolean lt(Money val) {
        return value.lt(val.value);
    }

    /**
     *
     * @param val
     * @return  true if this is greater than val
     */
    public boolean gt(Money val) {
        return value.gt(val.value);
    }

    public Money getZero(){
        return new Money(new Fraction(0, 1), currency);
    }

    public Fraction getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        double sum = value.getNumerator();
        return sum/100 + " " + currency.getCurrencyCode();
    }

    public int cents() {
        return value.getNumerator();
    }
}