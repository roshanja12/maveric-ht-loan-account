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
import org.banker.loan.enums.LoanPaymentStatus;
import org.banker.loan.enums.LoanStatus;
import org.banker.loan.exception.*;
import org.banker.loan.models.LoanDto;
import org.banker.loan.models.TransactionRequestDto;
import org.banker.loan.proxylayer.*;
import org.banker.loan.repository.LoanHistoryRepository;
import org.banker.loan.repository.LoanRepository;
import org.banker.loan.utils.CommonUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.multipart.FileUpload;
import org.modelmapper.ModelMapper;


import java.math.BigDecimal;


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

    @RestClient
    @Inject
    SavingsProxyLayer savingsProxyLayer;
    @Override
    public List<Loan> getAllLoan(int page, int size) throws NoDataException {
       try {
            List<Loan> loans = loanRepository.findAll(Sort.descending("createdDateTime")).page(page, size).list();
            if (loans != null) {
                return loans;
            }
            else{
                throw new NoDataException(ErrorCodes.PAGE_NOT_FOUND);
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
            throw new SQLCustomExceptions("No data found ");
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

    @Override
    public List<Loan> getAllData(String name, int pageIndex, int pageSize) {
        Page page = Page.of(pageIndex, pageSize);
        List<Loan> list= loanRepository.getLoanIdByCreteria(name,page);
        if(!list.isEmpty()){
            list.sort(Collections.reverseOrder(Comparator.comparing(Loan::getCreatedAt)));
            return  list;
        }
        else{
            throw new LoanException("Data not found in DB");
        }
    }

    @Override
    public String statusUpdate(Long loanId, String status) {
        Loan loan=  loanRepository.findById(loanId);
        if(loan!=null){
            loanRepository.updateLoanByIdAndStatus(loanId,status);
            return "Updated status in DB";
        }
        else{
            throw new LoanException("User not in database");
        }
    }

    @Override
    public String historyStatus(TransactionRequestDto transactionRequestDto) {
     Loan loan=   loanRepository.findById(transactionRequestDto.getAccountId());
 Boolean value= savingsProxyLayer.getTransactionHistories(transactionRequestDto);
    if(value){
        LoanPaymentHistory loanPaymentHistory= new LoanPaymentHistory();
            loanPaymentHistory.setLoanId(loan);
            loanPaymentHistory.setPaymentStatus(LoanPaymentStatus.RECEIVED);
            loanPaymentHistory.setDescription("Branch Bangalore");
            loanPaymentHistory.setAmountPaid(transactionRequestDto.getAmount());
            loanPaymentHistory.setBalance(amountDetect(loan,transactionRequestDto));
            loanPaymentHistory.setPaidAt(LocalDateTime.now());
            System.out.println(loanPaymentHistory.toString());
            historyRepository.persist(loanPaymentHistory);
            return "Successfully transcation done with payementId:"+loanPaymentHistory.getPaymentId();
        }
      else{
          throw new AmountNotAvailableException("Amount is not available");
        }
    }

    public BigDecimal amountDetect(Loan loan,TransactionRequestDto transactionRequestDto){
        BigDecimal result=null;
Loan loan1=loanRepository.findById(loan.getLoanId());
if(loan1!=null){
   if(loan1.getLoanAmount().compareTo(BigDecimal.ZERO) > 0 && loan1.getLoanAmount().compareTo(transactionRequestDto.getAmount())> 0 ){
       result= loan1.getLoanAmount().subtract(transactionRequestDto.getAmount());
       loan1.setLoanAmount(result);
       System.out.println(loan1.toString());
       loanRepository.persist(loan1);
       return result;
   }
   else{
       throw new AmountNotAvailableException("Not available amount");
   }

}
else{
    throw new AmountNotAvailableException("Not available in database");
}
    }

}
