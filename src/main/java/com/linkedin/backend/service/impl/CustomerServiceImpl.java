package com.linkedin.backend.service.impl;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.entities.Customer;
import com.linkedin.backend.exception.CustomerNotFoundException;
import com.linkedin.backend.exception.EmailAlreadyExistsException;
import com.linkedin.backend.mapper.CustomerMapper;
import com.linkedin.backend.repositories.CustomerRepository;
import com.linkedin.backend.service.CustomerService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDto> selectAllCustomers() {
        return customerRepository.findAll().stream()
                .map(CustomerMapper.INSTANCE::toCustomerDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CustomerDto> selectCustomerById(Integer customerId) {
        return customerRepository.findById(customerId)
                .map(CustomerMapper.INSTANCE::toCustomerDto)
                .or(() -> {
                    throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });
    }

    @Override
    public void insertCustomer(CustomerDto customerDto) {
        if (existsCustomerWithEmail(customerDto.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + customerDto.getEmail());
        }
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return customerRepository.existsByEmail(email);
    }

    @Override
    public boolean existsCustomerById(Integer customerId) {
        return customerRepository.existsById(customerId);
    }

    @Override
    public void deleteCustomerById(Integer customerId) {
        if (!existsCustomerById(customerId)) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public void updateCustomer(CustomerDto customerDto) {
        if (!existsCustomerById(customerDto.getId())) {
            throw new CustomerNotFoundException("Customer not found with ID: " + customerDto.getId());
        }
        Customer customer = CustomerMapper.INSTANCE.toCustomer(customerDto);
        customerRepository.save(customer);
    }

    @Override
    public CustomerDto selectUserByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(CustomerMapper.INSTANCE::toCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found with email: " + email));
    }


}