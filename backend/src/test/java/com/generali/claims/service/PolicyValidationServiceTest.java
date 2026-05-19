package com.generali.claims.service;

import static org.junit.jupiter.api
    .Assertions.assertEquals;
import static org.junit.jupiter.api
    .Assertions.assertFalse;
import static org.junit.jupiter.api
    .Assertions.assertTrue;

import com.generali.claims.model.ClaimType;
import com.generali.claims.model.Customer;
import com.generali.claims.model.Policy;
import com.generali.claims.model
    .ValidationResult;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PolicyValidationServiceTest {

    private PolicyValidationService service;
    private Policy activePolicy;
    private Policy inactivePolicy;
    private SimpleDateFormat sdf;

    @BeforeEach
    void setUp() throws ParseException {
        service =
            new PolicyValidationService();
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
        activePolicy.setRequiredDocuments(
            new HashSet<>(
                Arrays.asList(
                    "Photos of damage",
                    "Repair estimate"
                )
            )
        );

        inactivePolicy = new Policy();
        inactivePolicy.setId(2L);
        inactivePolicy.setPolicyNumber(
            "POL-AUTO-006"
        );
        inactivePolicy.setPolicyType("Auto");
        inactivePolicy.setCustomer(customer);
        inactivePolicy.setActive(false);
        inactivePolicy.setStartDate(
            sdf.parse("2023-01-01")
        );
        inactivePolicy.setEndDate(
            sdf.parse("2024-01-01")
        );
        inactivePolicy.setCoveredClaimTypes(
            new HashSet<>(
                Arrays.asList(
                    ClaimType.AUTO_COLLISION
                )
            )
        );
        inactivePolicy.setRequiredDocuments(
            new HashSet<>(
                Arrays.asList(
                    "Police report"
                )
            )
        );
    }

    @Test
    void testActivePolicyValidation()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.HOME_WATER_DAMAGE,
                sdf.parse("2025-06-15"),
                Arrays.asList(
                    "Photos of damage",
                    "Repair estimate"
                )
            );
        assertTrue(result.isValid());
        assertTrue(
            result.getErrors().isEmpty()
        );
    }

    @Test
    void testInactivePolicyValidation()
        throws ParseException {
        ValidationResult result =
            service.validate(
                inactivePolicy,
                ClaimType.AUTO_COLLISION,
                sdf.parse("2023-06-15"),
                Arrays.asList(
                    "Police report"
                )
            );
        assertFalse(result.isValid());
        assertTrue(
            result.getErrors().stream()
                .anyMatch(
                    e -> e.contains(
                        "not active"
                    )
                )
        );
    }

    @Test
    void testCoverageDateBeforeStart()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.HOME_WATER_DAMAGE,
                sdf.parse("2024-06-15"),
                Arrays.asList(
                    "Photos of damage",
                    "Repair estimate"
                )
            );
        assertFalse(result.isValid());
        assertTrue(
            result.getErrors().stream()
                .anyMatch(
                    e -> e.contains("before")
                )
        );
    }

    @Test
    void testCoverageDateAfterEnd()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.HOME_WATER_DAMAGE,
                sdf.parse("2027-01-15"),
                Arrays.asList(
                    "Photos of damage",
                    "Repair estimate"
                )
            );
        assertFalse(result.isValid());
        assertTrue(
            result.getErrors().stream()
                .anyMatch(
                    e -> e.contains("after")
                )
        );
    }

    @Test
    void testClaimTypeNotCovered()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.AUTO_COLLISION,
                sdf.parse("2025-06-15"),
                Collections.emptyList()
            );
        assertFalse(result.isValid());
        assertTrue(
            result.getErrors().stream()
                .anyMatch(
                    e -> e.contains(
                        "not covered"
                    )
                )
        );
    }

    @Test
    void testMissingRequiredDocuments()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.HOME_WATER_DAMAGE,
                sdf.parse("2025-06-15"),
                Arrays.asList(
                    "Photos of damage"
                )
            );
        assertTrue(result.isValid());
        assertEquals(
            1,
            result.getMissingDocuments()
                .size()
        );
        assertEquals(
            "Repair estimate",
            result.getMissingDocuments()
                .get(0)
        );
    }

    @Test
    void testAllDocumentsPresent()
        throws ParseException {
        ValidationResult result =
            service.validate(
                activePolicy,
                ClaimType.HOME_WATER_DAMAGE,
                sdf.parse("2025-06-15"),
                Arrays.asList(
                    "Photos of damage",
                    "Repair estimate"
                )
            );
        assertTrue(result.isValid());
        assertTrue(
            result.getMissingDocuments()
                .isEmpty()
        );
    }

    @Test
    void testNullPolicyValidation() {
        ValidationResult result =
            service.validate(
                null,
                ClaimType.HOME_WATER_DAMAGE,
                null,
                null
            );
        assertFalse(result.isValid());
        assertTrue(
            result.getErrors().stream()
                .anyMatch(
                    e -> e.contains(
                        "does not exist"
                    )
                )
        );
    }
}
