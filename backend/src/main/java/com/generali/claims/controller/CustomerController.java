package com.generali.claims.controller;

import com.generali.claims.model.Customer;
import com.generali.claims.repository
    .CustomerRepository;
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
    .RestController;

@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository repo;

    public CustomerController(
        CustomerRepository repo
    ) {
        this.repo = repo;
    }

    @GetMapping
    public List<Customer> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Customer> getById(
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
