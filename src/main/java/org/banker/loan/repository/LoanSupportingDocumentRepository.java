package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.banker.loan.entity.LoanSupportingDocument;
@ApplicationScoped
public interface LoanSupportingDocumentRepository extends PanacheRepository<LoanSupportingDocument> {
}
