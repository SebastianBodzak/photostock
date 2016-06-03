package pl.com.bottega.commons.math.probability;

import static pl.com.bottega.commons.math.probability.Probability.fromFraction;
import static pl.com.bottega.commons.math.probability.Probability.fromPercentage;

/**
 * Created by Dell on 2016-04-16.
 */
public class ProbabilityTestConsoleApp {
    public static void main(String[] args){
        shouldCreateFractionRepresentation();
//        cannotCreateFractionRepresentationIfValuteGTOne();

        shouldCreatePercentRepresentation();
//        cannotCreatePercentRepresentationIfValueGTHundred();

        shouldCalculateIfDifferentRepresentation();
        shouldEqualsZeroIfOneIsZero();
    }

    private static void shouldEqualsZeroIfOneIsZero() {

    }

    private static void shouldCalculateIfDifferentRepresentation() {
        Probability bothEvents = fromFraction(1).and(fromPercentage(50)); //popularny styl, bez zmiennych p1 i p2
        System.out.println("Wynik " + bothEvents);
    }

    private static void cannotCreatePercentRepresentationIfValueGTHundred() {
        try {
            Probability p = fromPercentage(190.5); //wyjątek
            throw new RuntimeException("powinien być wyjątek");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    private static void shouldCreatePercentRepresentation() {
        Probability p = fromPercentage(90.5);
    }

    private static void cannotCreateFractionRepresentationIfValuteGTOne() {
        try {
            Probability p = fromFraction(1.5); //powinien być wyjątek
            throw new RuntimeException("powinien być wyjątek");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    private static void shouldCreateFractionRepresentation() {
        Probability p = fromFraction(0.5);
    }
}
