package com.bankApi.service;

import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import com.bankApi.dto.ClientRegistrationDTO;
import com.bankApi.model.Client;
import com.bankApi.model.Credential;

/**
 * Service layer responsible for handling client business logic.
 * Manages atomic transactions for creating identity and business data.
 */
@ApplicationScoped
public class ClientService {
    /**
     * Registers a new client and their credentials in a single transaction.
     * * @param dto The registration data.
     * @return The created {@link Client} entity.
     * @throws WebApplicationException if the username or CPF already exists.
     */
    @Transactional
    public Client registerNewClient(ClientRegistrationDTO dto) {
        // Security Check: Verify if identity already exists
        if (Credential.findByUsername(dto.username()) != null) {
            throw new WebApplicationException("Username already taken", Response.Status.CONFLICT);
        }
        if (Client.findByCpf(dto.cpf()) != null) {
            throw new WebApplicationException("CPF already registered", Response.Status.CONFLICT);
        }

        // Register Client credentials
        Credential credential = new Credential();
        credential.username = dto.username();
        credential.password = BcryptUtil.bcryptHash(dto.password());
        credential.persist();

        // Register the client
        Client client = new Client();
        client.name = dto.name();
        client.cpf = dto.cpf();
        client.credential = credential;
        client.persist();

        return client;
    }

}
