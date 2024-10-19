package com.linkedin.backend.batch.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/batch")
public class JobTriggerController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job importCSVJob;

    @Autowired
    private Job importJSONJob;

    @PostMapping("/trigger/{jobName}")
    public ResponseEntity<String> triggerJob(@PathVariable String jobName) {
        try {
            Job job;
            if ("csv".equalsIgnoreCase(jobName)) {
                job = importCSVJob;
            } else if ("json".equalsIgnoreCase(jobName)) {
                job = importJSONJob;
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Unknown job name: " + jobName);
            }

            // Build job parameters with current timestamp or any other data
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis()) // Example parameter
                    .toJobParameters();

            jobLauncher.run(job, jobParameters);

            return ResponseEntity.ok("Job " + jobName + " triggered successfully!");
        } catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Job failed to start: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
