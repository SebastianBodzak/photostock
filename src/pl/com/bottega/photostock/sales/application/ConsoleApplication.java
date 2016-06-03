package pl.com.bottega.photostock.sales.application;

import pl.com.bottega.photostock.sales.infrastructure.repositories.FakeClientRepository;
import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.ClientRepository;
import pl.com.bottega.photostock.sales.model.ClientStatus;
import pl.com.bottega.photostock.sales.model.Money;

import static pl.com.bottega.photostock.sales.model.Money.CurrencyValues.*;
/**
 * Created by Dell on 2016-03-13.
 */
public class ConsoleApplication {

    public static void main(String[] args) {
        ClientRepository repository = new FakeClientRepository();


        Client panJanusz = repository.load("nr1");
        panJanusz. recharge(new Money(20.65, PLN));
        repository.save(panJanusz);
        System.out.println("Janusz " + panJanusz.getSaldo().toString());
        panJanusz.charge(new Money(6.71), "za coś");
        System.out.println("Janusz " + panJanusz.getSaldo().toString());
        panJanusz.charge(new Money(10), "za coś");
        System.out.println("Janusz " + panJanusz.getSaldo().toString());

        Client maciej = repository.load("nr5");
        maciej.recharge(new Money(15));
        System.out.println("Maciej" + maciej.getSaldo());
        maciej.charge(new Money(50), "za coś");
        System.out.println("Maciej saldo: " + maciej.getSaldo());
        System.out.println("Maciej debt: " + maciej.getDebt());

        Client stokrotka = repository.load("nr6");
        stokrotka.recharge(new Money(22));
        System.out.println("Stokrotka" + stokrotka.getSaldo() + " amount " + stokrotka.getAmount() + " debt " + stokrotka.getDebt());

        Client zenobiusz = repository.load("nr7");
        zenobiusz.recharge(new Money(4));
        System.out.println("Zenobiusz" + zenobiusz.getSaldo());
        zenobiusz.charge(new Money(18), "za coś");
        System.out.println("Zenobiusz" + zenobiusz.getSaldo());
        zenobiusz.charge(new Money(2), "za coś");
        System.out.println("Zenobiusz" + zenobiusz.getSaldo());
    }
}
