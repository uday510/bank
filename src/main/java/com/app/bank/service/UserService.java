package com.app.bank.service;

import com.app.bank.dto.RegisterUserRequest;
import com.app.bank.model.Customer;
import com.app.bank.repository.CustomerRepository;
import com.app.bank.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;

    public void registerUser(RegisterUserRequest request) {
        if (customerRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already in use");
        }

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setEmail(request.email());
        customer.setPassword(passwordEncoder.encode(request.password()));
        customer.setCreatedAt(DateTimeUtils.getUTCDate());

        customerRepository.save(customer);
    }

    public void createUser(Customer customer) {
        try {
            saveCustomerWithEncodedPassword(customer);
        } catch (Exception e) {
            throw e;
        }
    }

    private void saveCustomerWithEncodedPassword(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(DateTimeUtils.getUTCDate());
        customerRepository.save(customer);
    }

    public Optional<Customer> getUserByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
