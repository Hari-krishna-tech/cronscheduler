package com.ms.cronscheduler.service;


import com.ms.cronscheduler.dto.SchedulerJobDTO;
import com.ms.cronscheduler.model.DatabaseSettings;
import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class JobService {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    DatabaseSettingsService databaseSettingsService;

    @Autowired
    EntityManager entityManager;

    public Optional<Job> getJobById(Long id) {

        return jobRepository.findById(id);
    }

    public List<Job> getAll() {
        return jobRepository.findAllImpl();
    }

    @Transactional
    public Job save(SchedulerJobDTO theJob) throws IOException, ClassNotFoundException {
        Job job = new Job();

        job.setJobName(theJob.getJobName());
        job.setCronFrequency(theJob.getCronFrequency());
        job.setStartDateTime(theJob.getStartDateTime());
        job.setEndDateTime(theJob.getEndDateTime());
        job.setSqlQuery(theJob.getSqlQuery());
        job.setCc(theJob.getCc());
        job.setKeyUserEmail(theJob.getKeyUserEmail());
        job.setEmailSubject(theJob.getEmailSubject());
        job.setEmailBody(theJob.getEmailBody());
        job.setStatus(theJob.getStatus());
        job.setCreatedAt(theJob.getCreatedAt());
        job.setCreatedBy(theJob.getCreatedBy());
        job.setUpdatedAt(theJob.getUpdatedAt());
        job.setUpdatedBy(theJob.getUpdatedBy());
        job.setIsDeleted(theJob.getIsDeleted());

        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettingsById(theJob.getDatabaseSettingsId());
        job.setDatabaseSettings(databaseSettings);




        return jobRepository.save(job);
    }
    @Transactional
    public Job update(Long id,SchedulerJobDTO job) throws IOException, ClassNotFoundException {
        Job oldJob = jobRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Job not Found"));

//        boolean isUpdated = false;


            oldJob.setJobName(job.getJobName());


            oldJob.setCronFrequency(job.getCronFrequency());


            oldJob.setStartDateTime(job.getStartDateTime());


            oldJob.setEndDateTime(job.getEndDateTime());



            oldJob.setSqlQuery(job.getSqlQuery());

            DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettingsById(job.getDatabaseSettingsId());
            oldJob.setDatabaseSettings(databaseSettings);

            oldJob.setKeyUserEmail(job.getKeyUserEmail());

            oldJob.setCc(job.getCc());


            oldJob.setEmailBody(job.getEmailBody());


            oldJob.setEmailSubject(job.getEmailSubject());

            oldJob.setCreatedAt(job.getCreatedAt());
            oldJob.setCreatedBy(job.getCreatedBy());

            oldJob.setUpdatedAt(job.getUpdatedAt());
            oldJob.setUpdatedBy(job.getUpdatedBy());

            oldJob.setIsDeleted(job.getIsDeleted());



//        oldJob.setJobName(job.getJobName());
//        oldJob.setCronFrequency(job.getCronFrequency());
//        oldJob.setStartDate(job.getStartDate());
//        oldJob.setEndDate(job.getEndDate());
        oldJob.setStatus(job.getStatus());


        return jobRepository.save(oldJob);




    }
    @Transactional
    public void deleteById(Long id) {
        jobRepository.deleteByIdImple(id);
    }



    public List<Job> findByStatusAndIsDeletedFalse() {

        return jobRepository.findAllByStatusAndIsDeletedFalse();
    }
}
