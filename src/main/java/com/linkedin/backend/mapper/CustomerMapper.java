package com.linkedin.backend.mapper;

import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.entities.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CustomerMapper {

    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);

    // Mapping from Entity to DTO
    CustomerDto toCustomerDto(Customer customer);

    // Mapping from DTO to Entity
    Customer toCustomer(CustomerDto customerDto);
}
