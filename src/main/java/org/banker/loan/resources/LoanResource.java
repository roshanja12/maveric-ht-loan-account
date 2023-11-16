package org.banker.loan.resources;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.LoanDto;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.models.TransactionRequestDto;
import org.banker.loan.service.LoanServiceImp;
import org.banker.loan.service.MessagingService;
import org.banker.loan.utils.ResponseGenerator;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.PartType;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

@Path("/api/v1/loan")
@OpenAPIDefinition(info = @Info(title = "BankerApp Loan API", version = "1.0"))
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    LoanServiceImp loanService;

    @Inject
    Logger log;

    @Inject
    ResponseGenerator response;
    @Inject
    UriInfo uriInfo;

    @Inject
    MessagingService messagingService;

    @POST
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Operation(summary = "Create Loan", description = "create a new loan")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Creating a loan"),
            @APIResponse(responseCode = "204", description = "No Content")
    })
    public Response createLoan(@RestForm("supportingDoc") FileUpload file,
                               @Valid @RestForm @PartType(MediaType.APPLICATION_JSON)  LoanDto loanDto,
                               UriInfo uriInfo) {
        LoanDto responseDetails=  loanService.createLoanService(file,loanDto);
        ResponseDto responseSuccess= response.successResponseGenerator("Customer Loan Created",responseDetails,uriInfo);
        messagingService.loanProducerCustom(responseDetails);
        return Response.status(Response.Status.ACCEPTED)
                .entity(responseSuccess)
                .build();
    }

    @GET
    @Operation(summary = "Get all Loans", description = "Retrieve a list of all customer loans")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "List of loans"),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "404", description = "Resource Not found"),
            @APIResponse(responseCode = "500", description = "Internal Server error")
    })
    public Response getAllLoan(@Context UriInfo uriInfo, @QueryParam("page") int page, @QueryParam("size") int size) throws NoDataException {
        List<Loan> allLoans = loanService.getAllLoan(page, size);
            ResponseDto responseSucess = response.successResponseGenerator("list of all loans",allLoans,uriInfo);
            return Response.status(Response.Status.OK)
                    .entity(responseSucess)
                    .build();
    }
    @GET
    @Path("/loanid/{id}")
    @Operation(summary = "Get Loan by customer Id", description = "Retrieve a loan by customer id")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Retrieve of loan"),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "404", description = "Resource Not found"),
            @APIResponse(responseCode = "500", description = "Internal Server error")
    })
    public Response viewLoanById(@PathParam("id") Long id, @QueryParam("page") int page, @QueryParam("size") int size, @Context UriInfo uriInfo) throws LoanIdNotFoundException {
        List<LoanPaymentHistory> allLoansHistory = loanService.viewLoanById(id, page, size);
            ResponseDto responseSucess = response.successResponseGenerator("list of all payment history loans",allLoansHistory,uriInfo);
            return Response.status(Response.Status.OK)
                    .entity(responseSucess)
                    .build();
    }

    @PUT
    @Path("/status/{loanId}")
    @Operation(summary = "Withdraw", description = "Withdraw the amount")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Creating a loan"),
            @APIResponse(responseCode = "404", description = "Resource Not found"),
            @APIResponse(responseCode = "500", description = "Internal Server error")
    })
    public Response  status(@PathParam("loanId") Long loanId,@QueryParam("status") String status, UriInfo uriInfo)throws LoanIdNotFoundException {
        LoanStatus statusEnum = LoanStatus.valueOf(status);
        Loan resultStatus = null;
        //loanService.status(loanId,statusEnum.toString());
        ResponseDto responseSucess = response.successResponseGenerator("Status has been updated",resultStatus,uriInfo);
        return Response.status(Response.Status.OK)
                .entity(responseSucess)
                .build();
    }

    @GET
    @Path("/search")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "search", description = "Retrieve a data by name,id,email and number")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Retrieve of list of loan"),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseDto search( @Valid
            @QueryParam("searchValue") String searchValue, @QueryParam("page") int page, @QueryParam("size") int size) {
        List<Loan> list = loanService.getAllData(searchValue, page, size);
        return response.successResponseGenerator("Get Loan Details",list,uriInfo);
    }

    @PUT
    @Transactional
    @Path("/{loanId}/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "updating the status", description = "Updating the status of the loans")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "updating loan status"),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public ResponseDto statusUpdate(@Valid @PathParam("loanId") Long loanId, @PathParam("status") String status) {
        String message=loanService.status(loanId, status);
        return response.successResponseGenerator("Updated status in db",null,uriInfo);
    }


    @POST
    @Transactional
    @Path("/withdraw")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Posting the status in history", description = "Posting the status of the payment history")
    @APIResponses(value = {
            @APIResponse(responseCode = "200", description = "Posting payment history status"),
            @APIResponse(responseCode = "204", description = "No Content"),
            @APIResponse(responseCode = "500", description = "Internal Server Error")
    })
    public Response history(@Valid @RequestBody TransactionRequestDto requestDto) {
        String loanPaymentHistory=loanService.historyStatus(requestDto);
        ResponseDto responseSuccess= response.successResponseGenerator("LoanHistory Created",loanPaymentHistory,uriInfo);
        return Response.status(Response.Status.ACCEPTED)
                .entity(responseSuccess)
                .build();
    }


}
