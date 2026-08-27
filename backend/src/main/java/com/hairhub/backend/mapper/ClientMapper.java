package com.hairhub.backend.mapper;

import com.hairhub.backend.dto.ClientCreateDTO;
import com.hairhub.backend.dto.ClientResponseDTO;
import com.hairhub.backend.entity.Client;
import org.springframework.stereotype.Component;

@Component
public class ClientMapper {

    public Client toClient(ClientCreateDTO dto) {
        Client client = new Client(dto.name(), dto.phone(), dto.email());
        client.setIsActive(true);
        return client;
    }

    public ClientResponseDTO toClientDTO(Client client) {
        return new ClientResponseDTO(client.getId(), client.getName(),
                client.getPhone(), client.getEmail(), client.getIsActive());
    }

    public void updateClient(Client existing, ClientCreateDTO dto) {
        existing.setName(dto.name());
        existing.setPhone(dto.phone());
        existing.setEmail(dto.email());
    }
}
