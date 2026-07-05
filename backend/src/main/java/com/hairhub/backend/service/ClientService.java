package com.hairhub.backend.service;

import com.hairhub.backend.entity.Client;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.repository.ClientRepository;
import com.hairhub.backend.service.validators.PhoneValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final PhoneValidation phoneValidation;

    public ClientService(ClientRepository clientRepository, PhoneValidation phoneValidation) {
        this.phoneValidation = phoneValidation;
        this.clientRepository = clientRepository;
    }

    public Client create(Client client) {
        phoneValidation.validate(client.getPhone());
        Client saved = clientRepository.save(client);
        log.info("Client with id {} has been created", saved.getId());
        return saved;
    }

    public Client update(Client client){
        phoneValidation.validate(client.getPhone());
        Client updated = clientRepository.save(client);
        log.info("Client with id {} has been updated", client.getId());
        return updated;
    }

    public Client findById(Long id){
        return clientRepository.findById(id)
                .orElseThrow(()->{
                    log.warn("Client with id {} not found", id);
                    return new EntityNotFoundException("Client with id " + id + " not found");
                });
    }

    public List<Client> findByName(String name){
        return clientRepository.findByName(name);
    }

    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    public Client findByPhone(String phone){
        return clientRepository.findByPhone(phone)
                .orElseThrow(()->{
                    log.warn("Client with Phone {} not found", phone);
                    return new EntityNotFoundException("Client with Phone " + phone + " not found");
                });
    }

    public Client findByEmail(String email){
        return clientRepository.findByEmail(email)
                .orElseThrow(()->{
                    log.warn("Client with Email {} not found", email);
                    return new EntityNotFoundException("Client with Email " + email + " not found");
                });
    }

    public List<Client> findAllActive() {
        return clientRepository.findByIsActiveIsTrue();
    }

    public List<Client> findAllInactive() {
        return clientRepository.findByIsActiveIsFalse();
    }

    public void deactivate(Long id) {
        Client client = findById(id);
        client.setIsActive(false);
        clientRepository.save(client);
        log.info("Client with id {} has been deactivated", id);
    }
}
