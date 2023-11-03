package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.banker.loan.entity.Loan;
import org.banker.loan.entity.LoanPaymentHistory;
import java.util.List;

@ApplicationScoped
public class LoanHistoryRepository implements PanacheRepository<LoanPaymentHistory> {
    public List<LoanPaymentHistory> findByLoanId(Loan loanId, Page page) {
        return find("loanId", loanId).page(page).list();
    }
    public List<LoanPaymentHistory> findByLoanId(Loan loanId) {
        return find("loanId = ?1", loanId).list();
    }

}
