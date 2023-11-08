package org.banker.loan.entity;


import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.banker.loan.enums.LoanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Loans")
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loanId")
    private Long loanId;
    @NotNull
    @Column(name = "customerId")
    private Long customerId;
    @NotNull
    @Column(name = "savingsAccount")
    private Long savingsAccount;
    @NotNull
    @Column(name = "loanAmount")
    private BigDecimal loanAmount;
    @NotNull
    @Column(name = "emi")
    private int emi;
    @NotNull
    @Column(name = "customerName", columnDefinition = "varchar(40)")
    private String customerName;
    @NotNull
    @Column(name = "email", columnDefinition = "varchar(40)")
    private String email;
    @NotNull
    @Column(name = "phoneNumber", columnDefinition = "varchar(10)")
    private Long phoneNumber;
    @NotNull
    @Column(name = "status", columnDefinition = "varchar(20)")
    @Enumerated(EnumType.STRING)
    private LoanStatus status;
    @NotNull
    private LocalDateTime createdAt;

    @NotNull
    private LocalDateTime lastUpdateAt;

    @OneToOne   (mappedBy = "loan",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JsonbTransient
    private LoanSupportingDocument loanSupportingDocument;

    @OneToMany(mappedBy = "loanId",cascade= CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonbTransient
    public List<LoanPaymentHistory> paymentHistory;

    public Loan(long l, long l1, double bigDecimal, int emi, LoanStatus loanStatus, LocalDateTime now) {
    }


    @Override
    public String toString() {
        return "Loan{" +
                "loanId=" + loanId +
                ", customerId=" + customerId +
                ", savingsAccount=" + savingsAccount +
                ", loanAmount=" + loanAmount +
                ", emi=" + emi +
                ", customerName='" + customerName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", Status=" + status +
                ", createdDateTime=" + createdAt+
                '}';
    }
}
