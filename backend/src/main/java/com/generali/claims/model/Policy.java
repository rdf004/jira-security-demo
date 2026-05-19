package com.generali.claims.model;

import java.util.Date;
import java.util.List;
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
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "policies")
public class Policy {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String policyNumber;

    @NotBlank
    private String policyType;

    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    private boolean active;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @NotNull
    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    @CollectionTable(
        name = "policy_covered_types",
        joinColumns = @JoinColumn(
            name = "policy_id"
        )
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "claim_type")
    private List<ClaimType> coveredClaimTypes;

    @ElementCollection(
        fetch = FetchType.EAGER
    )
    @CollectionTable(
        name = "policy_required_docs",
        joinColumns = @JoinColumn(
            name = "policy_id"
        )
    )
    @Column(name = "document_name")
    private List<String> requiredDocuments;

    public Policy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(
        String policyNumber
    ) {
        this.policyNumber = policyNumber;
    }

    public String getPolicyType() {
        return policyType;
    }

    public void setPolicyType(
        String policyType
    ) {
        this.policyType = policyType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public List<ClaimType> getCoveredClaimTypes() {
        return coveredClaimTypes;
    }

    public void setCoveredClaimTypes(
        List<ClaimType> coveredClaimTypes
    ) {
        this.coveredClaimTypes = coveredClaimTypes;
    }

    public List<String> getRequiredDocuments() {
        return requiredDocuments;
    }

    public void setRequiredDocuments(
        List<String> requiredDocuments
    ) {
        this.requiredDocuments = requiredDocuments;
    }
}
