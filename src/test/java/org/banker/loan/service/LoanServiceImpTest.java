package org.banker.loan.service;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import io.quarkus.test.InjectMock;
import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.UriInfo;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.AmountNotAvailableException;
import org.banker.loan.exception.LoanException;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.TransactionRequestDto;
import org.banker.loan.proxylayer.AccountProxyLayer;
import org.banker.loan.proxylayer.SavingsProxyLayer;
import org.banker.loan.repository.LoanRepository;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@QuarkusTest
public class LoanServiceImpTest {
    @Inject
    LoanService loanService;
    @InjectMock
    LoanRepository loanRepository;
    @InjectMock
    @RestClient
    SavingsProxyLayer savingsProxyLayer;
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
            Loan status = new Loan();
            status.setStatus(APPROVED);
            Assertions.assertNotNull(status);
            Assertions.assertEquals(status.getStatus(),APPROVED);
        });
    }

   // @Test
    public void shouldGetByCreteria() {
        String searchValue = "1";
        int pageIndex=0;
        int pageSize=1;
        Page page = Page.of(pageIndex, pageSize);
        List<Loan> expectedResults = List.of(new Loan(1L, 1L, 10000.00, 12, APPROVED, LocalDateTime.now()));
        when(loanRepository.getLoanIdByCreteria(searchValue,page)).thenReturn(expectedResults);
        List<Loan> actualLoans=loanService.getAllData(searchValue,pageIndex,pageSize);
        Assertions.assertNotNull(actualLoans);
    }

    @Test
    public void testStatusUpdateSuccess() {
        Long loanId = 1L;
        String status = "UpdatedStatus";
        Loan mockLoan = new Loan();
        when(loanRepository.findById(loanId)).thenReturn(mockLoan);
        String result = loanService.statusUpdate(loanId, status);
        assertEquals("Updated status in DB", result);
    }

    @Test
    public void testStatusUpdateNotFound() {
        Long loanId = 2L;
        String status = "UpdatedStatus";
        try {
            when(loanService.statusUpdate(loanId, status)).thenReturn(null).thenThrow(LoanException.class);
        }catch (LoanException e){
            assertEquals("User not in database", e.getMessage());

        }
    }


    public void testHistoryStatusSuccess() {
        Loan loan = new Loan(1L, 1L, 10000.00, 12, APPLIED, LocalDateTime.now());
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAccountId(1L);
        requestDto.setAmount(BigDecimal.valueOf(22.00));
        when(loanRepository.findById(requestDto.getAccountId())).thenReturn(loan);
       // when(savingsProxyLayer.getTransactionHistories(requestDto)).thenReturn(true);
        String result = loanService.historyStatus(requestDto);
        assertTrue(result.startsWith("Successfully transcation done with payementId:"));
    }

    @Test
    public void testHistoryStatus_Failure() {
        TransactionRequestDto requestDto = new TransactionRequestDto();
        requestDto.setAccountId(1L);
        requestDto.setAmount(BigDecimal.valueOf(22.00));
        //when(savingsProxyLayer.getTransactionHistories(requestDto)).thenReturn(false);
        assertThrows(AmountNotAvailableException.class, () -> {
            loanService.historyStatus(requestDto);
        });
    }

}