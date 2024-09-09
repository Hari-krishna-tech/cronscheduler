package com.ms.cronscheduler.controller;


import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.service.JobSchedulerService;
import com.ms.cronscheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api")
public class JobController {
    @Autowired
    JobService jobService;
    @Autowired
    JobSchedulerService jobSchedulerService;

    @GetMapping("/jobs")
    public List<Job> getJobs() {
        return jobService.getAll();
    }

    @GetMapping("/jobs/{id}")
    public Job getJob(@PathVariable Integer id) {
        return jobService.getJobById(id).get();
    }

    @PostMapping("/jobs")
    public Job createJob(@RequestBody Job job) {
        // create schedular
        jobSchedulerService.scheduleTask(job);
        // update database
        return jobService.save(job);
    }

    @PutMapping("/jobs/{id}")
    public Job updateJob(@RequestBody Job job,@PathVariable Integer id) {
        jobSchedulerService.rescheduleTask(id,job);
        System.out.println(id);
        return jobService.update(id,job);
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Integer id) {
        Job job = jobService.getJobById(id).get();
        jobSchedulerService.stopTask(job.getJobName());
        jobService.deleteById(id);
        return new ResponseEntity<>("Job Deleted", HttpStatus.NO_CONTENT);
    }




}
