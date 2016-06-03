package pl.com.bottega.commons.math;

/**
 * Created by Dell on 2016-03-24.
 */
public class Utils {
    public static int[] convert(String[] args) throws NumberFormatException {
        int[] numbers = new int[args.length];
        for (int i =0; i < args.length; i++ ) {
            try {
                numbers[i] = Integer.parseInt(args[i]);
            }
            catch (NumberFormatException nfe) {
                throw new IllegalArgumentException ("Złe dane wejściowe" + nfe.getMessage());
            }
        }
        return numbers;
    }

    public static void display(int[] table) {
        for(int i = 0; i<table.length; i++)
            System.out.print(table[i]+" ");
        System.out.println();
    }
}
