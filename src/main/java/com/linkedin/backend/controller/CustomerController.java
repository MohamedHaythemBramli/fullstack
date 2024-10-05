package com.linkedin.backend.controller;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.exception.CustomerNotFoundException;
import com.linkedin.backend.exception.WebResponse;
import com.linkedin.backend.service.CustomerService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/customers")
@Validated
@Slf4j
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<WebResponse<Page<CustomerDto>>> getAllCustomers(
            @PageableDefault(page = 0, size = 10, sort = "name") Pageable pageable) {
        log.info("Fetching all customers with pagination: page={}, size={}, sort={}",
                pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        Page<CustomerDto> customers = customerService.selectAllCustomers(pageable);
        WebResponse<Page<CustomerDto>> response = WebResponse.<Page<CustomerDto>>builder()
                .success(true)
                .data(customers)
                .message("Customers fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<WebResponse<CustomerDto>> getCustomerById(@PathVariable Integer customerId) {
        log.info("Fetching customer with ID: {}", customerId);
        CustomerDto customerDto = customerService.selectCustomerById(customerId)
                .orElseThrow(() -> {
                    log.error("Customer not found with ID: {}", customerId);
                    return new CustomerNotFoundException("Customer not found with ID: " + customerId);
                });
        WebResponse<CustomerDto> response = WebResponse.<CustomerDto>builder()
                .success(true)
                .data(customerDto)
                .message("Customer fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<WebResponse<Void>> createCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Creating a new customer with email: {}", customerDto.getEmail());
        customerService.insertCustomer(customerDto);
        WebResponse<Void> response = WebResponse.<Void>builder()
                .success(true)
                .message("Customer created successfully")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<WebResponse<Void>> updateCustomer(@Valid @RequestBody CustomerDto customerDto) {
        log.info("Updating customer with ID: {}", customerDto.getId());
        customerService.updateCustomer(customerDto);
        WebResponse<Void> response = WebResponse.<Void>builder()
                .success(true)
                .message("Customer updated successfully")
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<WebResponse<Void>> deleteCustomer(@PathVariable Integer customerId) {
        log.info("Deleting customer with ID: {}", customerId);
        customerService.deleteCustomerById(customerId);
        WebResponse<Void> response = WebResponse.<Void>builder()
                .success(true)
                .message("Customer deleted successfully")
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/email")
    public ResponseEntity<WebResponse<CustomerDto>> getUserByEmail(@RequestParam String email) {
        log.info("Fetching customer with email: {}", email);
        CustomerDto customerDto = customerService.selectUserByEmail(email);
        WebResponse<CustomerDto> response = WebResponse.<CustomerDto>builder()
                .success(true)
                .data(customerDto)
                .message("Customer fetched successfully")
                .build();
        return ResponseEntity.ok(response);
    }

}
