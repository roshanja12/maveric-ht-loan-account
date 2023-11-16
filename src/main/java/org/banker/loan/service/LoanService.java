package org.banker.loan.service;

import jakarta.ws.rs.QueryParam;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.LoanDto;
import org.banker.loan.models.TransactionRequestDto;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public interface LoanService {
   List<Loan> getAllLoan(int page,int size) throws NoDataException;
   List<LoanPaymentHistory> viewLoanById(Long id, int pageIndex, int pageSize) throws LoanIdNotFoundException;
   String status(Long id,String status) throws LoanIdNotFoundException;
   LoanDto createLoanService(FileUpload supportFile, LoanDto dto);
   public List<Loan> getAllData(@QueryParam("searchBy") String name, @QueryParam("page") int page, @QueryParam("size") int size);
   public String statusUpdate(Long loanId, String status);
   public String historyStatus(TransactionRequestDto transactionRequestDto);

}
