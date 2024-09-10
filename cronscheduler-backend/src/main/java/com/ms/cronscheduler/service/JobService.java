package com.ms.cronscheduler.service;


import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    EntityManager entityManager;

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
        Job oldJob = jobRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Job not Found"));

//        boolean isUpdated = false;


            oldJob.setJobName(job.getJobName());


            oldJob.setCronFrequency(job.getCronFrequency());


            oldJob.setStartDate(job.getStartDate());


            oldJob.setEndDate(job.getEndDate());



            oldJob.setSqlQuery(job.getSqlQuery());


            oldJob.setDatabaseUrl(job.getDatabaseUrl());


            oldJob.setDatabaseName(job.getDatabaseName());


            oldJob.setDatabaseUsername(job.getDatabaseUsername());


            oldJob.setDatabasePassword(job.getDatabasePassword());


            oldJob.setKeyUserEmail(job.getKeyUserEmail());


            oldJob.setEmailBody(job.getEmailBody());


            oldJob.setEmailSubject(job.getEmailSubject());



//        oldJob.setJobName(job.getJobName());
//        oldJob.setCronFrequency(job.getCronFrequency());
//        oldJob.setStartDate(job.getStartDate());
//        oldJob.setEndDate(job.getEndDate());
        oldJob.setStatus(job.getStatus());


        return jobRepository.save(oldJob);




    }
    @Transactional
    public void deleteById(Integer id) {
        jobRepository.deleteById(id);
    }



}
