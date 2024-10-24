package com.ms.cronscheduler.controller;


import com.ms.cronscheduler.dto.SchedulerJobDTO;
import com.ms.cronscheduler.errorHandling.ResourceNotFoundException;
import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.service.JobSchedulerService;
import com.ms.cronscheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobService jobService;
    @Autowired
    JobSchedulerService jobSchedulerService;

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @GetMapping("/jobs")
    public List<Job> getJobs() {
        return jobService.getAll();
    }

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @GetMapping("/jobs/{id}")
    public Job getJob(@PathVariable Long id) {
        Optional<Job> job = jobService.getJobById(id);
        if(job.isPresent()) {
            return job.get();
        } else {
            throw new ResourceAccessException("Job not Found");
        }

    }

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @PostMapping("/jobs")
    public Job createJob(@RequestBody SchedulerJobDTO job) throws IOException, ClassNotFoundException {

        Job theJob1 = jobService.save(job);
        Long id = theJob1.getId();
        if(jobService.getJobById(id).isPresent()) {
            Job theJob = jobService.getJobById(id).get();
            jobSchedulerService.scheduleTask(theJob);
            return theJob;
        } else {
            throw new IOException("Job not Saved");
        }

    }

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @PutMapping("/jobs/{id}")
    public SchedulerJobDTO updateJob(@RequestBody SchedulerJobDTO job,@PathVariable Long id) throws IOException, ClassNotFoundException {

        jobSchedulerService.rescheduleTask(id,job);

        return job;
    }

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {

        // need to change the delete flag
        if(jobService.getJobById(id).isPresent()) {
            Job job = jobService.getJobById(id).get();
            jobSchedulerService.stopTask(job.getJobName());
            jobService.deleteById(id);
            return new ResponseEntity<>("Job Deleted", HttpStatus.NO_CONTENT);
        } else {
            throw new ResourceNotFoundException("No job Found");
        }

    }


}
