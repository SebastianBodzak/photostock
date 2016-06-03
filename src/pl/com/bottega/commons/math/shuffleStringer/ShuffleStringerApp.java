package pl.com.bottega.commons.math.shuffleStringer;

import java.io.*;
import java.util.Scanner;

/**
 * Created by Dell on 2016-04-11.
 */
public class ShuffleStringerApp {
    public static void main(String[] args) throws IOException {
        String str = "Drogi Marszałku, Wysoka Izbo. PKB rośnie. Różnorakie 34432444 i unowocześniania dalszych 1234567890 kierunków postępowego wychowania. \n W ten sposób wdrożenie nowych, lepszych rozwiązań pomaga w tym zakresie powoduje docenianie wag dalszych \n poczynań. Nie damy się. W ten sposób rozszerzenie naszej działalności przedstawia interpretującą \n próbę sprawdzenia odpowiednich warunków administracyjno-finansowych.";
        StringShuffler stringShuffler = new StringShuffler(str, " .\"\'_-,?!|{}()\uFEFF", true);
        String shuffler = stringShuffler.tokensShuffle();
        display(shuffler);

        System.out.println("\n\n THIS TEXT IS FROM FILE");

        String fileContent = readFile("C:/Text example.txt");
        System.out.println(fileContent);

        StringShuffler fileShuffler = new StringShuffler(fileContent, " .\"\'_-,?!|{}()﻿", true);
        String shuffleTextFromFile = fileShuffler.tokensShuffle();
        display(shuffleTextFromFile);
        saveFile("D:/sample.txt", shuffleTextFromFile);
    }



    private static void display(String text) {
        System.out.print(text);
    }

    private static String readFile(String filePathAndName) {
        File file = new File(filePathAndName);
        String fileContent = "";
        try (Scanner scanner = new Scanner(file)) { //automatyczne zamykanie pliku
            while (scanner.hasNextLine()) {
                fileContent += scanner.nextLine() + "\n ";
            }
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find file");
        }
        return fileContent;
    }

    private static void saveFile(String filePathAndName, String textForSaving) {
        try (PrintWriter newFile = new PrintWriter(filePathAndName)) {
            newFile.println(textForSaving);
        } catch (FileNotFoundException ex) {
            System.out.println("We wanted the best, but it turned out like always");
        }
    }
}
