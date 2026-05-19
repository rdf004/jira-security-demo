package com.generali.claims.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ClaimSubmissionRequest {

    private Long policyId;
    private ClaimType claimType;
    private Date incidentDate;
    private String incidentLocation;
    private String description;
    private BigDecimal estimatedLoss;
    private List<String> documents;

    public Long getPolicyId() {
        return policyId;
    }

    public void setPolicyId(Long policyId) {
        this.policyId = policyId;
    }

    public ClaimType getClaimType() {
        return claimType;
    }

    public void setClaimType(
        ClaimType claimType
    ) {
        this.claimType = claimType;
    }

    public Date getIncidentDate() {
        return incidentDate;
    }

    public void setIncidentDate(
        Date incidentDate
    ) {
        this.incidentDate = incidentDate;
    }

    public String getIncidentLocation() {
        return incidentLocation;
    }

    public void setIncidentLocation(
        String location
    ) {
        this.incidentLocation = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(
        String description
    ) {
        this.description = description;
    }

    public BigDecimal getEstimatedLoss() {
        return estimatedLoss;
    }

    public void setEstimatedLoss(
        BigDecimal estimatedLoss
    ) {
        this.estimatedLoss = estimatedLoss;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(
        List<String> documents
    ) {
        this.documents = documents;
    }
}
