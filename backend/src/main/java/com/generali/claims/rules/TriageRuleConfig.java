package com.generali.claims.rules;

import java.math.BigDecimal;

public class TriageRuleConfig {

    private BigDecimal straightThroughMaxAmount =
        new BigDecimal("2000");

    private BigDecimal adjusterReviewMinAmount =
        new BigDecimal("2000");

    private BigDecimal adjusterReviewMaxAmount =
        new BigDecimal("25000");

    private BigDecimal fraudThresholdAmount =
        new BigDecimal("25000");

    private int duplicateWindowDays = 30;

    public BigDecimal
        getStraightThroughMaxAmount() {
        return straightThroughMaxAmount;
    }

    public void setStraightThroughMaxAmount(
        BigDecimal amount
    ) {
        this.straightThroughMaxAmount = amount;
    }

    public BigDecimal
        getAdjusterReviewMinAmount() {
        return adjusterReviewMinAmount;
    }

    public void setAdjusterReviewMinAmount(
        BigDecimal amount
    ) {
        this.adjusterReviewMinAmount = amount;
    }

    public BigDecimal
        getAdjusterReviewMaxAmount() {
        return adjusterReviewMaxAmount;
    }

    public void setAdjusterReviewMaxAmount(
        BigDecimal amount
    ) {
        this.adjusterReviewMaxAmount = amount;
    }

    public BigDecimal
        getFraudThresholdAmount() {
        return fraudThresholdAmount;
    }

    public void setFraudThresholdAmount(
        BigDecimal amount
    ) {
        this.fraudThresholdAmount = amount;
    }

    public int getDuplicateWindowDays() {
        return duplicateWindowDays;
    }

    public void setDuplicateWindowDays(
        int days
    ) {
        this.duplicateWindowDays = days;
    }
}
