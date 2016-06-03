package pl.com.bottega.photostock.sales.model.client.strategies;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;

/**
 * Created by Dell on 2016-04-21.
 */

public class SilverPayingStrategy implements PayingStrategy {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean canAfford(Money price, Client client) {
        return client.getAmount().ge(price);
    }

    @Override
    public void charge(Money price, String cause, Client client) {
        if (canAfford(price, client)) {
            Money newAmount = client.getAmount().subtract(price);
            client.modifyAmount(newAmount);
        }
    }

    @Override
    public void recharge(Money quantity, Client client) {
        Money newAmount = client.getAmount().add(quantity);
        client.modifyAmount(newAmount);
    }
}
