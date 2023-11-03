package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
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
        Assertions.assertNotNull(result);
    }

    @Test
    public void status() {
        Loan sampleLoan = new Loan(1L, 1L, 10000.00, 12, APPLIED, LocalDateTime.now());
        Long loanId = 1L;
        Mockito.when((loanRepository.findLoanById(loanId))).thenReturn(sampleLoan);
        Loan result = loanRepository.findLoanById(loanId);
        Assertions.assertNotNull(result);
    }
}
