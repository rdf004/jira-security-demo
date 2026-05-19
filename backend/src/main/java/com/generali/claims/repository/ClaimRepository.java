package com.generali.claims.repository;

import com.generali.claims.model.Claim;
import com.generali.claims.model.ClaimType;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa
    .repository.JpaRepository;
import org.springframework.data.jpa
    .repository.Query;
import org.springframework.data.repository
    .query.Param;
import org.springframework.stereotype
    .Repository;

@Repository
public interface ClaimRepository
    extends JpaRepository<Claim, Long> {

    Optional<Claim> findByClaimNumber(
        String claimNumber
    );

    List<Claim> findByPolicyId(Long policyId);

    @Query(
        "SELECT c FROM Claim c "
        + "WHERE c.policy.id = :policyId "
        + "AND c.claimType = :claimType "
        + "AND c.submittedAt > :since"
    )
    List<Claim> findRecentByPolicyAndType(
        @Param("policyId") Long policyId,
        @Param("claimType") ClaimType type,
        @Param("since") Date since
    );
}
