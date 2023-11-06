package org.banker.loan.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.banker.loan.enums.LoanPaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Loan_payment_historys")
public class LoanPaymentHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanId")
    private Loan loanId;

    @NotNull
    private BigDecimal balance;
    @NotNull
    private BigDecimal amountPaid;
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "paymentStatus", columnDefinition = "varchar(20)")
    private LoanPaymentStatus paymentStatus;

    @Column(name = "description", columnDefinition = "varchar(75)")
    private String description;

    private LocalDateTime paidAt;

    public LoanPaymentHistory(long l, double v, double v1, LoanPaymentStatus loanPaymentStatus, String s, LocalDateTime now) {
    }

    public LoanPaymentHistory() {
    }

    @Override
    public String toString() {
        return "LoanPaymentHistory{" +
                "paymentId=" + paymentId +
                ", balance=" + balance +
                ", amountPaid=" + amountPaid +
                ", paymentStatus=" + paymentStatus +
                ", description=" + description +
                ", paidDateTime=" + paidAt +
                '}';
    }
}
