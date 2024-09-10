package com.ms.cronscheduler.service;

import com.ms.cronscheduler.model.Job;
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
        initializeTaskScheduler();
    }

    private void initializeTaskScheduler() {
        taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(50);
        taskScheduler.setThreadNamePrefix("jobScheduler-");
        taskScheduler.initialize();
    }

    public void scheduleTask(Job job) {
        Runnable task = createTask(job);
        CronTrigger cronTrigger = new CronTrigger("0 " + job.getCronFrequency());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(task, cronTrigger);
        scheduledTasks.put(job.getJobName(), scheduledFuture);
    }

    public void rescheduleTask(Integer id, Job job) {
        Job oldJob = jobService.getJobById(id).orElseThrow(() -> new IllegalArgumentException("Job not found"));
        stopTask(oldJob.getJobName());
        System.out.println("rescheduling  + " + job.getJobName());
        scheduleTask(job);
    }

    private Runnable createTask(Job job) {
        return () -> {
            try {
                System.out.println("Executing task :" + job.getJobName());
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime startDate = job.getStartDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                LocalDateTime endDate = job.getEndDate().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();

                if (now.isAfter(startDate) && now.isBefore(endDate)) {
                    job.setStatus("IN PROGRESS");
                    // business logic
                } else if(!now.isBefore(startDate)) {
                    job.setStatus("COMPLETED");
                    stopTask(job.getJobName());
                }

                jobService.update(job.getId(), job);
            } catch (Exception e) {
                // Log the exception
                e.printStackTrace();
            }
        };
    }

    public void stopTask(String taskName) {
        ScheduledFuture<?> scheduledFuture = scheduledTasks.get(taskName);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledTasks.remove(taskName);
        }
    }
}
