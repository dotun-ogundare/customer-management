package com.coedev;

import com.coedev.customer.Customer;
import com.coedev.customer.CustomerRepository;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;

import java.beans.BeanProperty;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            /*Customer alex = new Customer("Alex", "alex@gmail.com", 21);
            Customer jamila = new Customer("Jamila", "jamila@gmail.com", 22);
            Customer omolola = new Customer("Omolola", "omolola@gmail.com", 23);

            List<Customer> customers = List.of(alex, jamila, omolola);
            customerRepository.saveAll(customers); */

            Random random = new Random();
            var faker = new Faker();
            var name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            String fullName = firstName + " " + lastName;
            Customer customer = new Customer(
                    fullName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@coedev.com",
                    //faker.internet().safeEmailAddress(),
                    random.nextInt(16, 99)
            );
            customerRepository.save(customer);
        };
    }

}
