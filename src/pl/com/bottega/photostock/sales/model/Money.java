package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
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

    /**
     * String type for avoiding double problems
     * Does not support three decimals currency format
     * @param fractionalValue
     * @param currency
     * @throws IllegalArgumentException
     */
    public Money(double fractionalValue, CurrencyValues currency) throws IllegalArgumentException {
        Preconditions.checkArgument(!(fractionalValue < 0), "Amount can not be less then zero");
        int integerValue = (int) fractionalValue;
        int cents = 0;
        if (fractionalValue % 1 != 0) {
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

    /**
     * String type for avoiding double problems
     * Does not support three decimals currency format
     * @param integerValue
     * @param cents
     * @param currency
     * @throws IllegalArgumentException
     */
    public Money(int integerValue, int cents, CurrencyValues currency) throws IllegalArgumentException {
        Preconditions.checkArgument(!(cents >= 100 || cents < 0 || integerValue < 0), "Amount can not be less then zero");
        Fraction result = new Fraction((integerValue * 100) + cents, 100);
        this.value = result;
        this.currency = Currency.getInstance(String.valueOf(currency));
    }


    /**
     * String type for avoiding double problems
     * Does not support three decimals currency format
     * @param fractionalValue
     * @throws IllegalArgumentException
     */
    public Money(double fractionalValue) throws IllegalArgumentException {
        Preconditions.checkArgument(!(fractionalValue < 0), "Amount can not be less then zero");
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

    public Money add(Money amount) throws IllegalArgumentException {
        Preconditions.checkArgument(currency.equals(amount.currency), "Can not add if different currency", amount);
        return new Money(value.add(amount.value), currency);
    }

    public Money subtract(Money amount) throws IllegalArgumentException{
        Preconditions.checkArgument(currency.equals(amount.currency), "Can not substract if different currency", amount);
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equal(value, money.value) &&
                Objects.equal(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value, currency);
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