package com.generali.claims.controller;

import com.generali.claims.model.Policy;
import com.generali.claims.repository
    .PolicyRepository;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation
    .CrossOrigin;
import org.springframework.web.bind.annotation
    .GetMapping;
import org.springframework.web.bind.annotation
    .PathVariable;
import org.springframework.web.bind.annotation
    .RequestMapping;
import org.springframework.web.bind.annotation
    .RequestParam;
import org.springframework.web.bind.annotation
    .RestController;

@RestController
@RequestMapping("/api/policies")
@CrossOrigin(origins = "*")
public class PolicyController {

    private final PolicyRepository repo;

    public PolicyController(
        PolicyRepository repo
    ) {
        this.repo = repo;
    }

    @GetMapping
    public List<Policy> getAll() {
        return repo.findAll();
    }

    @GetMapping("/active")
    public List<Policy> getActive() {
        return repo.findByActiveTrue();
    }

    @GetMapping("/customer/{customerId}")
    public List<Policy> getByCustomer(
        @PathVariable Long customerId
    ) {
        return repo
            .findByCustomerId(customerId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Policy> getById(
        @PathVariable Long id
    ) {
        return repo.findById(id)
            .map(ResponseEntity::ok)
            .orElse(
                ResponseEntity.notFound()
                    .build()
            );
    }
}
