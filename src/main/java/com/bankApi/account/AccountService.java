package com.bankApi.account;

import com.bankApi.client.Client;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.util.Random;

/**
 * Service layer responsible for the lifecycle of financial accounts.
 * <p>
 * Handles the generation of unique account numbers and guarantees
 * safe initialization of balances. Designed to be called within
 * broader transactional contexts (e.g., Client registration orchestration).
 * </p>
 *
 * @version 1.0
 * @since 2026-04-20
 */
@ApplicationScoped
public class AccountService {

    private static final Logger log = Logger.getLogger(AccountService.class);
    private final Random random = new Random();

    /**
     * Creates a new, zero-balance account linked to an existing client.
     * <p>
     * This method requires an active transaction to run (TxType.REQUIRED).
     * If the account generation fails, it will flag the broader transaction for rollback.
     * </p>
     *
     * @param client The {@link Client} entity that will own the new account.
     * @return The fully persisted {@link Account} with a generated unique number.
     * @throws WebApplicationException with HTTP 500 if a unique account number cannot be generated after maximum retries.
     */
    @Transactional(Transactional.TxType.REQUIRED)
    public Account createAccount(Client client) {
        log.info("Gerando conta bancária para o cliente...");

        Account account = new Account();
        account.client = client;
        account.balance = BigDecimal.ZERO;
        account.number = generateUniqueAccountNumber();

        account.persist();

        log.info("Conta vinculada com sucesso. Número: " + account.number);
        return account;
    }

    /**
     * Generates a random 6-digit account number and verifies its uniqueness
     * against the database to prevent collisions.
     *
     * @return A guaranteed unique Long representing the account number.
     */
    private Long generateUniqueAccountNumber() {
        Long newNumber;
        int attempts = 0;

        do {
            newNumber = 100000L + random.nextInt(900000);
            if (Account.findByNumber(newNumber) == null) {
                return newNumber;
            }
            attempts++;
        } while (attempts < 10);

        log.error("Falha ao gerar número de conta único após 10 tentativas.");
        throw new WebApplicationException("Error generating account number", Response.Status.INTERNAL_SERVER_ERROR);
    }
}