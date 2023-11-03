package org.banker.loan.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Blob;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "loansupportingdocuments")
public class LoanSupportingDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loanDocId")
    private Long loanDocId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loanId", referencedColumnName = "loanId")
    private Loan document;
    @Column(name = "docName", columnDefinition = "varchar(30)")
    private String docName;
    @Column(name = "type", columnDefinition = "varchar(20)")
    private String type;
    private Blob docByte;
    private LocalDateTime uploadedDateTime;

}
