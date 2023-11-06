package org.banker.loan.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.banker.loan.entity.Loan;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.service.LoanService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.banker.loan.enums.LoanStatus.APPROVED;

@QuarkusTest
public class LoanResourcesTest {

    @InjectMock
    LoanService loanService;

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
}
