package pl.com.bottega.commons.math;

import javax.jws.soap.SOAPBinding;
import static pl.com.bottega.commons.math.Fraction.ONE;
import static pl.com.bottega.commons.math.Fraction.ZERO;

/**
 * Created by Dell on 2016-03-19.
 */

public class FractionTestConsoleApp {
    public static void main(String[] args) {

        Fraction f1;
        Fraction f2;

        try {
            f1 = new Fraction(1, 2);
            f2 = new Fraction(2);
        }
        catch(IllegalArgumentException ex) {
            System.out.println("Złe dane wejściowe! " + ex.getMessage());
            return;
        }
        Fraction f3 = new Fraction(2, 3);
        Fraction f4 = new Fraction(1, 2);
        Fraction f5 = new Fraction(0, 2);

      /*  Fraction sum = ONE.add(f1);
      // Fraction sum = f1.add(f2); //wzorzec projektowy: Value Object (immutability - nie ulegający zmianie); niemutowalny
        Fraction sum2 = f3.add(f4).add(ONE);
        System.out.println(sum.toString());
        System.out.println(sum2.toString());
        System.out.println(sum.reverse());*/

        Fraction f6 = new Fraction(3, 198);
        Fraction f7 = new Fraction(234, 100034);
        Fraction f8 = new Fraction(0, 345);
        Fraction f9 = new Fraction(10, 3);
        Fraction f10 = new Fraction(103009, 100);

        System.out.println(f6);
        System.out.println(f7);
        System.out.println(f8);
        System.out.println(f9);
        System.out.println(f10);
    }
}
