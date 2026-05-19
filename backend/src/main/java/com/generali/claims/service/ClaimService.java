package com.generali.claims.service;

import com.generali.claims.model.Claim;
import com.generali.claims.model
    .ClaimSubmissionRequest;
import com.generali.claims.model.ClaimStatus;
import com.generali.claims.model.Policy;
import com.generali.claims.model.TriageResult;
import com.generali.claims.model
    .ValidationResult;
import com.generali.claims.repository
    .ClaimRepository;
import com.generali.claims.repository
    .PolicyRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic
    .AtomicInteger;
import org.springframework.stereotype.Service;

@Service
public class ClaimService {

    private final ClaimRepository claimRepo;
    private final PolicyRepository policyRepo;
    private final PolicyValidationService
        validationService;
    private final TriageService triageService;
    private final AtomicInteger claimCounter =
        new AtomicInteger(1000);

    public ClaimService(
        ClaimRepository claimRepo,
        PolicyRepository policyRepo,
        PolicyValidationService validationSvc,
        TriageService triageSvc
    ) {
        this.claimRepo = claimRepo;
        this.policyRepo = policyRepo;
        this.validationService = validationSvc;
        this.triageService = triageSvc;
    }

    public List<Claim> getAllClaims() {
        return claimRepo.findAll();
    }

    public Optional<Claim> getClaimById(
        Long id
    ) {
        return claimRepo.findById(id);
    }

    public Optional<Claim> getClaimByNumber(
        String claimNumber
    ) {
        return claimRepo
            .findByClaimNumber(claimNumber);
    }

    public Object submitClaim(
        ClaimSubmissionRequest request
    ) {
        Optional<Policy> policyOpt =
            policyRepo.findById(
                request.getPolicyId()
            );

        if (!policyOpt.isPresent()) {
            ValidationResult vr =
                new ValidationResult();
            vr.addError(
                "Policy not found with ID: "
                + request.getPolicyId()
            );
            return vr;
        }

        Policy policy = policyOpt.get();
        ValidationResult validation =
            validationService.validate(
                policy,
                request.getClaimType(),
                request.getIncidentDate(),
                request.getDocuments()
            );

        if (!validation.isValid()) {
            return validation;
        }

        Claim claim = new Claim();
        claim.setClaimNumber(
            generateClaimNumber()
        );
        claim.setPolicy(policy);
        claim.setClaimType(
            request.getClaimType()
        );
        claim.setIncidentDate(
            request.getIncidentDate()
        );
        claim.setIncidentLocation(
            request.getIncidentLocation()
        );
        claim.setDescription(
            request.getDescription()
        );
        claim.setEstimatedLoss(
            request.getEstimatedLoss()
        );
        claim.setDocuments(
            request.getDocuments() != null
                ? request.getDocuments()
                : new ArrayList<>()
        );
        claim.setMissingDocuments(
            validation.getMissingDocuments()
        );
        claim.setSubmittedAt(new Date());
        claim.setStatus(
            ClaimStatus.VALIDATED
        );

        List<String> reasons =
            new ArrayList<>();
        TriageResult triageResult =
            triageService.triage(
                claim, reasons
            );
        claim.setTriageResult(triageResult);
        claim.setTriageReasons(reasons);

        if (triageResult == TriageResult
            .STRAIGHT_THROUGH_PROCESSING) {
            claim.setStatus(
                ClaimStatus.STRAIGHT_THROUGH
            );
        } else if (triageResult == TriageResult
            .ADJUSTER_REVIEW) {
            claim.setStatus(
                ClaimStatus.ADJUSTER_REVIEW
            );
        } else {
            claim.setStatus(
                ClaimStatus.FRAUD_INVESTIGATION
            );
        }

        claim.setAdjusterSummary(
            triageService
                .generateAdjusterSummary(claim)
        );

        return claimRepo.save(claim);
    }

    private String generateClaimNumber() {
        int num = claimCounter.incrementAndGet();
        return "CLM-2025-" + num;
    }
}
