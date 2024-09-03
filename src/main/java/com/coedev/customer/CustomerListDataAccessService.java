package com.coedev.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("List")
public class CustomerListDataAccessService implements CustomerDao{

    private static List<Customer> customers;
    static {
        customers = new ArrayList<>();
        Customer alex = new Customer(1, "Alex", "alex@gmail.com", 21);
        Customer jamila = new Customer(2, "Jamila", "jamila@gmail.com", 22);
        Customer omolola = new Customer(3, "Omolola", "omolola@gmail.com", 23);

        customers.add(alex);
        customers.add(jamila);
        customers.add(omolola);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream().filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));

    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        customers.stream()
                .filter(c -> c.getId().equals(customerId))
                .findFirst()
                .ifPresent(o -> customers.remove(o)); // try Alt + Enter
                //.ifPresent(customers::remove); //

    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}
