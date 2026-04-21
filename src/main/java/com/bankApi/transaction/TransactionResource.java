package com.bankApi.transaction;

import com.bankApi.transaction.dto.*;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API Endpoints for financial operations.
 */
@Path("/transactions")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransactionResource {

    @Inject
    TransactionService transactionService;

    /**
     * Executes an atomic transfer between two accounts.
     * @param dto The validated transfer payload.
     * @return 201 Created with the clean receipt (TransactionResponseDTO).
     */
    @POST
    @Path("/transfer")
    public Response transfer(@Valid TransferRequestDTO dto) {

        Transaction transaction = transactionService.transfer(
                dto.originAccountNumber(),
                dto.destinationAccountNumber(),
                dto.amount()
        );
        TransferResponseDTO receipt = new TransferResponseDTO(
                transaction.id,
                transaction.originAccount.number,
                transaction.destinationAccount.number,
                transaction.value,
                transaction.date,
                transaction.type.name()
        );

        return Response.status(Response.Status.CREATED).entity(receipt).build();
    }

    /**
     * Executes a deposit.
     * @param dto The validated transfer payload.
     * @return 201 Created with the clean receipt (TransactionResponseDTO).
     */
    @POST
    @Path("/deposit")
    public Response deposit(@Valid DepositRequestDTO dto) {

        Transaction transaction = transactionService.deposit(
                dto.destinationAccountNumber(),
                dto.amount()
        );

        DepositResponseDTO receipt = new DepositResponseDTO(
                transaction.id,
                transaction.destinationAccount.number,
                transaction.value,
                transaction.date,
                transaction.type.name()
        );

        return Response.status(Response.Status.CREATED).entity(receipt).build();
    }

    /**
     * Executes an ATM cash withdrawal.
     * @return 201 Created with the clean receipt.
     */
    @POST
    @Path("/withdraw")
    public Response withdraw(@Valid WithdrawalRequestDTO dto) {

        Transaction transaction = transactionService.withdraw(
                dto.originAccountNumber(),
                dto.amount()
        );

        WithdrawalResponseDTO receipt = new WithdrawalResponseDTO(
                transaction.id,
                transaction.originAccount.number,
                transaction.value,
                transaction.date,
                transaction.type.name()
        );

        return Response.status(Response.Status.CREATED).entity(receipt).build();
    }
}