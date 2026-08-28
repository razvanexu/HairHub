package com.hairhub.backend.controller;

import com.hairhub.backend.dto.ClientCreateDTO;
import com.hairhub.backend.dto.ClientResponseDTO;
import com.hairhub.backend.entity.Client;
import com.hairhub.backend.mapper.ClientMapper;
import com.hairhub.backend.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;

    public ClientController(ClientService clientService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.clientMapper = clientMapper;
    }

    @PostMapping
    public ResponseEntity<ClientResponseDTO> postClient(@Valid @RequestBody ClientCreateDTO dto) {
        Client saved = clientService.create(dto);
        return ResponseEntity.status(201).body(clientMapper.toClientDTO(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id) {
        Client client = clientService.findById(id);
        ClientResponseDTO clientResponseDTO = clientMapper.toClientDTO(client);
        return ResponseEntity.ok(clientResponseDTO);
    }

    @GetMapping()
    public ResponseEntity<List<ClientResponseDTO>> searchClients(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String email) {
        List<ClientResponseDTO> foundClients = clientService
                .search(name, phone, email)
                .stream()
                .map(clientMapper::toClientDTO)
                .toList();
        return ResponseEntity.ok(foundClients);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ClientResponseDTO>> findAllActive() {
        List<ClientResponseDTO> activeClients = clientService
                .findAllActive()
                .stream()
                .map(clientMapper::toClientDTO)
                .toList();
        return ResponseEntity.ok(activeClients);
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<ClientResponseDTO>> findAllInActive() {
        List<ClientResponseDTO> inActiveClients = clientService
                .findAllInactive()
                .stream()
                .map(clientMapper::toClientDTO)
                .toList();
        return ResponseEntity.ok(inActiveClients);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(@Valid @RequestBody ClientCreateDTO dto,
                                                          @PathVariable Long id) {
        Client existing = clientService.findById(id);
        clientMapper.updateClient(existing, dto);
        Client updated = clientService.update(existing);
        return ResponseEntity.ok(clientMapper.toClientDTO(updated));
    }

}
