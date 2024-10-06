package com.ms.cronscheduler.controller;


import com.ms.cronscheduler.dto.SchedulerJobDTO;
import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.service.JobSchedulerService;
import com.ms.cronscheduler.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@CrossOrigin()
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
    public Job getJob(@PathVariable Long id) {
        return jobService.getJobById(id).get();
    }

    @PostMapping("/jobs")
    public Job createJob(@RequestBody SchedulerJobDTO job) throws IOException, ClassNotFoundException {
        // create schedular
        System.out.println(job);
        Job theJob = jobService.save(job);
        jobSchedulerService.scheduleTask(theJob);
        // update database
        return theJob;
    }

    @PutMapping("/jobs/{id}")
    public Job updateJob(@RequestBody SchedulerJobDTO job,@PathVariable Long id) throws IOException, ClassNotFoundException {
        Job theJob = jobService.update(id,job);
        jobSchedulerService.rescheduleTask(id,theJob);
        System.out.println(id);
        return theJob;
    }

    @DeleteMapping("/jobs/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        Job job = jobService.getJobById(id).get();
        jobSchedulerService.stopTask(job.getJobName());
        jobService.deleteById(id);
        return new ResponseEntity<>("Job Deleted", HttpStatus.NO_CONTENT);
    }


}
