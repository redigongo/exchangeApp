package com.exchange.exchange.specifications;

import com.exchange.exchange.DTO.TransactionFilterDTO;
import com.exchange.exchange.models.Transaction;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionSpecification implements Specification<Transaction> {

    private TransactionFilterDTO filterDTO;

    public TransactionSpecification(TransactionFilterDTO filterDTO) {
        super();
        this.filterDTO = filterDTO;
    }

    @Override
    public Predicate toPredicate(Root<Transaction> root, CriteriaQuery<?> query, CriteriaBuilder cb) {

        List<Predicate> predicates = new ArrayList<>();

        if (filterDTO == null) {
            return cb.and(predicates.toArray(new Predicate[predicates.size()]));
        }


        if (filterDTO.getStartDate() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("date"), LocalDateTime.of(filterDTO.getStartDate(), LocalTime.of(0, 0, 0))));
        }
        if (filterDTO.getEndDate() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("date"), LocalDateTime.of(filterDTO.getEndDate(), LocalTime.of(23, 59, 59))));
        }
        if (filterDTO.getSourceCurrency() != null) {
            predicates.add(cb.equal(root.get("sourceCurrency"), filterDTO.getSourceCurrency()));
        }
        if (filterDTO.getTargetCurrency() != null) {
            predicates.add(cb.equal(root.get("targetCurrency"), filterDTO.getTargetCurrency()));
        }


        return cb.and(predicates.toArray(new Predicate[predicates.size()]));
    }
}