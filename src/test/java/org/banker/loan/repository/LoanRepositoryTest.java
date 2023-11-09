package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.util.List;

import static org.banker.loan.enums.LoanPaymentStatus.RECEIVED;
import static org.banker.loan.enums.LoanStatus.APPLIED;
import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class LoanRepositoryTest {
    @InjectMock
    LoanRepository loanRepository;
    @InjectMock
    LoanHistoryRepository historyRepository;


    @Test
    public void shouldGetAllLoans() {
        List<Loan> loans = List.of(new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()));
        PanacheQuery<Loan> query = Mockito.mock(PanacheQuery.class);
        Mockito.when(loanRepository.findAll()).thenReturn(query);
        Mockito.when(query.list()).thenReturn(loans);
    }
    @Test
    public void viewLoanById() {
        Loan sampleLoan = new Loan(1L, 1L, 10000.00, 12, APPLIED, LocalDateTime.now());
        Mockito.when(historyRepository.find("loanId", sampleLoan))
                .thenReturn(Mockito.mock(PanacheQuery.class));
        List<LoanPaymentHistory> result = historyRepository.findByLoanId(sampleLoan, Page.of(1, 10));
        assertNotNull(result);
    }

    @Test
    public void status() {
        Loan sampleLoan = new Loan(1L, 1L, 10000.00, 12, APPLIED, LocalDateTime.now());
        Long loanId = 1L;
        Mockito.when((loanRepository.findLoanById(loanId))).thenReturn(sampleLoan);
        Loan result = loanRepository.findLoanById(loanId);
        assertNotNull(result);
    }

    public void testGetLoanIdByCriteria() {
        String searchValue ="1";
        Page page = new Page(0, 10);
        Response response = RestAssured.given()
                .contentType(ContentType.JSON)
                .queryParam("searchValue", searchValue)
                .queryParam("page.index", page.index)
                .queryParam("page.size", page.size)
                .when()
                .get("/api/v1/loan/search");
        response.then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }
    @Test
    public void testGetLoanIdByCriteria_Success() {
        Loan loan1 = new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()); // Create a loan entity
       loanRepository.persist(loan1);
       String searchValue = "10000.00";
        Page page = new Page(0, 10);
        List<Loan> result = loanRepository.getLoanIdByCreteria(searchValue, page);
        assertNotNull(result);
    }
}
