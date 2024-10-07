package com.linkedin.backend.controller;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.exception.CustomerNotFoundException;
import com.linkedin.backend.exception.WebResponse;
import com.linkedin.backend.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Customer Management", description = "Operations related to Customer Management")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(summary = "Get all customers", description = "Fetch all customers with pagination and sorting")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customers fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

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
    @Operation(summary = "Get a customer by ID", description = "Fetch a customer by providing the customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
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

    @Operation(summary = "Create a new customer", description = "Create a new customer by providing customer details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Customer created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
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
    @Operation(summary = "Update an existing customer", description = "Update customer details by providing customer ID and details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer updated successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found"),
            @ApiResponse(responseCode = "400", description = "Validation error")
    })
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

    @Operation(summary = "Delete a customer", description = "Delete a customer by providing the customer ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Customer deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
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

    @Operation(summary = "Get a customer by email", description = "Fetch a customer by providing the email address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Customer fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Customer not found")
    })
    @GetMapping("/email")
    public ResponseEntity<WebResponse<CustomerDto>> getCustomerByEmail(@RequestParam String email) {
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
