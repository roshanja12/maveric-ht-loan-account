package org.banker.loan.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import org.banker.loan.entity.Loan;
import org.banker.loan.exception.LoanException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.repository.LoanRepository;
import org.banker.loan.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@QuarkusTest
public class LoanResourcesTest {

    @InjectMock
    LoanService loanService;
    @InjectMock
    LoanRepository loanRepository;
    @Inject
    LoanResource loanResource;

    @BeforeEach
    public void setup() throws NoDataException {
        List<Loan> mockLoanList = Collections.singletonList(new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()));
        Mockito.when(loanService.getAllLoan(1, 10)).thenReturn(mockLoanList);
    }


    @Test
    public void testGetAllLoan() {
        io.restassured.response.Response response = RestAssured.given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .when()
                .get("/api/v1/loan")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void testViewLoanById() {
        Long loanId = 1L;
        io.restassured.response.Response response = RestAssured.given()
                .queryParam("page", 1)
                .queryParam("size", 10)
                .get("/api/v1/loan/loanid/{id}", loanId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void status(){
        Long loanId = 1L;
        io.restassured.response.Response response = RestAssured.given()
                .put("/api/v1/loan/status/{id}", loanId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void testGetLoanByCriteria_Success() {
        String searchValue = "email";
        int page=0;
        int pageSize=2;
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan());
        when(loanService.getAllData(searchValue,page,pageSize)).thenReturn(loans);

        ResponseDto response= loanResource.search(searchValue,page,pageSize);
        assertNotNull(response);

    }
    @Test
    public void testGetLoanByCriteria_Failure()  throws LoanException {
        String searchValue = "mark";
        int page=0;
        int pageSize=2;
        List<Loan> loans = new ArrayList<>();
        when(loanService.getAllData(searchValue,page,pageSize)).thenReturn(loans).thenThrow(LoanException.class);
    }


    @Test
    public void testStatusUpdate_Success() {
        Long loanId=1L;
        String status="APPROVED";
        List<Loan> loans = new ArrayList<>();
        loans.add(new Loan());
        when(loanService.statusUpdate(loanId,status)).thenReturn("Updated status in DB");
        ResponseDto  message=loanResource.statusUpdate(loanId,status);
        assertNotNull(message.getMsg());
    }

    @Test
    public void testStatusUpdate_Failure() throws LoanException {
        Long loanId=1L;
        String status="APPROVED";
        when(loanRepository.findById(loanId)).thenReturn(null);
        when(loanService.statusUpdate(loanId,status)).thenReturn("Data not found in DB").thenThrow(LoanException.class);
    }

}
