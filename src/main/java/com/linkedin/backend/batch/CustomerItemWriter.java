package com.linkedin.backend.batch;

import com.linkedin.backend.entities.Customer;
import com.linkedin.backend.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
@Slf4j
public class CustomerItemWriter implements ItemWriter<Customer> {


    private final CustomerRepository customerRepository;
    @Override
    public void write(Chunk<? extends Customer> chunk) throws Exception {
        chunk.forEach(customer -> {
            log.info(String.valueOf(customer));
        });
        customerRepository.saveAll(chunk);
    }
}
