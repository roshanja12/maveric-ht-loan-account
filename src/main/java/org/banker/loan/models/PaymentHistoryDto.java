package org.banker.loan.models;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PaymentHistoryDto {
    private Long customerId;
    private String customerName;
    private String description;
    private BigDecimal amount;
    private String paymentStatus;
    private BigDecimal balance;
    private LocalDateTime paymentDateTime;

}
