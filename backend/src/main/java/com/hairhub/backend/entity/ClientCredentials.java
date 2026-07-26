package com.hairhub.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "customer_credentials")
public class ClientCredentials {

    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private Client client;

    private String passwordHash;

    public ClientCredentials(){}

    public ClientCredentials(Client client, String passwordHash) {
        this.client = client;
        this.passwordHash = passwordHash;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
