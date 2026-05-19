package com.generali.claims.model;

public enum TriageResult {
    STRAIGHT_THROUGH_PROCESSING(
        "Straight-Through Processing"
    ),
    ADJUSTER_REVIEW("Adjuster Review"),
    FRAUD_MANUAL_INVESTIGATION(
        "Fraud/Manual Investigation"
    );

    private final String displayName;

    TriageResult(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
