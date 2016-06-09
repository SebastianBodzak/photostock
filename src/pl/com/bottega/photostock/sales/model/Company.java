package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Objects;

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
        return Objects.equal(name, company.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }
}
