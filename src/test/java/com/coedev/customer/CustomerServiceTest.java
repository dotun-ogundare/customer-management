package com.coedev.customer;

import com.coedev.exception.DuplicateResourceException;
import com.coedev.exception.RequestValidationException;
import com.coedev.exception.ResourceNotfoundException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerDao customerDao;
    private CustomerService underTest;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }


    @Test
    void itShouldGetAllCustomers() {
        //WHEN
        underTest.getAllCustomers();

        //THEN
        Mockito.verify(customerDao).selectAllCustomers();
    }

    @Test
    void itShouldGetCustomerById() {
        //GIVEN
        int id = 2;
        Customer customer = new Customer(
                id, "Alex", "alex@gmail.com", 19
        );

        //WHEN
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        Customer actual = underTest.getCustomerById(2);

        //THEN
        Assertions.assertThat(actual).isEqualTo(customer);
    }

    @Test
    void itShouldThrowWhenGetCustomerReturnsEmptyOptional() {
        //GIVEN
        int id = 2;


        //WHEN
        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        //THEN
        Assertions.assertThatThrownBy(() -> underTest.getCustomerById(id))
                .isInstanceOf(ResourceNotfoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void itShouldAddCustomer() {
        //GIVEN
        String email = "ajero@gmail.com";
        Mockito.when(customerDao.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 19);
        //WHEN
        underTest.addCustomer(request);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        Mockito.verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getId()).isNull();
        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(request.age());

    }

    @Test
    void itShouldThrowWhenEmailExistWhileAddingACustomer() {
        //GIVEN
        String email = "ajero@gmail.com";
        Mockito.when(customerDao.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "alex", email, 19);
        //WHEN
        Assertions.assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Email already taken");

        //THEN
        Mockito.verify(customerDao, Mockito.never()).insertCustomer(Mockito.any());


    }

    @Test
    void itShouldDeleteCustomerById() {
        //GIVEN
        int id = 11;

        Mockito.when(customerDao.existPersonWithId(id)).thenReturn(true); // ensure the person exist

        //WHEN
        underTest.deleteCustomerById(id);

        //THEN
        Mockito.verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void itShouldThrowWhenDeleteCustomerByIdNotExist() {
        //GIVEN
        int id = 11;

        Mockito.when(customerDao.existPersonWithId(id)).thenReturn(false);

        //WHEN
        Assertions.assertThatThrownBy(() -> underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotfoundException.class)
                        .hasMessage("customer with id [%s] not found".formatted(id));
        //THEN
        Mockito.verify(customerDao, Mockito.never()).deleteCustomerById(id);
    }

    @Test
    void itShouldUpdateAllCustomerProperties() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        String newEmail = "sh@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Sholape", newEmail, 21);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());

    }

    @Test
    void itShouldUpdateOnlyCustomerName() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "Sholape", null, null);

        //When
        underTest.updateCustomer(id, updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(updateRequest.name());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateOnlyEmail() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        String newEmail = "sh@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        //When
        underTest.updateCustomer(id, updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void itShouldUpdateOnlyCustomerAge() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, null, 18);

        //When
        underTest.updateCustomer(id, updateRequest);

        //THEN
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        Mockito.verify(customerDao).updateCustomer(customerArgumentCaptor.capture());
        Customer capturedCustomer = customerArgumentCaptor.getValue();

        Assertions.assertThat(capturedCustomer.getName()).isEqualTo(customer.getName());
        Assertions.assertThat(capturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        Assertions.assertThat(capturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void itShouldThrowWhenUpdateEmailThatIsAlreadyTaken() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        String newEmail = "sh@gmail.com";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                null, newEmail, null);
        Mockito.when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);

        //When
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");

        //THEN
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());

    }

    @Test
    void itShouldThrowWhenCustomerUpdateHasNoChanges() {
        //GIVEN
        int id = 10;
        Customer customer = new Customer(
                id, "shola", "alao@gmail.com", 17
        );

        Mockito.when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //WHEN
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());


        //When
        Assertions.assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        //THEN
        Mockito.verify(customerDao, Mockito.never()).updateCustomer(Mockito.any());

    }



}