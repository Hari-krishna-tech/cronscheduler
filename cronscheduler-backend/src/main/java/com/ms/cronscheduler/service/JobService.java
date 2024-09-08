package com.ms.cronscheduler.service;


import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    public Optional<Job> getJobById(Integer id) {

        return jobRepository.findById(id);
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    @Transactional
    public Job save(Job job) {
        return jobRepository.save(job);
    }
    @Transactional
    public Job update(Integer id,Job job) {
        Optional<Job> oldJob = jobRepository.findById(id);

        oldJob.get().setJobName(job.getJobName());
        oldJob.get().setCronFrequency(job.getCronFrequency());
        oldJob.get().setStartDate(job.getStartDate());
        oldJob.get().setEndDate(job.getEndDate());
        oldJob.get().setStatus(job.getStatus());
        return jobRepository.save(oldJob.get());
    }
    @Transactional
    public void deleteById(Integer id) {
        jobRepository.deleteById(id);
    }



}
