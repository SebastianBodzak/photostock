package pl.com.bottega.commons.math;

/**
 * Created by Dell on 2016-03-24.
 */
public class BubbleSortTestConsoleApp2 {
    public static void main(String[] args) {
        int[] numbers;
        try {
            numbers = Utils.convert(args);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Wrong arguments!" + ex);
            return;
        }
        int[] sortedNumbers = Sorter.sort(numbers);
        Utils.display(numbers);
        Utils.display(sortedNumbers);
    }
}