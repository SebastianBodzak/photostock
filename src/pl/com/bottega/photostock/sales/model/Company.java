package pl.com.bottega.photostock.sales.model;

/**
 * Created by Dell on 2016-05-01.
 */
public class Company {

    private String name;

    public Company(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Company company = (Company) o;

        return name != null ? name.equals(company.name) : company.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
