package com.ms.cronscheduler.service;

import com.ms.cronscheduler.dto.SchedulerJobDTO;
import com.ms.cronscheduler.errorHandling.ResourceNotFoundException;
import com.ms.cronscheduler.model.DatabaseSettings;
import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import javax.management.relation.RelationServiceNotRegisteredException;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

@Service
public class JobSchedulerService {

    private static final Logger LOGGER = Logger.getLogger(JobSchedulerService.class.getName());

    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private JobService jobService;
    private ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ExcelService excelService;

    @Autowired
    private EmailService emailService;

    @PostConstruct
    public void init() {
        initializeTaskScheduler();
        restoreAllActiveJobs();
    }

    private void restoreAllActiveJobs() {
        List<Job> activeJobs = jobService.findByStatusAndIsDeletedFalse();
        activeJobs.forEach(this::scheduleTask);
    }

    private void initializeTaskScheduler() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(200);
        taskScheduler.setThreadNamePrefix("jobScheduler-");
        taskScheduler.initialize();
    }

    public void scheduleTask(Job job) {
        Runnable task = createTask(job.getId());
        CronTrigger cronTrigger = new CronTrigger("0 " + job.getCronFrequency());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, cronTrigger);
        assert scheduledFuture != null;
        scheduledTasks.put(job.getJobName(), scheduledFuture);
    }

    public void rescheduleTask(Long id, SchedulerJobDTO job) throws IOException, ClassNotFoundException {
        Job oldJob = jobService.getJobById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        stopTask(oldJob.getJobName());
        LOGGER.info("rescheduling  + " + job.getJobName());
        Job newJob = jobService.update(id,job);
        scheduleTask(newJob);
    }


    private Runnable createTask(Long jobId) {
        return () -> {
            Job job;
            if(jobService.getJobById(jobId).isPresent()) {
                job = jobService.getJobById(jobId).get();
            } else {
                throw new ResourceNotFoundException("Job not found");
            }
            try {
                LOGGER.info("Executing task :" + job.getJobName());
                LocalDateTime now = LocalDateTime.now();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime startDate = job.getStartDateTime();
                LocalDateTime endDate = job.getEndDateTime();

                if (now.isAfter(startDate) && now.isBefore(endDate)) {
                    job.setStatus("IN PROGRESS");
                    // business logic

                    List<List<Map<String, Object>>> result = databaseService.fetchData(Arrays.asList(job.getSqlQuery()), job.getDatabaseSettings().getDatabaseUrl(), job.getDatabaseSettings().getDatabaseUsername(), job.getDatabaseSettings().getDatabasePassword());
                    LOGGER.info("DATA BASE FETCHED");
                    byte[] excel = excelService.writeDataToExcelUsingFastExcel(result);

                    String outputFilePath = job.getJobName() + ".xlsx";

                    try {
                        //writeByteArrayToExcelFile(excel, outputFilePath);
                        emailService.sendEmailWithAttachment(excel, outputFilePath,job.getKeyUserEmail(), job.getCc() ,job.getEmailBody(),job.getEmailSubject());
                        LOGGER.info("Excel file created successfully! and mail send");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } else if(!now.isBefore(startDate)) {
                    job.setStatus("COMPLETED");
                    stopTask(job.getJobName());
                }

                jobService.update(job.getId(), createDto(job));
            } catch (Exception e) {
                // Log the exception
                e.printStackTrace();
            }
        };
    }

    public SchedulerJobDTO createDto(Job theJob) throws IOException, ClassNotFoundException {
        SchedulerJobDTO job = new SchedulerJobDTO();
        job.setJobName(theJob.getJobName());
        job.setCronFrequency(theJob.getCronFrequency());
        job.setStartDateTime(theJob.getStartDateTime());
        job.setEndDateTime(theJob.getEndDateTime());
        job.setSqlQuery(theJob.getSqlQuery());
        job.setCc(theJob.getCc());
        job.setKeyUserEmail(theJob.getKeyUserEmail());
        job.setEmailSubject(theJob.getEmailSubject());
        job.setEmailBody(theJob.getEmailBody());
        job.setDatabaseSettingsId(theJob.getDatabaseSettings().getId());
        job.setStatus(theJob.getStatus());
        job.setCreatedAt(theJob.getCreatedAt());
        job.setUpdatedAt(theJob.getUpdatedAt());
        job.setCreatedBy(theJob.getCreatedBy());
        job.setUpdatedBy(theJob.getUpdatedBy());
        job.setIsDeleted(theJob.getIsDeleted());

        return job;

    }

    public void stopTask(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(taskName);
            System.out.println(taskName + " stopped");
        }
    }
}
