package pl.com.bottega.photostock.sales.model;

import java.util.ArrayList;

/**
 * Created by Dell on 2016-03-31.
 */
public class Utils {
    public static void displayItems(ArrayList<LightBox> lightBoxes) {
            int n = 1;
            int lightBoxesSize = lightBoxes.size();
            for (LightBox lbx : lightBoxes) {
                System.out.println(n + ". " + lbx.getName() + " - " + lbx.getOwnerName());
                int longestNameLength = lbx.checkLongestNameLength(lbx);
                for (Product prod : lbx.getProducts()) {
                    if (prod.isAvailable()) {
                        System.out.println(prod.getNumber() + printLine(prod, longestNameLength) + prod.getPrice());
                    } else {
                        System.out.println("X" + printLine(prod, longestNameLength) + prod.getPrice());
                    }
                }
                if (n < lightBoxesSize)
                    System.out.println("========================");
                n++;
            }
    }

    public static void displayAllItems(Client owner, LightBox lightBoxWithAllItems) {
        System.out.println("\nContent of all your lightboxes " + owner.getName() + ":");
        for (Product prod : lightBoxWithAllItems.getProducts()) {
            int longestNameLength = lightBoxWithAllItems.checkLongestNameLength(lightBoxWithAllItems);
            System.out.println(prod.getNumber() + printLine(prod, longestNameLength) + prod.getPrice());
        }
    }

    private static String printLine(Product prod, int longestNameLength) {
        String splitLine = "";
        int length = 1;
        StringBuilder sB = new StringBuilder(splitLine);
        if (prod.isAvailable())
            length = prod.getNumber().length();
        for (int i = 0; i < (longestNameLength - length); i++) {
            sB.append(" ");
        }
        sB.append(" | ");
        splitLine = sB.toString();
        return splitLine;
    }

    public static void addPictureFromLightBoxes (ArrayList<LightBox> lightBoxes, LightBox ...lightBox) {
        for (int i = 0; i < lightBox.length; i++) {
            lightBoxes.add(lightBox[i]);
        }
    }
}
