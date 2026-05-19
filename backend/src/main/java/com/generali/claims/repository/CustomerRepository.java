package com.generali.claims.repository;

import com.generali.claims.model.Customer;
import org.springframework.data.jpa
    .repository.JpaRepository;
import org.springframework.stereotype
    .Repository;

@Repository
public interface CustomerRepository
    extends JpaRepository<Customer, Long> {
}
