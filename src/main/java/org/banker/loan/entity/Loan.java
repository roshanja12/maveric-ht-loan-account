package org.banker.loan.entity;


import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.banker.loan.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Data
@Table(name = "loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loanId")
    private Long loanId;
    @NotNull
    @Column(name = "customerId")
    private Long customerId;
    @NotNull
    @Column(name = "accountId")
    private Long savingsAccountId;
    @NotNull
    @Column(name = "loanAmount")
    private BigDecimal loanAmount;
    @NotNull
    @Column(name = "emi")
    private int emi;
    @NotNull
    @Column(name = "status", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    @NotNull
    private LocalDateTime createdDateTime;

    @OneToOne(mappedBy = "document")
    private LoanSupportingDocument loanSupportingDocument;

    @OneToMany(mappedBy = "loanId",cascade= CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonbTransient
    public List<LoanPaymentHistory> paymentHistory;

    public Loan(long l, long l1, double bigDecimal, int emi, LoanStatus loanStatus, LocalDateTime now) {
    }

    public Loan() {
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", customerId=" + customerId +
                ", loanAmount=" + loanAmount +
                ", emi=" + emi +
                ", status=" + status +
                ", createdDateTime=" + createdDateTime +
                '}';
    }
}
