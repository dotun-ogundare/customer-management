package com.coedev.customer;

import com.coedev.AbstractTestcontainersUnitTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainersUnitTest {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void itShouldSelectAllCustomers() {
        //GIVEN
        Customer customer = new Customer(
                FAKER.name().fullName(),
                FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);

        //WHEN
        List<Customer> customers = underTest.selectAllCustomers();

        Assertions.assertThat(customers).isNotEmpty();

        //THEN
    }

    @Test
    void itShouldSelectCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //When
        Optional<Customer> actual = underTest.selectCustomerById(id);

        //Then
        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
           Assertions.assertThat(c.getId()).isEqualTo(id);
           Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
           Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldReturnEmptyWhenSelectCustomerById() {
        //GIVEN
        int id = -1;

        //WHEN
        var actual = underTest.selectCustomerById(id);

        //THEN
        Assertions.assertThat(actual).isEmpty();
    }

    @Test
    void itShouldInsertCustomer() {
        //GIVEN

        //WHEN

        //THEN
    }

    @Test
    void itShouldExistPersonWithEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        //WHEN
        boolean actual = underTest.existPersonWithEmail(email);

        //THEN
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void itShouldExistCustomerWithId() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        var actual = underTest.existPersonWithId(id);

        //THEN
        Assertions.assertThat(actual).isTrue();
    }

    @Test
    void itShouldReturnFalseWhenIdNotPresent() {
        //GIVEN
        int id = -1;

        //WHEN
        var actual = underTest.existPersonWithId(id);

        //THEN
        Assertions.assertThat(actual).isFalse();
    }

    @Test
    void itShouldDeleteCustomerById() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);
        Assertions.assertThat(actual).isNotPresent();
    }

    @Test
    void itShouldUpdateCustomerName() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newName = "Omoterinwa";

        //WHEN name is changed
        Customer update = new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
           Assertions.assertThat(c.getId()).isEqualTo(id);
           Assertions.assertThat(c.getName()).isEqualTo(newName);
           Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
           Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldUpdateCustomerEmail() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newEmail = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();

        //WHEN email is changed
        Customer update = new Customer();
        update.setId(id);
        update.setEmail(newEmail
        );

        underTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
           Assertions.assertThat(c.getId()).isEqualTo(id);
           Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
           Assertions.assertThat(c.getEmail()).isEqualTo(newEmail); // changed
           Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void itShouldUpdateCustomerAge() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        var newAge = 21;

        //WHEN age is changed
        Customer update = new Customer();
        update.setId(id);
        update.setAge(newAge);

        underTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(newAge); //changed
        });
    }

    @Test
    void itShouldUpdateAllProperties() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        //WHEN update with new name, age and email
        Customer update = new Customer();
        update.setId(id);
        update.setName("Omosholape");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(129);


        underTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValue(update);
    }

    //Test to Ensure it will not update when nothing to update

    @Test
    void itShouldNotUpdateWhenNothingToUpdate() {
        //GIVEN
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        Customer customer = new Customer(
                FAKER.name().fullName(),
                email,
                20
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        //WHEN nothing to update
        Customer update = new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        //THEN
        Optional<Customer> actual = underTest.selectCustomerById(id);

        Assertions.assertThat(actual).isPresent().hasValueSatisfying(c -> {
            Assertions.assertThat(c.getId()).isEqualTo(id);
            Assertions.assertThat(c.getName()).isEqualTo(customer.getName());
            Assertions.assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            Assertions.assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }
}