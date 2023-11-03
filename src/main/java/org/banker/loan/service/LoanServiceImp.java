package org.banker.loan.service;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.*;
import org.banker.loan.repository.LoanHistoryRepository;
import org.banker.loan.repository.LoanRepository;
import org.jboss.logging.Logger;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


@ApplicationScoped
public class LoanServiceImp implements LoanService{

    @Inject
    LoanRepository loanRepository;

    @Inject
    LoanHistoryRepository historyRepository;
    @Inject
    Logger log;

    @Override
    public List<Loan> getAllLoan(int page, int size) throws NoDataException {
       try {
            List<Loan> loans = loanRepository.findAll(Sort.descending("createdDateTime")).page(page, size).list();
            if (loans != null) {
                return loans;
            }
            else{
                throw new NoDataException(ErrorCodes.CONNECTION_ISSUE);
           }
        }catch (Exception exception){
           throw new SQLCustomExceptions("No data found ");
       }
    }

    @Override
    public List<LoanPaymentHistory> viewLoanById(Long id, int pageIndex, int pageSize) throws LoanIdNotFoundException {
        try {
            Loan loan = loanRepository.findLoanById(id);
            if (loan != null) {
                Page page = Page.of(pageIndex, pageSize);
                List<LoanPaymentHistory> history = historyRepository.findByLoanId(loan, page);
                history.sort(Collections.reverseOrder(Comparator.comparing(LoanPaymentHistory::getPaidDateTime)));
                return history;
            } else {
                throw new LoanIdNotFoundException(ErrorCodes.PAGE_NOT_FOUND);
            }
        } catch (Exception exception) {
            throw new SQLCustomExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

    @Override
    @Transactional
    public Loan status(Long id,LoanStatus status) throws LoanIdNotFoundException {
        try {
            Loan loanId = loanRepository.findLoanById(id);
            if (loanId.getStatus() == LoanStatus.APPLIED) {
                loanId.setStatus(status);
                loanRepository.persist(loanId);
                return loanId;
            } else {
                throw new LoanIdNotFoundException(ErrorCodes.PAGE_NOT_FOUND);
            }
        }catch (Exception exception){
            throw new LoanIdNotFoundException("Data not found in for the Id");
        }
    }


}
