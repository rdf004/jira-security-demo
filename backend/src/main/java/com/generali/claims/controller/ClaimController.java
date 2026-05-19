package com.generali.claims.controller;

import com.generali.claims.model.Claim;
import com.generali.claims.model
    .ClaimSubmissionRequest;
import com.generali.claims.model
    .ValidationResult;
import com.generali.claims.service
    .ClaimService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation
    .CrossOrigin;
import org.springframework.web.bind.annotation
    .GetMapping;
import org.springframework.web.bind.annotation
    .PathVariable;
import org.springframework.web.bind.annotation
    .PostMapping;
import org.springframework.web.bind.annotation
    .RequestBody;
import org.springframework.web.bind.annotation
    .RequestMapping;
import org.springframework.web.bind.annotation
    .RequestParam;
import org.springframework.web.bind.annotation
    .RestController;

@RestController
@RequestMapping("/api/claims")
@CrossOrigin(origins = "*")
public class ClaimController {

    private final ClaimService service;

    @PersistenceContext
    private EntityManager entityManager;

    public ClaimController(
        ClaimService service
    ) {
        this.service = service;
    }

    @GetMapping
    public List<Claim> getAll() {
        return service.getAllClaims();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Claim> getById(
        @PathVariable Long id
    ) {
        return service.getClaimById(id)
            .map(ResponseEntity::ok)
            .orElse(
                ResponseEntity.notFound()
                    .build()
            );
    }

    @PostMapping
    public ResponseEntity<?> submit(
        @RequestBody
        ClaimSubmissionRequest request
    ) {
        Object result =
            service.submitClaim(request);
        if (result
            instanceof ValidationResult) {
            return ResponseEntity
                .status(
                    HttpStatus.BAD_REQUEST
                )
                .body(result);
        }
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result);
    }

    @SuppressWarnings("unchecked")
    @GetMapping("/search")
    public List<Claim> search(
        @RequestParam String keyword
    ) {
        String sql =
            "SELECT * FROM claims"
            + " WHERE description LIKE ?1";
        return entityManager
            .createNativeQuery(
                sql, Claim.class
            )
            .setParameter(
                1, "%" + keyword + "%"
            )
            .getResultList();
    }
}
