package com.linkedin.backend.service;

import com.linkedin.backend.dto.CustomerDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    List<CustomerDto> selectAllCustomers();
    Optional<CustomerDto> selectCustomerById(Integer customerId);
    void insertCustomer(CustomerDto customerDto);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerById(Integer customerId);
    void deleteCustomerById(Integer customerId);
    void updateCustomer(CustomerDto customerDto);
    CustomerDto selectUserByEmail(String email);

}
