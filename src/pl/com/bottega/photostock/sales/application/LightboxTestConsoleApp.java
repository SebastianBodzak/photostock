package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.model.*;
import pl.com.bottega.photostock.sales.model.exceptions.ProductNotAvailableException;
import pl.com.bottega.photostock.sales.model.products.Picture;

import java.util.ArrayList;

import static pl.com.bottega.photostock.sales.model.products.Resolution.DUZA;
import static pl.com.bottega.photostock.sales.model.products.Resolution.MALA;

/**
 * Created by Dell on 2016-03-19.
 */
public class LightboxTestConsoleApp {
    public static void main(String[] args) {
        ClientRepository repository = new FakeClientRepository();

        Client janusz = repository.load("nr1");
        Client wieslaw = repository.load("nr2");
        janusz.recharge(new Money(22.00));
//        repository.save(janusz);
        LightBox lightboxJanusz = new LightBox(janusz, "random");
        lightboxJanusz.addUser(wieslaw, ClientRole.GUEST);
        LightBox lightBoxJanusz2 = new LightBox(janusz, "basement");
        LightBox lightBoxClone = lightBoxJanusz2.cloneLightBox(janusz, lightBoxJanusz2);

        Picture lumberJack = new Picture("lumberjack (2)", new Money(2), null, false, null, DUZA);
        Picture kitty = new Picture("kitty (2)", new Money(2), null);
        Picture kitty2 = new Picture("kitty (2)", new Money(2), null);
        Picture computer = new Picture("zdjęcie powtarzające się", new Money(4), null);
        Picture tree = new Picture("tree (1)", new Money(1), null);
        Picture lighting = new Picture("nr nieaktywny", new Money(2), null, false, null, MALA);
        Picture cellar = new Picture("cellar (3)", new Money(3), null);
        Picture door = new Picture("door (8)", new Money(8), null);


        try {
//            lightboxJanusz.add(lumberJack);
            //lightboxJanusz.getClose();
            lightboxJanusz.add(kitty);
            //lightboxJanusz.add(lumberJack);
            lightboxJanusz.add(computer);
            lightboxJanusz.add(tree);
            //lightboxJanusz.getClose();

//            lightBoxJanusz2.add(lighting);
//            lightBoxJanusz2.add(lighting);
            lightBoxJanusz2.add(cellar);
            lightBoxJanusz2.add(door);
            lightBoxJanusz2.add(computer);

            lightBoxClone.add(tree);
        }

        catch (IllegalStateException ex) {
            System.out.println("Don't touch this again " + ex);
        }
        catch (IllegalArgumentException ex) {
            System.out.println("You cannot add the same picture twice, try again " + ex);
        }
        catch (ProductNotAvailableException ex) {
            System.out.println(ex.getMessage() + " " + ex.getNumber() + " " + ex.getClazz());
        }
        /*finally {
            System.out.println("Fajnie, że żyjesz");
        }*/

        int count = lightboxJanusz.getItemsCounter();
        System.out.println("Ilość elementów " + count);
        int count2 = lightBoxJanusz2.getItemsCounter();
        System.out.println("Ilość elementów " + count2);

        ArrayList<LightBox> lightBoxes = new ArrayList<>();
        Utils.addPictureFromLightBoxes(lightBoxes, lightboxJanusz, lightBoxJanusz2);
        Utils.displayItems(lightBoxes);

        Utils.displayAllItems(janusz, lightBoxClone);

        LightBox mergeLightbox = new LightBox(janusz, "Merge");
        mergeLightbox.mergeList(mergeLightbox, lightboxJanusz, lightBoxJanusz2);
        Utils.displayAllItems(janusz, mergeLightbox);


    }
}
