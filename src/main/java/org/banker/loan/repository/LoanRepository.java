package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import org.banker.loan.entity.Loan;

import java.util.List;

@ApplicationScoped
public class LoanRepository implements PanacheRepository<Loan> {


    public Loan findLoanById(Long id) {
        return find("loanId = :loanId", Parameters.with("loanId", id)).firstResult();
    }

}
