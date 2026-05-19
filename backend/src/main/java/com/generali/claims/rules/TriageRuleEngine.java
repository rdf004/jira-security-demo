package com.generali.claims.rules;

import com.fasterxml.jackson.databind
    .ObjectMapper;
import com.generali.claims.model.Claim;
import com.generali.claims.model.TriageResult;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class TriageRuleEngine {

    private TriageRuleConfig config;

    @PostConstruct
    public void init() {
        try {
            InputStream is = getClass()
                .getClassLoader()
                .getResourceAsStream(
                    "rules/triage-rules.json"
                );
            if (is != null) {
                ObjectMapper mapper =
                    new ObjectMapper();
                config = mapper.readValue(
                    is,
                    TriageRuleConfig.class
                );
            } else {
                config =
                    new TriageRuleConfig();
            }
        } catch (Exception e) {
            config = new TriageRuleConfig();
        }
    }

    public TriageRuleConfig getConfig() {
        return config;
    }

    public void setConfig(
        TriageRuleConfig config
    ) {
        this.config = config;
    }

    public TriageResult evaluate(
        Claim claim,
        boolean hasDuplicate,
        List<String> reasons
    ) {
        BigDecimal amount =
            claim.getEstimatedLoss();
        boolean hasMissingDocs =
            claim.getMissingDocuments() != null
            && !claim.getMissingDocuments()
                .isEmpty();

        if (hasDuplicate) {
            reasons.add(
                "Duplicate claim detected "
                + "within "
                + config
                    .getDuplicateWindowDays()
                + " days"
            );
            return TriageResult
                .FRAUD_MANUAL_INVESTIGATION;
        }

        if (!claim.getPolicy().isActive()) {
            reasons.add(
                "Policy is expired/inactive"
            );
            return TriageResult
                .FRAUD_MANUAL_INVESTIGATION;
        }

        if (amount.compareTo(
            config.getFraudThresholdAmount()
        ) > 0) {
            reasons.add(
                "Unusually high claim amount"
                + " ($" + amount + " exceeds"
                + " $"
                + config
                    .getFraudThresholdAmount()
                + " threshold)"
            );
            return TriageResult
                .FRAUD_MANUAL_INVESTIGATION;
        }

        if (amount.compareTo(
            config
                .getStraightThroughMaxAmount()
        ) <= 0 && !hasMissingDocs) {
            reasons.add(
                "Low-value claim ($"
                + amount + " <= $"
                + config
                    .getStraightThroughMaxAmount()
                + ") with complete documents"
            );
            return TriageResult
                .STRAIGHT_THROUGH_PROCESSING;
        }

        if (hasMissingDocs) {
            reasons.add(
                "Missing required documents"
            );
        }
        if (amount.compareTo(
            config.getAdjusterReviewMinAmount()
        ) > 0) {
            reasons.add(
                "Medium/high-value claim"
                + " requires adjuster review"
            );
        }

        reasons.add(
            "Claim routed to adjuster for"
            + " manual review"
        );
        return TriageResult.ADJUSTER_REVIEW;
    }
}
