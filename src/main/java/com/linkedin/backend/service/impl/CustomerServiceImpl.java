package com.linkedin.backend.service.impl;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.entities.Customer;
import com.linkedin.backend.exception.CustomerNotFoundException;
import com.linkedin.backend.exception.EmailAlreadyExistsException;
import com.linkedin.backend.mapper.CustomerMapper;
import com.linkedin.backend.repositories.CustomerRepository;
import com.linkedin.backend.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Page<CustomerDto> selectAllCustomers(Pageable pageable) {
        log.info("Fetching customers from database with pagination and sorting");
        return customerRepository.findAll(pageable)
                .map(CustomerMapper.INSTANCE::toCustomerDto);
    }

    @Override
    public Optional<CustomerDto> selectCustomerById(Integer customerId) {
        log.info("Fetching customer by ID: {}", customerId);
        return Optional.ofNullable(customerRepository.findById(customerId)
                .map(CustomerMapper.INSTANCE::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with ID: " + customerId)));
    }

    @Override
    public void insertCustomer(CustomerDto customerDto) {
        log.info("Inserting a new customer with email: {}", customerDto.getEmail());
        if (existsCustomerWithEmail(customerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + customerDto.getEmail());
        }
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        log.info("Checking if a customer exists with email: {}", email);
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        log.info("Checking if a customer exists with ID: {}", customerId);
        return customerRepository.existsById(customerId);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(CustomerDto customerDto) {
        log.info("Updating customer with ID: {}", customerDto.getId());
        if (!customerRepository.existsById(customerDto.getId())) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerDto.getId());
        }
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto selectUserByEmail(String email) {
        log.info("Fetching customer by email: {}", email);
        return customerRepository.findByEmail(email)
                .map(CustomerMapper.INSTANCE::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
    }

}
