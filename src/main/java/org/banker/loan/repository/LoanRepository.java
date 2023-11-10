package org.banker.loan.repository;

import io.quarkus.hibernate.orm.panache.Panache;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Page;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.banker.loan.entity.Loan;

import java.util.List;

@ApplicationScoped
public class LoanRepository implements PanacheRepository<Loan> {
    @Inject
    EntityManager entityManager;

    public Loan findLoanById(Long id) {
        return find("loanId = :loanId", Parameters.with("loanId", id)).firstResult();
    }

    public List<Loan> getLoanIdByCreteria(String searchValue, Page page) {

        String nativeQuery = "SELECT * FROM loans WHERE CAST(loanId AS TEXT) ILIKE :searchValue OR phonenumber ILIKE :searchValue OR CAST(phoneNumber AS TEXT) ILIKE :searchValue OR customername ILIKE :searchValue OR email ILIKE :searchValue ";
        return entityManager.createNativeQuery(nativeQuery, Loan.class)
                .setParameter("searchValue", "%" + searchValue + "%")
                .setFirstResult(page.index * page.size)
                .setMaxResults(page.size)
                .getResultList();
    }

    public void updateLoanByIdAndStatus(Long loanId, String status){
        String updateQuery = "UPDATE loans SET status = ?1 WHERE loanId = ?2";
        Panache.getEntityManager().createNativeQuery(updateQuery)
                .setParameter(1, status)
                .setParameter(2, loanId)
                .executeUpdate();
    }
}
