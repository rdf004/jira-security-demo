package com.generali.claims.service;

import static org.junit.jupiter.api
    .Assertions.assertEquals;
import static org.junit.jupiter.api
    .Assertions.assertFalse;

import com.generali.claims.model.Claim;
import com.generali.claims.model.ClaimType;
import com.generali.claims.model.Customer;
import com.generali.claims.model.Policy;
import com.generali.claims.model.TriageResult;
import com.generali.claims.rules
    .TriageRuleConfig;
import com.generali.claims.rules
    .TriageRuleEngine;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TriageServiceTest {

    private TriageRuleEngine ruleEngine;
    private Policy activePolicy;
    private Policy inactivePolicy;
    private SimpleDateFormat sdf;

    @BeforeEach
    void setUp() throws ParseException {
        ruleEngine = new TriageRuleEngine();
        TriageRuleConfig config =
            new TriageRuleConfig();
        ruleEngine.setConfig(config);

        sdf = new SimpleDateFormat(
            "yyyy-MM-dd"
        );

        Customer customer = new Customer(
            "Maria", "Rossi",
            "maria@test.com", "+39 123"
        );

        activePolicy = new Policy();
        activePolicy.setId(1L);
        activePolicy.setPolicyNumber(
            "POL-HOME-001"
        );
        activePolicy.setPolicyType("Home");
        activePolicy.setCustomer(customer);
        activePolicy.setActive(true);
        activePolicy.setStartDate(
            sdf.parse("2025-01-01")
        );
        activePolicy.setEndDate(
            sdf.parse("2026-01-01")
        );
        activePolicy.setCoveredClaimTypes(
            new HashSet<>(
                Arrays.asList(
                    ClaimType
                        .HOME_WATER_DAMAGE
                )
            )
        );

        inactivePolicy = new Policy();
        inactivePolicy.setId(2L);
        inactivePolicy.setActive(false);
        inactivePolicy.setStartDate(
            sdf.parse("2023-01-01")
        );
        inactivePolicy.setEndDate(
            sdf.parse("2024-01-01")
        );
        inactivePolicy.setCustomer(customer);
    }

    private Claim createClaim(
        Policy policy,
        BigDecimal amount,
        List<String> missingDocs
    ) throws ParseException {
        Claim claim = new Claim();
        claim.setPolicy(policy);
        claim.setClaimType(
            ClaimType.HOME_WATER_DAMAGE
        );
        claim.setIncidentDate(
            sdf.parse("2025-06-15")
        );
        claim.setEstimatedLoss(amount);
        claim.setMissingDocuments(
            missingDocs != null
                ? new HashSet<>(missingDocs)
                : new HashSet<>()
        );
        claim.setDescription(
            "Water damage in kitchen"
        );
        return claim;
    }

    @Test
    void testStraightThroughProcessing()
        throws ParseException {
        Claim claim = createClaim(
            activePolicy,
            new BigDecimal("500"),
            Collections.emptyList()
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, false, reasons
            );
        assertEquals(
            TriageResult
                .STRAIGHT_THROUGH_PROCESSING,
            result
        );
        assertFalse(reasons.isEmpty());
    }

    @Test
    void testAdjusterReviewHighValue()
        throws ParseException {
        Claim claim = createClaim(
            activePolicy,
            new BigDecimal("10000"),
            Collections.emptyList()
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, false, reasons
            );
        assertEquals(
            TriageResult.ADJUSTER_REVIEW,
            result
        );
    }

    @Test
    void testAdjusterReviewMissingDocs()
        throws ParseException {
        Claim claim = createClaim(
            activePolicy,
            new BigDecimal("1500"),
            Arrays.asList("Repair estimate")
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, false, reasons
            );
        assertEquals(
            TriageResult.ADJUSTER_REVIEW,
            result
        );
        assertTrue(reasons, "Missing");
    }

    @Test
    void testFraudDuplicateClaim()
        throws ParseException {
        Claim claim = createClaim(
            activePolicy,
            new BigDecimal("1000"),
            Collections.emptyList()
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, true, reasons
            );
        assertEquals(
            TriageResult
                .FRAUD_MANUAL_INVESTIGATION,
            result
        );
        assertTrue(reasons, "Duplicate");
    }

    @Test
    void testFraudExpiredPolicy()
        throws ParseException {
        Claim claim = createClaim(
            inactivePolicy,
            new BigDecimal("1000"),
            Collections.emptyList()
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, false, reasons
            );
        assertEquals(
            TriageResult
                .FRAUD_MANUAL_INVESTIGATION,
            result
        );
        assertTrue(reasons, "expired");
    }

    @Test
    void testFraudHighAmount()
        throws ParseException {
        Claim claim = createClaim(
            activePolicy,
            new BigDecimal("50000"),
            Collections.emptyList()
        );
        List<String> reasons =
            new ArrayList<>();
        TriageResult result =
            ruleEngine.evaluate(
                claim, false, reasons
            );
        assertEquals(
            TriageResult
                .FRAUD_MANUAL_INVESTIGATION,
            result
        );
        assertTrue(reasons, "high");
    }

    private void assertTrue(
        List<String> reasons,
        String keyword
    ) {
        boolean found = reasons.stream()
            .anyMatch(
                r -> r.toLowerCase().contains(
                    keyword.toLowerCase()
                )
            );
        org.junit.jupiter.api.Assertions
            .assertTrue(
                found,
                "Expected reason containing '"
                + keyword + "' in: "
                + reasons
            );
    }
}
