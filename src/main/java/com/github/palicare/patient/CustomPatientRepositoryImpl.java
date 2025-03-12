package com.github.palicare.patient;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CustomPatientRepositoryImpl implements CustomPatientRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PatientEntity> searchPatients(String query, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PatientEntity> cq = cb.createQuery(PatientEntity.class);
        Root<PatientEntity> patient = cq.from(PatientEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        String[] terms = query.trim().split("\\s+");

        for (String term : terms) {
            String likePattern = "%" + term.toLowerCase() + "%";
            Predicate firstNamePredicate = cb.like(cb.lower(patient.get("firstName")), likePattern);
            Predicate lastNamePredicate = cb.like(cb.lower(patient.get("lastName")), likePattern);

            Predicate roomNumberPredicate = null;
            if (term.matches("\\d+")) { // Prüft, ob der Suchbegriff eine Zahl ist
                int roomNumber = Integer.parseInt(term);
                roomNumberPredicate = cb.equal(patient.get("roomNumber"), roomNumber);
            }

            if (roomNumberPredicate != null) {
                predicates.add(cb.or(firstNamePredicate, lastNamePredicate, roomNumberPredicate));
            } else {
                predicates.add(cb.or(firstNamePredicate, lastNamePredicate));
            }
        }

        cq.where(cb.and(predicates.toArray(new Predicate[0])));

        TypedQuery<PatientEntity> queryResult = entityManager.createQuery(cq);
        queryResult.setFirstResult((int) pageable.getOffset());
        queryResult.setMaxResults(pageable.getPageSize());

        List<PatientEntity> resultList = queryResult.getResultList();
        long totalCount = getTotalCount(query);

        return new PageImpl<>(resultList, pageable, totalCount);
    }

    private long getTotalCount(String query) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<PatientEntity> patient = countQuery.from(PatientEntity.class);

        List<Predicate> predicates = new ArrayList<>();
        String[] terms = query.trim().split("\\s+");

        for (String term : terms) {
            String likePattern = "%" + term.toLowerCase() + "%";
            Predicate firstNamePredicate = cb.like(cb.lower(patient.get("firstName")), likePattern);
            Predicate lastNamePredicate = cb.like(cb.lower(patient.get("lastName")), likePattern);

            Predicate roomNumberPredicate = null;
            if (term.matches("\\d+")) { // Prüft, ob der Suchbegriff eine Zahl ist
                int roomNumber = Integer.parseInt(term);
                roomNumberPredicate = cb.equal(patient.get("roomNumber"), roomNumber);
            }

            if (roomNumberPredicate != null) {
                predicates.add(cb.or(firstNamePredicate, lastNamePredicate, roomNumberPredicate));
            } else {
                predicates.add(cb.or(firstNamePredicate, lastNamePredicate));
            }
        }

        countQuery.select(cb.count(patient)).where(cb.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(countQuery).getSingleResult();
    }

}