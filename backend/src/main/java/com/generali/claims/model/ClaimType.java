package com.generali.claims.model;

public enum ClaimType {
    AUTO_COLLISION("Auto Collision"),
    HOME_WATER_DAMAGE("Home Water Damage"),
    TRAVEL_DELAYED_BAGGAGE(
        "Travel Delayed Baggage"
    ),
    HEALTH_REIMBURSEMENT(
        "Health Reimbursement"
    );

    private final String displayName;

    ClaimType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
