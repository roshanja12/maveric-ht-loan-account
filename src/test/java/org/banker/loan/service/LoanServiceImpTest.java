package org.banker.loan.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanException;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.repository.LoanHistoryRepository;
import org.banker.loan.repository.LoanRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.banker.loan.enums.LoanPaymentStatus.RECEIVED;
import static org.banker.loan.enums.LoanStatus.APPLIED;
import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@QuarkusTest
public class LoanServiceImpTest {
    @Inject
    LoanServiceImp loanService;

    @InjectMock
    LoanRepository loanRepository;

    @InjectMock
    LoanHistoryRepository historyRepository;

    @Test
    public void shouldGetAllLoans()  {
        List<Loan> testLoans = new ArrayList<>();
        testLoans.add(new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()));
        PanacheQuery<Loan> panacheQuery = Mockito.mock(PanacheQuery.class);
        when(panacheQuery.list()).thenReturn(testLoans);
        when(loanRepository.findAll(Sort.descending("createdDateTime"))).thenReturn(panacheQuery);
        Assertions.assertNotNull(testLoans);
    }
    @Test
    public void testViewLoanById() throws LoanIdNotFoundException {
        Loan testLoan = new Loan(1L, 1L, 10000.00, 12, LoanStatus.APPROVED, LocalDateTime.now());
        List<LoanPaymentHistory> testHistory = List.of(new LoanPaymentHistory(1L, 1000.00, 500.00, RECEIVED, "Payment 1", LocalDateTime.now()));
        when(loanRepository.findLoanById(1L)).thenReturn(testLoan);
        when(historyRepository.findByLoanId(testLoan, Page.of(0, 10))).thenReturn(testHistory);
        List<LoanPaymentHistory> result = loanService.viewLoanById(1L, 0, 10);
        Assertions.assertNotNull(result);
    }

    @Test
    public void testStatusLoanApplied() {
        Long loanId = 1L;
        Loan testLoan = new Loan(1L, 1L, 10000.00, 12, LoanStatus.APPLIED, LocalDateTime.now());
        when(loanRepository.findLoanById(loanId)).thenReturn(testLoan);
        LoanIdNotFoundException exception = assertThrows(LoanIdNotFoundException.class, () -> {
            Loan status = loanService.status(loanId, APPROVED);
            Assertions.assertNotNull(status);
            Assertions.assertEquals(status.getStatus(),APPROVED);
        });
    }
    @Test
    public void testStatusUpdateSuccess() {
        Long loanId = 1L;
        String status = "UpdatedStatus";
        Loan mockLoan = new Loan();
        when(loanService.statusUpdate(loanId,status)).thenReturn("Updated status in DB");
        String result = loanService.statusUpdate(loanId, status);
        assertEquals("Updated status in DB", result);
    }

    @Test
    public void testStatusUpdateNotFound() {
        Long loanId = 2L;
        String status = "UpdatedStatus";
        when(loanService.statusUpdate(loanId,status)).thenReturn(null).thenThrow(LoanException.class);
    }

}