package pl.com.bottega.photostock.sales.model;

import pl.com.bottega.photostock.sales.model.client.strategies.PayingStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Dell on 2016-03-13.
 */
public class Client {

    private String name;
    private String address;
    private Money debt;
    private Money amount;
    private Money creditLimit;
    private boolean active = true;
    private ClientStatus status;
    private String number;
//    private Set<Company> companies = new HashSet<>();
    private Company company;

    private PayingStrategy payingStrategy;

    public Client(String number, String name, String address, ClientStatus status, Money debt, Money amount, Money creditLimit, Company company, PayingStrategy payingStrategy) {
        this.number = number;
        this.name = name;
        this.address = address;
        this.status = status;
        this.debt = debt;
        this.amount = amount;
        this.creditLimit = creditLimit;
        this.company = company;
        this.payingStrategy = payingStrategy;
//        this.companies.add(company);
    }

    public Client(String name, String address, ClientStatus status, Money debt, Money amount, Money creditLimit, Company company) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.debt = debt;
        this.amount = amount;
        this.creditLimit = creditLimit;
//        this.companies.add(company);
        this.company = company;
    }

    public Client(String name, String address, ClientStatus status, Money debt, Money amount, Money creditLimit) {
        this.name = name;
        this.address = address;
        this.status = status;
        this.debt = debt;
        this.amount = amount;
        this.creditLimit = creditLimit;
    }

    public Client(String number, String name, String address, ClientStatus clientsStatus, Money debt, Money amount, Money creditLimit, boolean active) {

        this.number = number;
        this.name = name;
        this.address = address;
        this.status = clientsStatus;
        this.debt = debt;
        this.amount = amount;
        this.creditLimit = creditLimit;
        this.active = active;
    }

    public boolean canAfford(Money price) {
            return payingStrategy.canAfford(price, this);
    }

    public void charge(Money price, String cause) {
        payingStrategy.charge(price, cause, this);
    }

    public void recharge(Money quantity) {
        payingStrategy.recharge(quantity, this);
    }

    public void modifyAmount(Money newAmount) {
        amount = newAmount;
    }

    public void modifyDebt(Money newDebt) {
        debt = newDebt;
    }

    public void modifyCreditLimit(Money newCreditLimit) {
        this.creditLimit = newCreditLimit;
    }

    public Money getSaldo() {
        return amount.subtract(debt);
    }

    public PayingStrategy getPayingStrategy() {
        return payingStrategy;
    }

    public String getName() {
        return name;
    }

    public Money getAmount() {
        return amount;
    }

    public Money getCreditLimit() {
        return creditLimit;
    }

    public Money getDebt() {
        return debt;
    }

    public boolean isActive() {
        return active;
    }

    public String introduce() {
        return name + " - " + status.getPolishString(status);
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public ClientStatus getStatus() {
        return status;
    }

    public void setStatus(ClientStatus status) {
        this.status = status;
    }

    public void setPayingStrategy(PayingStrategy payingStrategy) {
        this.payingStrategy = payingStrategy;
    }

//    public void addCompanies(Company ...company) {
//        for (Company com : company) {
//            companies.add(com);
//        }
//    }

//    public void removeCompany(Company company) {
//        for (Company com : companies) {
//            if (com.equals(company))
//                companies.remove(com);
//        }
//    }

    public String getCompany() {
        return company.getName();
    }

//    public Set<Company> getCompanies() {
//        return companies;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Client client = (Client) o;

        if (name != null ? !name.equals(client.name) : client.name != null) return false;
        if (address != null ? !address.equals(client.address) : client.address != null) return false;
        if (status != client.status) return false;
        return number != null ? number.equals(client.number) : client.number == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (address != null ? address.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (number != null ? number.hashCode() : 0);
        return result;
    }

    //number,name,address,status,debt,amount,creditLimit,currency,company
    public String[] export() {
        return new String[]{getNumber(), getName(), getAddress(), String.valueOf(getStatus()), String.valueOf(getDebt().cents()), String.valueOf(getAmount().cents()),
                String.valueOf(getCreditLimit().cents()), String.valueOf(getAmount().getCurrency()), String.valueOf(getCompany()), getPayingStrategy().getName()};
    }

    public String getAddress() {
        return address;
    }

    public void setName(String newName) {
        this.name = newName;
    }
}