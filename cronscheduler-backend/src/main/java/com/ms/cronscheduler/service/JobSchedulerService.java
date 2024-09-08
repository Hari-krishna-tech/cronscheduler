package com.ms.cronscheduler.service;


import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.model.Status;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class JobSchedulerService {

    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private JobService jobService;
    private ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @PostConstruct
    public void init() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        taskScheduler.setThreadNamePrefix("jobScheduler-");
        taskScheduler.initialize();
    }

    public void scheduleTask(Job job) {
        Runnable task = () -> {
            System.out.println("Executing task: " + job.getJobName());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = job.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime endDate = job.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            if(now.isBefore(startDate)) {
                job.setStatus("NOT STARTED");
            } else if(now.isAfter(startDate) && now.isBefore(endDate)) {
                job.setStatus("IN PROGRESS");
                // business logic
            } else {
                job.setStatus("COMPLETED");

                // stop task
                stopTask(job.getJobName());
            }

            jobService.update(job.getId(), job);
        };



        CronTrigger cronTrigger = new CronTrigger(job.getCronFrequency());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, cronTrigger);
        scheduledTasks.put(job.getJobName(), scheduledFuture);
    }

    public void rescheduleTask(Integer id,Job job) {
        Job oldJob = jobService.getJobById(id).get();
        String taskName = oldJob.getJobName();
        stopTask(taskName);
        Runnable task = () -> {
            System.out.println("Executing task: " + job.getJobName());
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime startDate = job.getStartDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            LocalDateTime endDate = job.getEndDate().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
            if (now.isBefore(startDate)) {
                job.setStatus("NOT STARTED");
            } else if (now.isAfter(startDate) && now.isBefore(endDate)) {
                job.setStatus("IN PROGRESS");
                // business logic
            } else {
                job.setStatus("COMPLETED");

                // stop task
                stopTask(job.getJobName());
            }

            jobService.update(job.getId(), job);
        };

    }
    public void stopTask(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
        if(scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(taskName);
        }
    }
}
