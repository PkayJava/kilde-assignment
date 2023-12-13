package com.senior.kilde.assignment.api.component;

import com.senior.kilde.assignment.scommon.service.RepaymentService;
import com.senior.kilde.assignment.dao.entity.BorrowerRepayment;
import com.senior.kilde.assignment.dao.entity.BorrowerRepayment_;
import com.senior.kilde.assignment.dao.repository.BorrowerRepository;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.hibernate.jpa.AvailableHints;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BatchProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(BatchProcess.class);

    private final EntityManager entityManager;

    private final RepaymentService repaymentService;

    /**
     * Daily at time 00
     * @throws CloneNotSupportedException
     */
    @Scheduled(cron = "0 0 0 * * *")
    public void borrowRepaymentCollector() throws CloneNotSupportedException {
        LocalDate today = LocalDate.now();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<BorrowerRepayment> criteriaQuery = criteriaBuilder.createQuery(BorrowerRepayment.class);

        EntityGraph<BorrowerRepayment> graph = entityManager.createEntityGraph(BorrowerRepayment.class);
        graph.addAttributeNodes(BorrowerRepayment_.account);

        Root<BorrowerRepayment> root = criteriaQuery.from(BorrowerRepayment.class);
        criteriaQuery.select(root);
        criteriaQuery.where(criteriaBuilder.notEqual(root.get(BorrowerRepayment_.loanDuration), root.get(BorrowerRepayment_.monthPaymentCount)));
        criteriaQuery.where(criteriaBuilder.lessThanOrEqualTo(root.get(BorrowerRepayment_.nextPaymentDate), today.toDate()));
        TypedQuery<BorrowerRepayment> query = entityManager.createQuery(criteriaQuery);
        query.setHint(AvailableHints.HINT_SPEC_LOAD_GRAPH, graph);
        List<BorrowerRepayment> repayments = query.getResultList();
        for (BorrowerRepayment repayment : repayments) {
            try {
                this.repaymentService.processRepayment((BorrowerRepayment) repayment.clone());
                LOGGER.info(repayment.getId() + " Successful");
            } catch (Throwable e) {
                LOGGER.info(repayment.getId() + " Error due to " + e.getMessage());
            }
        }
    }

}
