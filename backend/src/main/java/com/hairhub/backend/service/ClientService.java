package com.hairhub.backend.service;

import com.hairhub.backend.dto.ClientCreateDTO;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.exceptions.EntityNotFoundException;
import com.hairhub.backend.mapper.ClientMapper;
import com.hairhub.backend.repository.ClientRepository;
import com.hairhub.backend.service.validators.PhoneValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class ClientService {
    private static final String NOT_FOUND_SUFFIX = " not found";
    private final ClientRepository clientRepository;
    private final PhoneValidation phoneValidation;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, PhoneValidation phoneValidation, ClientMapper clientMapper) {
        this.phoneValidation = phoneValidation;
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    public Client create(ClientCreateDTO clientCreateDTO) {
        phoneValidation.validate(clientCreateDTO.phone());
        Client client = clientMapper.toClient(clientCreateDTO);
        Client saved = clientRepository.save(client);
        log.info("[create] Client with id {} has been created", saved.getId());
        return saved;
    }

    public Client update(Client client) {
        phoneValidation.validate(client.getPhone());
        Client updated = clientRepository.save(client);
        log.info("[update] Client with id {} has been updated", client.getId());
        return updated;
    }

    public Client findById(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("[findById] Client with id {} not found", id);
                    return new EntityNotFoundException("Client with id " + id + NOT_FOUND_SUFFIX);
                });
    }

    public List<Client> findByName(String name) {
        List<Client> namedClients = clientRepository.findByName(name);
        log.debug("[findByName] Found {} client(s)", namedClients.size());
        return namedClients;
    }

    public List<Client> findAll() {
        List<Client> allClients = clientRepository.findAll();
        log.debug("[findAll] Found {} client(s)", allClients.size());
        return allClients;
    }

    public Client findByPhone(String phone) {
        return clientRepository.findByPhone(phone)
                .orElseThrow(() -> {
                    log.warn("[findByPhone] Client with Phone {} not found", phone);
                    return new EntityNotFoundException("Client with Phone " + phone + NOT_FOUND_SUFFIX);
                });
    }

    public Client findByEmail(String email) {
        return clientRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("[findByEmail] Client with Email {} not found", email);
                    return new EntityNotFoundException("Client with Email " + email + NOT_FOUND_SUFFIX);
                });
    }

    public List<Client> findAllActive() {
        List<Client> activeClients = clientRepository.findByIsActiveIsTrue();
        log.debug("[findAllActive] Found {} client(s)", activeClients.size());
        return activeClients;
    }

    public List<Client> findAllInactive() {
        List<Client> inactiveClients = clientRepository.findByIsActiveIsFalse();
        log.debug("[findAllInactive] Found {} client(s)", inactiveClients.size());
        return inactiveClients;
    }

    public void deactivate(Long id) {
        Client client = findById(id);
        client.setIsActive(false);
        clientRepository.save(client);
        log.info("[deactivate] Client with id {} has been deactivated", id);
    }

    public List<Client> search(String name, String phone, String email) {
        if (name != null) {
            return findByName(name);
        }
        if (phone != null) {
            return Collections.singletonList(findByPhone(phone));
        }
        if (email != null) {
            return Collections.singletonList(findByEmail(email));
        }
        return findAll();
    }
}
