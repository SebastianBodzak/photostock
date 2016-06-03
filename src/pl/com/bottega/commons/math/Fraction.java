package pl.com.bottega.commons.math;

/**
 * Created by Dell on 2016-03-19.
 */
public class Fraction {
    private final int numerator;
    private final int denominator;
    public static final Fraction ONE = new Fraction(1, 1);
    public static final Fraction ZERO = new Fraction(0, 1);

    /**
     *
     * Klasa modeluje ułamek
     *
     * @param numerator licznik ułamka
     * @param denominator mianownik ułamka
     *
     * @throws IllegalArgumentException gdy mianownik jest równy 0
     */

    public Fraction(int numerator, int denominator) {
        if (denominator == 0)
            throw new IllegalArgumentException("Denominator cannot be zero");


        this.numerator = numerator;
        this.denominator = denominator;
        //todo obsługa zerowego mianownika
    }

    public Fraction(int numerator) {
        /*this(numerator, 0); nie chcemy walidować (poprawiać) liczby 10*/
        this.numerator = numerator;
        this.denominator = 10;
    }

    /**
     *
     * @param literal ułamek w reprezentacji licznik/mianownik, np. 3/4
     */
    public Fraction(String literal) {
        String[] parts = literal.split("/");
        if (parts.length != 2)
            throw new IllegalArgumentException("To nie jest ułamek!");

        try {
            this.numerator = Integer.parseInt(parts[0]);
            this.denominator = Integer.parseInt(parts[1]);
            if (this.denominator == 0)
                throw new IllegalArgumentException("Zero w mianowniku");
        }
        catch (NumberFormatException ex) { //nie musimy przepakować tego, możemy rzucić NFE
            throw new IllegalArgumentException("To nie jest ułamek", ex);
        }
    }

    public Fraction add(Fraction addend) {
       if (denominator == addend.denominator) {
           int numeratorSum = numerator + addend.numerator;
           return new Fraction(numeratorSum, denominator);
       } else {
           int commonDenominator = denominator * addend.denominator;
           int thisNominator = numerator * addend.denominator;
           int addendNominator = addend.numerator * denominator;
           int numeratorSum = thisNominator + addendNominator;

           return new Fraction(numeratorSum, commonDenominator);
       }
    }

    public Fraction subtract(Fraction subtrahend) {
        if (denominator == subtrahend.denominator) {
            int numeratorDifferential = numerator - subtrahend.numerator;
            return new Fraction(numeratorDifferential, denominator);
        } else {
            int commonDenominator = denominator * subtrahend.denominator;
            int thisNominator = numerator * subtrahend.denominator;
            int subtrahendNominator = subtrahend.numerator * denominator;
            int numeratorDifferential = thisNominator - subtrahendNominator;

            return new Fraction(numeratorDifferential, commonDenominator);
        }
    }

    @Override
    public String toString() {
        if ((numerator % denominator) != 0) {
            return setFirstLine() + "\n" + setSecondLine() + "\n" + setThirdLine();
        } else {
            return Integer.toString(setWholeNumber());
        }
    }

    public Fraction reverseV2() throws IllegalStateException {
        if (this.numerator == 0)
            throw new IllegalStateException("Zero cannot be reserved");
        return new Fraction(this.denominator, this.numerator);
    }

    public Fraction reverse() throws IllegalStateException {
        try{
            return new Fraction(this.denominator, this.numerator);
        }
        catch(IllegalArgumentException ex) {
            throw new IllegalStateException("Zero cannot be resereved", ex);
        }
    }

    private String setFirstLine() {
        if (ifWholeNumberExist()) {
            int newNumerator;
            newNumerator = numerator % denominator;
            return spaceEqualsWholeNumberLength() + numeratorSpace(newNumerator) + newNumerator;
        } else {
            return spaceEqualsWholeNumberLength() + numeratorSpace(numerator) + numerator;
        }
    }

    private String setSecondLine() {
        if (setWholeNumber() == 0)
            return addFractionLine();
        return setWholeNumber() + addFractionLine() ;
    }

    private String setThirdLine() {
        return spaceEqualsWholeNumberLength() + denominator;
    }

    private int setWholeNumber() {
        int wholeNumber;
        wholeNumber = numerator/denominator;

        return wholeNumber;
    }

    private boolean ifWholeNumberExist() {
        boolean wholeNumberExist = false;
        if(numerator > denominator)
            wholeNumberExist = true;
        return wholeNumberExist;
    }

    private String spaceEqualsWholeNumberLength() {
        String spaceNumerator = "";
        if (ifWholeNumberExist()) {
            String setWholeNumber = Integer.toString(setWholeNumber());
            for (int i = 0; i < setWholeNumber.length(); i++) {
                spaceNumerator += " ";
            }
        }
        return spaceNumerator;
    }

    private String addFractionLine() {
        String fractionLine = "";
        int denominatorLength = denominatorLength();
        for (int i = 0; i < denominatorLength; i++) {
            fractionLine += "-";
        }
        return fractionLine;
    }

    private int denominatorLength() {
        String stringDenominator = Integer.toString(denominator);
        int denominatorLength = stringDenominator.length();
        return denominatorLength;
    }

    private String numeratorSpace(int newNumerator) {
        String numeratorSpace = "";
        String newNumeratorString = Integer.toString(newNumerator);
        int newNumeratorLength = newNumeratorString.length();
        if (newNumeratorLength < denominatorLength()) {
            for (int i = newNumeratorLength; i < denominatorLength(); i++) {
                if (i % 2 == 0)
                    numeratorSpace += " ";
            }
        }
        return numeratorSpace;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fraction fraction = (Fraction) o;

        if (numerator != fraction.numerator) return false;
        return denominator == fraction.denominator;
        //1/2 != 2/4 hashcode też do poprawy
    }

    @Override
    public int hashCode() {
        int result = numerator;
        result = 31 * result + denominator;
        return result;
    }

    public boolean ge(Fraction value) {
        boolean result = false;
        if (denominator == value.denominator) {
            if (numerator >= value.numerator)
               result = true;
        } else {
            int thisNominator = numerator * value.denominator;
            int valueNominator = value.numerator * denominator;
            if (thisNominator >= valueNominator)
                result = true;
        }
        return result;
    }

    public boolean le(Fraction value) {
        boolean result = false;
        if (denominator == value.denominator) {
            if (numerator <= value.numerator)
                result = true;
        } else {
            int thisNominator = numerator * value.denominator;
            int valueNominator = value.numerator * denominator;
            if (thisNominator <= valueNominator)
                result = true;
        }
        return result;
    }

    public boolean lt(Fraction value) {
        boolean result = false;
        if (denominator == value.denominator) {
            if (numerator < value.numerator)
                result = true;
        } else {
            int thisNominator = numerator * value.denominator;
            int valueNominator = value.numerator * denominator;
            if (thisNominator < valueNominator)
                result = true;
        }
        return result;
    }

    public boolean gt(Fraction value) {
        boolean result = false;
        if (denominator == value.denominator) {
            if (numerator > value.numerator)
                result = true;
        } else {
            int thisNominator = numerator * value.denominator;
            int valueNominator = value.numerator * denominator;
            if (thisNominator > valueNominator)
                result = true;
        }
        return result;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }
}