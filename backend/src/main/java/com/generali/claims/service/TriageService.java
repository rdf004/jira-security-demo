package com.generali.claims.service;

import com.generali.claims.model.Claim;
import com.generali.claims.model.ClaimType;
import com.generali.claims.model.TriageResult;
import com.generali.claims.repository
    .ClaimRepository;
import com.generali.claims.rules
    .TriageRuleEngine;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TriageService {

    private final ClaimRepository claimRepo;
    private final TriageRuleEngine ruleEngine;

    public TriageService(
        ClaimRepository claimRepo,
        TriageRuleEngine ruleEngine
    ) {
        this.claimRepo = claimRepo;
        this.ruleEngine = ruleEngine;
    }

    public TriageResult triage(
        Claim claim,
        List<String> reasons
    ) {
        boolean hasDuplicate =
            checkDuplicateClaim(claim);
        return ruleEngine.evaluate(
            claim, hasDuplicate, reasons
        );
    }

    private boolean checkDuplicateClaim(
        Claim claim
    ) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -30);
        Date thirtyDaysAgo = cal.getTime();

        List<Claim> recent =
            claimRepo.findRecentByPolicyAndType(
                claim.getPolicy().getId(),
                claim.getClaimType(),
                thirtyDaysAgo
            );
        return recent != null
            && !recent.isEmpty();
    }

    public String generateAdjusterSummary(
        Claim claim
    ) {
        StringBuilder sb = new StringBuilder();
        sb.append("CLAIM SUMMARY\n");
        sb.append("=============\n\n");

        sb.append("Claim Number: ");
        sb.append(claim.getClaimNumber());
        sb.append("\n");

        sb.append("Customer: ");
        sb.append(
            claim.getPolicy().getCustomer()
                .getFullName()
        );
        sb.append("\n");

        sb.append("Policy: ");
        sb.append(
            claim.getPolicy().getPolicyNumber()
        );
        sb.append(" (");
        sb.append(
            claim.getPolicy().getPolicyType()
        );
        sb.append(")\n");

        sb.append("Claim Type: ");
        sb.append(
            claim.getClaimType()
                .getDisplayName()
        );
        sb.append("\n");

        sb.append("Incident Date: ");
        sb.append(claim.getIncidentDate());
        sb.append("\n");

        sb.append("Location: ");
        sb.append(claim.getIncidentLocation());
        sb.append("\n");

        sb.append("Estimated Loss: $");
        sb.append(claim.getEstimatedLoss());
        sb.append("\n\n");

        sb.append("DESCRIPTION\n");
        sb.append("-----------\n");
        sb.append(claim.getDescription());
        sb.append("\n\n");

        sb.append("TRIAGE RESULT: ");
        if (claim.getTriageResult() != null) {
            sb.append(
                claim.getTriageResult()
                    .getDisplayName()
            );
        }
        sb.append("\n\n");

        if (claim.getTriageReasons() != null
            && !claim.getTriageReasons()
                .isEmpty()) {
            sb.append("ROUTING REASONS\n");
            sb.append("---------------\n");
            for (String r :
                claim.getTriageReasons()) {
                sb.append("- ");
                sb.append(r);
                sb.append("\n");
            }
            sb.append("\n");
        }

        if (claim.getMissingDocuments() != null
            && !claim.getMissingDocuments()
                .isEmpty()) {
            sb.append("MISSING DOCUMENTS\n");
            sb.append("-----------------\n");
            for (String d :
                claim.getMissingDocuments()) {
                sb.append("- ");
                sb.append(d);
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}
