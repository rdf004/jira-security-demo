package com.generali.claims.repository;

import com.generali.claims.model.Policy;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa
    .repository.JpaRepository;
import org.springframework.stereotype
    .Repository;

@Repository
public interface PolicyRepository
    extends JpaRepository<Policy, Long> {

    List<Policy> findByCustomerId(
        Long customerId
    );

    List<Policy> findByActiveTrue();

    Optional<Policy> findByPolicyNumber(
        String policyNumber
    );
}
