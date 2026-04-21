package com.bankApi.account;

import com.bankApi.account.dto.StatementResponseDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST API Endpoints for Account-focused operations.
 */
@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountResource {

    @Inject
    AccountService accountService;

    /**
     * Retrieves the complete bank statement for an account.
     * @param number The account number passed in the URL.
     * @return 200 OK with the statement (StatementResponseDTO).
     */
    @GET
    @Path("/{number}/statement")
    public Response getStatement(@PathParam("number") Long number) {

        StatementResponseDTO statement = accountService.getStatement(number);

        return Response.status(Response.Status.OK).entity(statement).build();
    }
}