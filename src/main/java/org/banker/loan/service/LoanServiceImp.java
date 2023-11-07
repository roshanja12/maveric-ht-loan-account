package org.banker.loan.service;

import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import org.banker.loan.entity.LoanSupportingDocument;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.*;
import org.banker.loan.models.LoanDto;
import org.banker.loan.proxylayer.*;
import org.banker.loan.repository.LoanHistoryRepository;
import org.banker.loan.repository.LoanRepository;
import org.banker.loan.utils.CommonUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.modelmapper.ModelMapper;
import java.time.LocalDateTime;
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

    @Inject
    CommonUtils commonUtils;
    @RestClient
    @Inject
    CustomerProxyLayer customerLayer;
    @RestClient
    @Inject
    AccountProxyLayer accountProxyLayer;

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
                history.sort(Collections.reverseOrder(Comparator.comparing(LoanPaymentHistory::getPaidAt)));
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

    @Override
    @Transactional
    public LoanDto createLoanService(FileUpload supportFile, LoanDto dto) {
        try {
            log.info("Service for Creating loan called");
            ModelMapper modelMapper = new ModelMapper();
            Loan loanMapper = modelMapper.map(dto, Loan.class);
            if (isActiveSavingAccount(dto.getSavingsAccount()) && isActiveCustomer(dto.getCustomerId(), loanMapper)) {
                dto.setStatus(LoanStatus.APPROVED);
                loanMapper.setStatus(dto.getStatus());
                loanMapper.setCreatedAt(LocalDateTime.now());
                loanMapper.setLastUpdateAt(LocalDateTime.now());
                LoanSupportingDocument supDoc = commonUtils.savingLoanSupportingDocmentDetails(supportFile);
                supDoc.setUploadedDateTime(loanMapper.getCreatedAt());
                loanMapper.setLoanSupportingDocument(supDoc);
                supDoc.setLoan(loanMapper);
                loanRepository.persist(loanMapper);
                dto.setLoanId(loanMapper.getLoanId());
                return dto;
            }
            return new LoanDto();
        }catch (PersistenceException e){
            log.error("Error: ",e.getStackTrace());
            throw new SQLCustomExceptions(ErrorCodes.CONNECTION_ISSUE);
        }
    }

    public boolean isActiveCustomer(Long customerId,Loan loanMapper) {
        log.info("Validating the active customer for Creating loan called");
        ModelMapper modelMapper = new ModelMapper();
        Response restResponse = customerLayer.getCustomerByCriteria(customerId.toString());
        RestClientResponse response= restResponse.readEntity(RestClientResponse.class);
        Customer customer =modelMapper.map(response.getData(),Customer.class);
        if(customer.getCustomerId().equals(customerId)) {
            loanMapper.setCustomerName(customer.getFirstName() + " " + customer.getLastName());
            loanMapper.setEmail(customer.getEmail());
            loanMapper.setPhoneNumber(customer.getPhoneNumber());
            return true;
        } else{
            log.warn("Inactive customer for Creating loan called|"+customerId);
            throw new ServiceException(ErrorCodes.INACTIVE_CUSTOMER);
        }
    }
    public boolean isActiveSavingAccount(Long savingsAccountId) {
        log.info("Validating the savings account for Creating loan called");
        ModelMapper modelMapper = new ModelMapper();
        Response restResponse = accountProxyLayer.getSavingAccountDetailBasedOnAccountId(savingsAccountId);
        RestClientResponse response= restResponse.readEntity(RestClientResponse.class);
        Account account= modelMapper.map(response.getData(),Account.class);
        if (account.getStatus().equals("ACTIVE"))
            return true;
        log.warn("Inactive savings account for Creating loan called|"+savingsAccountId);
        throw new ServiceException(ErrorCodes.INACTIVE_SAVINGS_ACCOUNT);
    }


}
