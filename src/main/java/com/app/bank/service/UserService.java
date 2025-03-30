package com.app.bank.service;

import com.app.bank.dto.LoginRequest;
import com.app.bank.dto.RegisterUserRequest;
import com.app.bank.model.Customer;
import com.app.bank.repository.CustomerRepository;
import com.app.bank.security.JwtUtil;
import com.app.bank.util.DateTimeUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

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
        Optional<Customer> existingCustomer = customerRepository.findByEmail(customer.getEmail());
        if (existingCustomer.isPresent()) {
            throw new RuntimeException("Email is already in use");
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setCreatedAt(DateTimeUtils.getUTCDate());
        customerRepository.save(customer);
    }

    public Optional<Customer> getUserByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    public String login(LoginRequest loginRequest) {
        String jwt = null;
        Authentication authentication = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.email(), loginRequest.password());
        try {
            Authentication authenticationResponse = authenticationManager.authenticate(authentication);
            jwt = JwtUtil.generateToken(authenticationResponse);
        } catch (Exception e) {
            throw new RuntimeException("Invalid credentials");
        }
        return jwt;
    }
}
