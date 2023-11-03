package org.banker.loan.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Loan_Supporting_Documents")
public class LoanSupportingDocument  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loanDocId")
    private Long loanDocId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "loanId")
    private Loan loan;

    @Column(name = "docName", columnDefinition = "varchar(30)")
    private String docName;
    @Column(name = "type", columnDefinition = "varchar(20)")
    private String type;
    private Blob docByte;
    private LocalDateTime uploadedDateTime;

}
