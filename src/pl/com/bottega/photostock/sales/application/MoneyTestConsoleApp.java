package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.model.Money;
import static pl.com.bottega.photostock.sales.model.Money.CurrencyValues.*;

/**
 * Created by Dell on 2016-04-12.
 */
public class MoneyTestConsoleApp {
    public static void main(String[] args) {
        shouldConstruct();

        shouldEqualsIfSameCurrency();
        canNotEqualsIfDifferentCurrency();

        shouldAddHugeNumbers();
        shouldAddSmallNumbers();
        try {
            canNotAddIfDifferentCurrency();
        }
        catch (IllegalArgumentException ex) {
            System.out.println("You have used different currency, so result is very good");
        }

        shouldMultipleByTinyRatio();
        shouldMultipleBYLargeRatio();

    }

    private static void shouldConstruct() {
        new Money(10.5, PLN);
        new Money(10, 50, PLN);
    }

    private static void shouldEqualsIfSameCurrency() {
        Money m1 = new Money(10.5, PLN);
        Money m2 = new Money(10, 50, PLN);

        boolean eq = m1.equals(m2);
        if (!eq)
            System.out.println("coś nie tak z porównywaniem");
    }

    private static void canNotEqualsIfDifferentCurrency() {
        Money m1 = new Money(10.5, PLN);
        Money m2 = new Money(10, 50, EUR);

        boolean eq = m1.equals(m2);
        if (eq)
            System.out.println("coś nie tak z porównywaniem");
    }

    private static void shouldAddHugeNumbers() {
        Money m1 = new Money(100_000_000d, PLN);
        Money m2 = new Money(300_000_000d, PLN);

        Money expectedSum = new Money(400_000_000d, PLN);

        Money sum = m1.add(m2);

        boolean eq = expectedSum.equals(sum);
        if (!eq)
            System.out.println("Coś nie tak z dodawaniem dużych liczb");
    }

    private static void shouldAddSmallNumbers() {
        Money m1 = new Money(0.32, PLN);
        Money m2 = new Money(1.33, PLN);

        Money expectedSum = new Money(1.65, PLN);

        Money sum = m1.add(m2);

        boolean eq = expectedSum.equals(sum);
        if (!eq)
            System.out.println("Coś nie tak z dodawaniem małych liczb");

    }

    private static void canNotAddIfDifferentCurrency() {
        Money m1 = new Money(10, 50, PLN);
        Money m2 = new Money(10, 50, EUR);

        Money expectedSum = new Money(21, PLN);

        Money sum = m1.add(m2);

        boolean eq = expectedSum.equals(sum);
        if (eq)
            System.out.println("coś nie tak z porównywaniem walut");
    }

    private static void shouldMultipleByTinyRatio() {
        Money m1 = new Money(100.10);

        Money expextedSum = new Money(110.11);
        Money sum = m1.multiple(1.1);

        boolean eq = expextedSum.equals(sum);
        if (!eq)
            System.out.println("Coś nie tak z mnożeniem na małych liczbach");
    }

    private static void shouldMultipleBYLargeRatio() {
        Money m1 = new Money(100.22);

        Money expextedSum = new Money(400.88);
        Money sum = m1.multiple(4);

        boolean eq = expextedSum.equals(sum);
        if (!eq)
            System.out.println("Coś nie tak z mnożeniem na dużych liczbach");

    }
}
