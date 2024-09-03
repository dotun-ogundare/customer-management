package com.coedev.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void itShouldSelectAllCustomers() {
        //WHEN
        underTest.selectAllCustomers();

        //THEN
        Mockito.verify(customerRepository)
                .findAll();
    }

    @Test
    void itShouldSelectCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.selectCustomerById(id);
        //THEN
        Mockito.verify(customerRepository)
                .findById(id);
    }

    @Test
    void itShouldInsertCustomer() {
        //GIVEN
        Customer customer = new Customer(
                1, "Ali", "ali@gmail.com", 21
        );

        //WHEN
        underTest.insertCustomer(customer);

        //THEN
        Mockito.verify(customerRepository).save(customer);
    }

    @Test
    void itShouldExistPersonWithEmail() {
        //GIVEN
        String email = "omolola@gmail.com";

        //WHEN
        underTest.existPersonWithEmail(email);

        //THEN
        Mockito.verify(customerRepository).existsCustomersByEmail(email);
    }

    @Test
    void itShouldExistPersonWithId() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.existPersonWithId(id);

        //THEN
        Mockito.verify(customerRepository).existsCustomersById(id);
    }

    @Test
    void itShouldDeleteCustomerById() {
        //GIVEN
        int id = 1;

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        Mockito.verify(customerRepository).deleteById(id);
    }

    @Test
    void itShouldUpdateCustomer() {
        //GIVEN
        Customer customer = new Customer(
                1, "Alimoshuru", "alimosho@gmail.com", 31
        );

        //WHEN
        underTest.updateCustomer(customer);

        //THEN
        Mockito.verify(customerRepository).save(customer);
    }
}