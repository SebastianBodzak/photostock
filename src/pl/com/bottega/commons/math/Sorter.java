package pl.com.bottega.commons.math;

/**
 * Created by Dell on 2016-03-22.
 */
public class Sorter {

    public static int[] sort(int[] table) {
        int[] sortedTable = (int[])table.clone();
        sortInplace(sortedTable);
        return sortedTable;
    }

    public static int[] sortInplace(int[] table) {
        sortTable(table);
        return table;
    }


    private static void sortTable(int[] table) {
        int n = table.length;
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (table[j] > table[j + 1]) {
                    int swapNumber = table[j + 1];
                    table[j + 1] = table[j];
                    table[j] = swapNumber;
                }
            }
            n -= 1;
        }
    }
}
