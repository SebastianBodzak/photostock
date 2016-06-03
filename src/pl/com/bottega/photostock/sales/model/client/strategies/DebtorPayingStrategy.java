package pl.com.bottega.photostock.sales.model.client.strategies;

import pl.com.bottega.photostock.sales.model.Client;
import pl.com.bottega.photostock.sales.model.Money;

/**
 * Created by Dell on 2016-04-20.
 */
public class DebtorPayingStrategy implements PayingStrategy {

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public boolean canAfford(Money price, Client client) {
        Money purchasePotential = client.getAmount().add(client.getCreditLimit().subtract(client.getDebt()));
        return purchasePotential.ge(price);
    }

    @Override
    public void charge(Money price, String cause, Client client) {
        if (canAfford(price, client)){
            Money amount = client.getAmount();
            Money debt = client.getDebt();

            amount = amount.subtract(price);
            client.modifyAmount(amount);
            if (amount.lt(amount.getZero())){
                client.modifyDebt(debt.subtract(amount));
                client.modifyAmount(amount.getZero());
            }
        }
    }

    @Override
    public void recharge(Money quantity, Client client) {
        Money amount = client.getAmount();
        Money debt = client.getDebt();

        debt = debt.subtract(quantity);
        client.modifyDebt(debt);
        if (debt.lt(debt.getZero())){
            client.modifyAmount(amount.subtract(debt));
            client.modifyDebt(debt.getZero());
        }
    }
}
