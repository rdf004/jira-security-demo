package com.generali.claims.service;

import com.generali.claims.model.ClaimType;
import com.generali.claims.model.Policy;
import com.generali.claims.model.ValidationResult;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class PolicyValidationService {

    public ValidationResult validate(
        Policy policy,
        ClaimType claimType,
        Date incidentDate,
        List<String> submittedDocs
    ) {
        ValidationResult result =
            new ValidationResult();

        validatePolicyExists(policy, result);
        if (!result.isValid()) {
            return result;
        }

        validatePolicyActive(policy, result);
        validateCoverageDates(
            policy, incidentDate, result
        );
        validateClaimTypeCoverage(
            policy, claimType, result
        );
        validateRequiredDocuments(
            policy, submittedDocs, result
        );

        return result;
    }

    public void validatePolicyExists(
        Policy policy,
        ValidationResult result
    ) {
        if (policy == null) {
            result.addError(
                "Policy does not exist"
            );
        }
    }

    public void validatePolicyActive(
        Policy policy,
        ValidationResult result
    ) {
        if (policy != null && !policy.isActive()) {
            result.addError(
                "Policy is not active"
            );
        }
    }

    public void validateCoverageDates(
        Policy policy,
        Date incidentDate,
        ValidationResult result
    ) {
        if (policy == null
            || incidentDate == null) {
            return;
        }
        if (incidentDate.before(
            policy.getStartDate()
        )) {
            result.addError(
                "Incident date is before "
                + "policy coverage start date"
            );
        }
        if (incidentDate.after(
            policy.getEndDate()
        )) {
            result.addError(
                "Incident date is after "
                + "policy coverage end date"
            );
        }
    }

    public void validateClaimTypeCoverage(
        Policy policy,
        ClaimType claimType,
        ValidationResult result
    ) {
        if (policy == null
            || claimType == null) {
            return;
        }
        List<ClaimType> covered =
            policy.getCoveredClaimTypes();
        if (covered == null
            || !covered.contains(claimType)) {
            result.addError(
                "Claim type '"
                + claimType.getDisplayName()
                + "' is not covered by "
                + "this policy"
            );
        }
    }

    public void validateRequiredDocuments(
        Policy policy,
        List<String> submittedDocs,
        ValidationResult result
    ) {
        if (policy == null) {
            return;
        }
        List<String> required =
            policy.getRequiredDocuments();
        if (required == null
            || required.isEmpty()) {
            return;
        }
        for (String doc : required) {
            boolean found = submittedDocs != null
                && submittedDocs.stream()
                    .anyMatch(
                        s -> s.equalsIgnoreCase(
                            doc
                        )
                    );
            if (!found) {
                result.addMissingDocument(doc);
            }
        }
    }
}
