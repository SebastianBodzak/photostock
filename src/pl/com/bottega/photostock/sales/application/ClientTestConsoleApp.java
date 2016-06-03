package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientFactory;
import pl.com.bottega.photostock.sales.model.ClientRepository;
import pl.com.bottega.photostock.sales.model.Money;
import static pl.com.bottega.photostock.sales.model.ClientStatus.*;


/**
 * Created by Dell on 2016-04-20.
 */
public class ClientTestConsoleApp {
    public static void main(String[] args) {

        shouldChargeAndRechargeSilver();
        shouldCRechargeStandard();
        shouldChargeStandard();
        shouldCRechargeVip();
        shouldChargeVip();
        shouldChargeWithDebtVip();
//        shouldChargeAndRechargeGold();
//        shouldChargeAndRechargePlatinum();
//        shouldntChargeStandard();
    }

        Client janusz = ClientFactory.create("nr1", "Księżyc", STANDARD, "Very Big Company");
        Client korowiow = ClientFactory.create("nr2", "Hell", STANDARD, "Very Big Company");
        Client java = ClientFactory.create("nr3", "Very Deep Hell", STANDARD, "Small And Totally Different Company");
        Client zegrzysław = ClientFactory.create("nr4", "secret", SILVER, null);
        Client maciej = ClientFactory.create("nr5", "NY", VIP, "Another Company");
        Client stokrotka = ClientFactory.create("nr6", "Łąka", VIP, null);
        Client zenobiusz = ClientFactory.create("nr7", "LA", VIP, null);
        Client cleopatra = ClientFactory.create("nr8", "Teby", PLATINUM, null);
        Client marylin = ClientFactory.create("nr9", "LA", GOLD, null);

    private static void shouldChargeAndRechargeSilver() {
        Client zegrzysław = ClientFactory.create("nr4", "secret", SILVER, null);

        zegrzysław.recharge(new Money(50.55));
        System.out.println(zegrzysław.getAmount());
        zegrzysław.charge(new Money(10.50), "open amount charge");
        System.out.println(zegrzysław.getAmount());
    }

    private static void shouldCRechargeStandard() {
        Client janusz = ClientFactory.create("nr1", "Księżyc", STANDARD, "Very Big Company");

        janusz.recharge(new Money(50.55));
//        repository.save(janusz);
        Money expectedAmount = new Money(50.55);

        boolean eq = expectedAmount.equals(janusz.getAmount());
        if (!eq)
            System.out.println("Coś nie tak z doładowaniem standardowego konta");
     }

    private static void shouldChargeStandard() {
        Client korowiow = ClientFactory.create("nr2", "Hell", STANDARD, "Very Big Company");

        korowiow.recharge(new Money(50));
        korowiow.charge(new Money(20), " sorry ");
        Money expectedAmount = new Money(30);

        boolean eq = expectedAmount.equals(korowiow.getAmount());
        if (!eq)
            System.out.println("Coś nie tak z obciążeniem standardowego konta");
    }

    private static void shouldCRechargeVip() {
        Client maciej = ClientFactory.create("nr5", "NY", VIP, "Another Company");


        maciej.recharge(new Money(80));
        Money expectedAmount = new Money(80);

        boolean eq = expectedAmount.equals(maciej.getAmount());
        if (!eq)
            System.out.println("Coś nie tak z doładowaniem vipowskiego konta");
    }

    private static void shouldChargeVip() {
        Client stokrotka = ClientFactory.create("nr6", "Łąka", VIP, null);


        stokrotka.recharge(new Money(100));
        stokrotka.charge(new Money(20), " sorry ");
        Money expectedAmount = new Money(80);

        boolean eq = expectedAmount.equals(stokrotka.getAmount());
        if (!eq)
            System.out.println("Coś nie tak z obciążeniem vipowskiego konta");
    }

    private static void shouldChargeWithDebtVip() {
        Client zenobiusz = ClientFactory.create("nr7", "LA", VIP, null);


        zenobiusz.recharge(new Money(10));
        zenobiusz.charge(new Money(20), " sorry ");
        Money expectedDebt = new Money(10);

        boolean eq = expectedDebt.equals(zenobiusz.getDebt());
        if (!eq)
            System.out.println("Coś nie tak z obciążeniem vipowskiego konta i z długiem");
    }

//    private static void shouldChargeAndRechargeGold() {
//        ClientRepository repository = new FakeClientRepository();
//        Client marylin = repository.load("nr9");
//
//        marylin.recharge(new Money(50));
//        marylin.charge(new Money(20), " sorry ");
//        Money expectedAmount = new Money(30);
//
//        boolean eq = expectedAmount.equals(marylin.getAmount());
//        if (!eq)
//            System.out.println("Coś nie tak z obciążeniem złotego konta");
//    }
//
//    private static void shouldChargeAndRechargePlatinum() {
//        ClientRepository repository = new FakeClientRepository();
//        Client cleopatra = repository.load("nr8");
//
//        cleopatra.recharge(new Money(80.34));
//        cleopatra.charge(new Money(20.12), " sorry ");
//        Money expectedAmount = new Money(60.22);
//
//        boolean eq = expectedAmount.equals(cleopatra.getAmount());
//        if (!eq)
//            System.out.println("Coś nie tak z obciążeniem platynowego konta");
//    }
//
//    private static void shouldntChargeStandard() {
//        ClientRepository repository = new FakeClientRepository();
//        Client java = repository.load("nr3");
//
//        java.recharge(new Money(10));
//        java.charge(new Money(30), " sorry ");
//        System.out.println("Sorry Java, you can not buy this product " + java.getAmount());
//    }
}
