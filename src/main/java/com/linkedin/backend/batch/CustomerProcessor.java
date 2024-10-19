package com.linkedin.backend.batch;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.entities.Customer;
import com.linkedin.backend.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class CustomerProcessor implements ItemProcessor<CustomerDto, Customer> {

    private final CustomerMapper customerMapper;

    @Override
    public Customer process(CustomerDto customerDto) {
        return customerMapper.toCustomer(customerDto);
    }
}
