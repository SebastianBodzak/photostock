package pl.com.bottega.photostock.sales.model.client.strategies;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;

/**
 * Created by Dell on 2016-04-20.
 */
public interface PayingStrategy {
    public String getName();
    public boolean canAfford(Money price, Client client);
    public void charge(Money price, String cause, Client client);
    public void recharge(Money quantity, Client client);
}
