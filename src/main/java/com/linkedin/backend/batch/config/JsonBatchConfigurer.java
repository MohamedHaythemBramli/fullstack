package com.linkedin.backend.batch.config;




import com.linkedin.backend.batch.CustomerItemWriter;
import com.linkedin.backend.batch.CustomerProcessor;
import com.linkedin.backend.dto.CustomerDto;
import com.linkedin.backend.entities.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.batch.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class JsonBatchConfigurer {


    @Bean
    public Job importJSONJob(JobRepository jobRepository, Step importJSON ){
        return new JobBuilder("Import JSON JOB",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importJSON)
                .build();
    }


    @Bean
    public Step importJSON(JobRepository jobRepository ,
                          PlatformTransactionManager transactionManager ,
                          CustomerItemWriter customerItemWriter ,
                          CustomerProcessor customerProcessor) {


        return new StepBuilder("Import JSON Step",jobRepository)
                .<CustomerDto, Customer>chunk(5,transactionManager)
                .reader(customerJsonItemReader())
                .processor(customerProcessor)
                .writer(customerItemWriter)
                .build();
    }

    @Bean
    public JsonItemReader<CustomerDto> customerJsonItemReader(){

        return new JsonItemReaderBuilder<CustomerDto>()
                .name("jsonCustomerReader")
                .resource(new ClassPathResource("json/customers.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(CustomerDto.class))
                .build();

    }

}
