package pl.com.bottega.commons.math;

/**
 * Created by Dell on 2016-03-19.
 */
public class ExceptionTester {
    public static void main(String[] args) {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);

        Fraction f1 = new Fraction(a, b);

        try {
            Fraction freversed = f1.reverseV2();
            System.out.println("Reversed " + freversed);
        }
        catch (IllegalStateException ex) {

            System.out.println("Złe dane wejściowe! " + ex.getMessage());
            return;
        }

    }
}
