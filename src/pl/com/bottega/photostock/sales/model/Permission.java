package pl.com.bottega.photostock.sales.model;

import com.google.common.base.Objects;

/**
 * Created by Dell on 2016-05-04.
 */
public class Permission {

    private Client client;
    private ClientRole clientRole;

    public Permission(Client client, ClientRole clientRole) {
        this.client = client;
        this.clientRole = clientRole;
    }

    public Client getClient() {
        return client;
    }

    public ClientRole getClientRole() {
        return clientRole;
    }

    public void setClientRole(ClientRole clientRole) {
        this.clientRole = clientRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return Objects.equal(client, that.client) &&
                clientRole == that.clientRole;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(client, clientRole);
    }
}
