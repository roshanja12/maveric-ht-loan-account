package org.banker.loan.models;

import lombok.Data;
import org.banker.loan.enums.Action;
import org.banker.loan.enums.LoanStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@Data
public class CreateLoanMessageDto implements Serializable  {
    private Long loanId;
    private LoanStatus loanStatus;
    private Integer customerId;
    private Action type;
    private Instant createdAt;
    private BigDecimal amount;
    private Integer month;
    private Integer year;
}
