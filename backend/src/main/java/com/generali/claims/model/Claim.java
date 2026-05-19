package com.generali.claims.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "claims")
public class Claim {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(unique = true)
    private String claimNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "policy_id")
    private Policy policy;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClaimType claimType;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date incidentDate;

    private String incidentLocation;

    @Column(length = 2000)
    private String description;

    @NotNull
    private BigDecimal estimatedLoss;

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    @CollectionTable(
        name = "claim_documents",
        joinColumns = @JoinColumn(
            name = "claim_id"
        )
    )
    @Column(name = "document_name")
    private Set<String> documents =
        new LinkedHashSet<>();

    @Enumerated(EnumType.STRING)
    private ClaimStatus status =
        ClaimStatus.SUBMITTED;

    @Enumerated(EnumType.STRING)
    private TriageResult triageResult;

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    @CollectionTable(
        name = "claim_triage_reasons",
        joinColumns = @JoinColumn(
            name = "claim_id"
        )
    )
    @Column(name = "reason")
    private Set<String> triageReasons =
        new LinkedHashSet<>();

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    @CollectionTable(
        name = "claim_missing_docs",
        joinColumns = @JoinColumn(
            name = "claim_id"
        )
    )
    @Column(name = "document_name")
    private Set<String> missingDocuments =
        new LinkedHashSet<>();

    @Column(length = 3000)
    private String adjusterSummary;

    @Temporal(TemporalType.TIMESTAMP)
    private Date submittedAt;

    public Claim() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClaimNumber() {
        return claimNumber;
    }

    public void setClaimNumber(
        String claimNumber
    ) {
        this.claimNumber = claimNumber;
    }

    public Policy getPolicy() {
        return policy;
    }

    public void setPolicy(Policy policy) {
        this.policy = policy;
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
        String incidentLocation
    ) {
        this.incidentLocation = incidentLocation;
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

    public Set<String> getDocuments() {
        return documents;
    }

    public void setDocuments(
        Set<String> documents
    ) {
        this.documents = documents;
    }

    public ClaimStatus getStatus() {
        return status;
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public TriageResult getTriageResult() {
        return triageResult;
    }

    public void setTriageResult(
        TriageResult triageResult
    ) {
        this.triageResult = triageResult;
    }

    public Set<String> getTriageReasons() {
        return triageReasons;
    }

    public void setTriageReasons(
        Set<String> triageReasons
    ) {
        this.triageReasons = triageReasons;
    }

    public Set<String> getMissingDocuments() {
        return missingDocuments;
    }

    public void setMissingDocuments(
        Set<String> missingDocuments
    ) {
        this.missingDocuments = missingDocuments;
    }

    public String getAdjusterSummary() {
        return adjusterSummary;
    }

    public void setAdjusterSummary(
        String adjusterSummary
    ) {
        this.adjusterSummary = adjusterSummary;
    }

    public Date getSubmittedAt() {
        return submittedAt;
    }

    public void setSubmittedAt(
        Date submittedAt
    ) {
        this.submittedAt = submittedAt;
    }
}
