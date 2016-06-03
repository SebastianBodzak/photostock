package pl.com.bottega.commons.math;


/**
 * Created by Dell on 2016-03-22.
 */
public class BubbleSortTestConsoleApp {
    public static void main(String[] args) {
        int[] numbers;
        try {
            numbers = Utils.convert(args);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Wrong arguments!" + ex);
            return;
        }
        Sorter.sortInplace(numbers);
        Utils.display(numbers);
    }

}