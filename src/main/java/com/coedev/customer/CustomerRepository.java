package com.coedev.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//@Repository // annotation not needed because it is given to us by defaul
public interface CustomerRepository
        extends JpaRepository<Customer, Integer> {

    boolean existsCustomersByEmail(String email);
    boolean existsCustomersById(Integer id);
}
