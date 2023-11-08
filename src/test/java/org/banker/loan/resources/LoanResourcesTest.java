package org.banker.loan.resources;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import org.banker.loan.entity.Loan;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.LoanDto;
import org.banker.loan.models.ResponseDto;
import org.banker.loan.proxylayer.RestClientResponse;
import org.banker.loan.repository.LoanRepository;
import org.banker.loan.service.LoanService;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
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
        when(loanService.getAllLoan(1, 10)).thenReturn(mockLoanList);
    }


    @Test
    public void testGetAllLoan() {
        io.restassured.response.Response response = given()
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
        io.restassured.response.Response response = given()
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
        io.restassured.response.Response response = given()
                .put("/api/v1/loan/status/{id}", loanId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .extract()
                .response();
        Assertions.assertEquals(200, response.statusCode());
    }

    @Test
    public void createLoanAccoount(){
        Long loanId = 1L;
        LoanDto loanDto = new LoanDto();
        loanDto.setCustomerId(1L);
        loanDto.setSavingsAccount(1L);
        FileUpload file = mock(FileUpload.class);
        URL resourceUrl = LoanResourcesTest.class.getClassLoader().getResource("application.properties");
        File file1 = new File(resourceUrl.getPath());
        when(loanService.createLoanService(file, loanDto)).thenReturn(loanDto);
        String dto ="{\n" +
                "    \"customerId\":1,\n" +
                "    \"savingsAccount\":1,\n" +
                "    \"loanAmount\":199,\n" +
                "    \"emi\":6\n" +
                "}";

       given()
                .multiPart("supportingDoc", file1)
                .formParam("loanDto",dto)
                .when()
                .post("/api/v1/loan/")
                .then()
                .statusCode(202)
                .body("msg",equalToIgnoringCase("Customer Loan Created"));
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
        ResponseDto message=loanResource.statusUpdate(loanId,status);
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
