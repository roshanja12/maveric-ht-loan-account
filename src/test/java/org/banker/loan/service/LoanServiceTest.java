package org.banker.loan.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanSupportingDocument;
import org.banker.loan.exception.ErrorCodes;
import org.banker.loan.exception.ServiceException;
import org.banker.loan.models.LoanDto;
import org.banker.loan.proxylayer.*;
import org.banker.loan.repository.LoanHistoryRepository;
import org.banker.loan.repository.LoanRepository;
import org.banker.loan.utils.CommonUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.wildfly.common.Assert;

import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.mockito.Mockito.mock;

@QuarkusTest
public class LoanServiceTest {

    @Inject
    LoanService loanService;
    @InjectMock
    LoanRepository loanRepo;
    @InjectMock
    CommonUtils commonUtils;
    @InjectMock
    @RestClient
    AccountProxyLayer accountProxyLayer;
    @InjectMock
    @RestClient
    CustomerProxyLayer customerProxyLayer;

    @Inject
    LoanHistoryRepository historyRepository;
    Account accountDetails= new Account();
    Customer customerDetails = new Customer();
    LoanDto loanDto = new LoanDto();



    @BeforeEach
    public  void setUp(){
        ModelMapper modelMapper = new ModelMapper();

        loanDto.setCustomerId(1L);
        loanDto.setSavingsAccount(1L);

        accountDetails.setStatus("ACTIVE");
        accountDetails.setSavingsAccountId("1");

        customerDetails.setCustomerId(1L);

    }



    @Test
    void createLoanService()  {
        Response response = mock(Response.class);
        Response clinetResponse = mock(Response.class);
        Loan loan = mock(Loan.class);

        RestClientResponse accountClientResponse = new RestClientResponse();
        accountClientResponse.setData(accountDetails);
        Mockito.when(accountProxyLayer.getSavingAccountDetailBasedOnAccountId(loanDto.getSavingsAccount())).thenReturn(response);
        Mockito.when(response.readEntity(RestClientResponse.class)).thenReturn(accountClientResponse);

        RestClientResponse customerClientResponse = new RestClientResponse();
        customerClientResponse.setData(customerDetails);
        Mockito.when(clinetResponse.readEntity(RestClientResponse.class)).thenReturn(customerClientResponse);
        Mockito.when(customerProxyLayer.getCustomerByCriteria(loanDto.getSavingsAccount().toString())).thenReturn(clinetResponse);

        LoanSupportingDocument supdoc = mock(LoanSupportingDocument.class);
        FileUpload file = mock(FileUpload.class);
        Mockito.doNothing().when(loanRepo).persist(loan);
        Mockito.when(commonUtils.savingLoanSupportingDocmentDetails(file)).thenReturn(supdoc);
        LoanDto dto= loanService.createLoanService(file,loanDto);

        Assert.assertNotNull(dto);
        Assertions.assertEquals(dto.getCustomerId(),1);
        Assertions.assertEquals(dto.getStatus(), APPROVED);
    }

    @Test
    void createLoanWithInactiveAccount()  {
        Response response = mock(Response.class);
        Loan loan = mock(Loan.class);
        accountDetails.setStatus("INACTIVE");
        RestClientResponse accountClientResponse = new RestClientResponse();
        System.out.println(accountDetails.toString());
        accountClientResponse.setData(accountDetails);
        Mockito.when(accountProxyLayer.getSavingAccountDetailBasedOnAccountId(loanDto.getSavingsAccount())).thenReturn(response);
        Mockito.when(response.readEntity(RestClientResponse.class)).thenReturn(accountClientResponse);

        FileUpload file = mock(FileUpload.class);
        Mockito.doNothing().when(loanRepo).persist(loan);
        try {
            LoanDto dto = loanService.createLoanService(file, loanDto);
        }catch (ServiceException e){
            Assertions.assertEquals(e.getMessage(), ErrorCodes.INACTIVE_SAVINGS_ACCOUNT);
        }
    }

    @Test
    void createLoanServiceWithInactiveCustomer()  {
        Response response = mock(Response.class);
        Response clinetResponse = mock(Response.class);
        Loan loan = mock(Loan.class);

        RestClientResponse accountClientResponse = new RestClientResponse();
        accountClientResponse.setData(accountDetails);
        Mockito.when(accountProxyLayer.getSavingAccountDetailBasedOnAccountId(loanDto.getSavingsAccount())).thenReturn(response);
        Mockito.when(response.readEntity(RestClientResponse.class)).thenReturn(accountClientResponse);

        RestClientResponse customerClientResponse = new RestClientResponse();
        customerDetails.setCustomerId(111L);
        customerClientResponse.setData(customerDetails);
        Mockito.when(clinetResponse.readEntity(RestClientResponse.class)).thenReturn(customerClientResponse);
        Mockito.when(customerProxyLayer.getCustomerByCriteria(loanDto.getSavingsAccount().toString())).thenReturn(clinetResponse);

        LoanSupportingDocument supdoc = mock(LoanSupportingDocument.class);
        FileUpload file = mock(FileUpload.class);

        Mockito.doNothing().when(loanRepo).persist(loan);
        Mockito.when(commonUtils.savingLoanSupportingDocmentDetails(file)).thenReturn(supdoc);
        try {
            LoanDto dto = loanService.createLoanService(file, loanDto);
        }catch (ServiceException e){
            Assertions.assertEquals(e.getMessage(), ErrorCodes.INACTIVE_CUSTOMER);
        }
    }
}
