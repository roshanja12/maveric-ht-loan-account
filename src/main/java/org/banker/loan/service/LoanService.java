package org.banker.loan.service;

import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.LoanIdNotFoundException;
import org.banker.loan.exception.NoDataException;
import org.banker.loan.models.LoanDto;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import java.util.List;

public interface LoanService {
   List<Loan> getAllLoan(int page,int size) throws NoDataException;
   List<LoanPaymentHistory> viewLoanById(Long id, int pageIndex, int pageSize) throws LoanIdNotFoundException;
   Loan status(Long id,LoanStatus status) throws LoanIdNotFoundException;
   LoanDto createLoanService(FileUpload supportFile, LoanDto dto);

}
