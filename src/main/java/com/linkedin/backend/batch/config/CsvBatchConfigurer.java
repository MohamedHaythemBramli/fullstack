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
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.ValidationUtils;

@Configuration
public class CsvBatchConfigurer {



    @Bean
    public Job importCSVJob(JobRepository jobRepository,Step importCSV ){
        return new JobBuilder("Import CSV JOB",jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(importCSV)
                .build();
    }


    @Bean
    public Step importCSV(JobRepository jobRepository ,
                          PlatformTransactionManager transactionManager ,
                          CustomerItemWriter customerItemWriter ,
                          CustomerProcessor customerProcessor) {

        return new StepBuilder("Import CSV Step",jobRepository)
                .<CustomerDto, Customer>chunk(5,transactionManager)
                .reader(flatFileItemReader())
                .processor(customerProcessor)
                .writer(customerItemWriter)
                .build();
    }

    @Bean
    public FlatFileItemReader<CustomerDto> flatFileItemReader(){
        return new FlatFileItemReaderBuilder<CustomerDto>()
                .resource(new ClassPathResource("/csv/customers.csv"))
                .name("Flat File Customer Reader")
                .saveState(Boolean.FALSE)
                .linesToSkip(1)
                .delimited()
                .delimiter(",")
                .names("name","email","age")
                .targetType(CustomerDto.class)
                .build();
    }
}
