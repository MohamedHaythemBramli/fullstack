package com.linkedin.backend.service;

import com.linkedin.backend.dto.CustomerDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CustomerService {
    Page<CustomerDto> selectAllCustomers(Pageable pageable);
    Optional<CustomerDto> selectCustomerById(Integer customerId);
    void insertCustomer(CustomerDto customerDto);
    boolean existsCustomerWithEmail(String email);
    boolean existsCustomerById(Integer customerId);
    void deleteCustomerById(Integer customerId);
    void updateCustomer(CustomerDto customerDto);
    CustomerDto selectUserByEmail(String email);

}
