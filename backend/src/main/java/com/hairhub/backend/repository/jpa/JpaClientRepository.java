package com.hairhub.backend.repository.jpa;

import com.hairhub.backend.entity.Client;
import com.hairhub.backend.repository.ClientRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaClientRepository implements ClientRepository {

    private final SpringDataClientRepository springDataClientRepository;

    public JpaClientRepository(SpringDataClientRepository springDataClientRepository){
        this.springDataClientRepository = springDataClientRepository;
    }

    @Override
    public Client save(Client client) {
        return springDataClientRepository.save(client);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return springDataClientRepository.findById(id);
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return springDataClientRepository.findByEmail(email);
    }

    @Override
    public Optional<Client> findByPhone(String phone) {
        return springDataClientRepository.findByPhone(phone);
    }

    @Override
    public List<Client> findByName(String name) {
        return springDataClientRepository.findByName(name);
    }

    @Override
    public List<Client> findByIsActiveIsTrue() {
        return springDataClientRepository.findByIsActiveIsTrue();
    }

    @Override
    public List<Client> findByIsActiveIsFalse() {
        return springDataClientRepository.findByIsActiveIsFalse();
    }

    @Override
    public List<Client> findAll() {
        return springDataClientRepository.findAll();
    }
}
