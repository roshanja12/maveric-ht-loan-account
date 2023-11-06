package org.banker.loan.service;

import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanException;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import static org.banker.loan.enums.LoanPaymentStatus.RECEIVED;
import static org.banker.loan.enums.LoanStatus.APPLIED;
import static org.banker.loan.enums.LoanStatus.APPROVED;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


@QuarkusTest
public class LoanServiceImpTest {
    @InjectMock
    LoanService loanService;

    @Test
    public void shouldGetAllLoans() throws NoDataException {
        List<Loan> loans = List.of(new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()));
        when(loanService.getAllLoan(anyInt(), anyInt())).thenReturn(loans);
        List<Loan> actualLoans = loanService.getAllLoan(1, 10);
        Assertions.assertNotNull(actualLoans);

    }
    @Test
    public void viewLoanById() throws LoanIdNotFoundException {
        List<LoanPaymentHistory> loanHistory = List.of(new LoanPaymentHistory(1L, 1000.00, 500.00, RECEIVED, "Payment 1", LocalDateTime.now()));
        when(loanService.viewLoanById(anyLong(),anyInt(), anyInt())).thenReturn(loanHistory);
        List<LoanPaymentHistory> loanPaymentHistories = loanService.viewLoanById(1L, 1, 10);
        Assertions.assertNotNull(loanPaymentHistories);
    }

    @Test
    public void status() throws LoanIdNotFoundException{
        Long loanId = 1L;
        LoanStatus status = LoanStatus.WITHDRAW;
        Loan loan = new Loan(1L, 1L, 10000.00, 12, APPLIED, LocalDateTime.now());
        loan.setStatus(LoanStatus.WITHDRAW);
        when(loanService.status(loanId,status)).thenReturn(loan);
        Loan result = loanService.status(loanId,status);
        Assertions.assertEquals(LoanStatus.WITHDRAW, result.getStatus());
    }

    @Test
    public void shouldGetByCreteria() {
        String searchValue = "mark";
        int page=0;
        int pageSize=2;
        List<Loan> expectedResults = List.of(new Loan(1L, 1L, 2L, null, 12, "Anil", "anil@gamail.com", 1233333L, APPROVED, LocalDateTime.now(), null, null));
        when(loanService.getAllData(anyString(), anyInt(), anyInt())).thenReturn(expectedResults);
        List<Loan> actualLoans=loanService.getAllData(searchValue,page,pageSize);
        Assertions.assertNotNull(actualLoans);
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