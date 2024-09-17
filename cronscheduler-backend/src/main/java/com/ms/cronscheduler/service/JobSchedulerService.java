package com.ms.cronscheduler.service;

import com.ms.cronscheduler.model.Job;
import com.ms.cronscheduler.repository.JobRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

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

@Service
public class JobSchedulerService {

    private ThreadPoolTaskScheduler taskScheduler;
    @Autowired
    private JobService jobService;
    private ConcurrentHashMap<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    @Autowired
    private DatabaseService databaseService;

    @Autowired
    private ExcelService excelService;

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

    public  void writeByteArrayToExcelFile(ByteArrayInputStream byteArrayInputStream, String outputFilePath) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = byteArrayInputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        }
    }


    private Runnable createTask(Job job) {
        return () -> {
            try {
                System.out.println("Executing task :" + job.getJobName());
                LocalDateTime now = LocalDateTime.now();
                ZoneId zoneId = ZoneId.systemDefault();
                LocalDateTime startDate = job.getStartDateTime();
                LocalDateTime endDate = job.getEndDateTime();
//                LocalDateTime startDate = job.getStartDateTime().toInstant()
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();
//                LocalDateTime endDate = job.getEndDateTime().toInstant()
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime();



                if (now.isAfter(startDate) && now.isBefore(endDate)) {
                    job.setStatus("STARTED");
                    // business logic

                    List<List<Map<String, Object>>> result = databaseService.fetchData(Arrays.asList(job.getSqlQuery()), job.getDatabaseUrl(), job.getDatabaseUsername(), job.getDatabasePassword());
                    ByteArrayInputStream excel = excelService.writeDataToExcel(result);

                    String outputFilePath = job.getJobName() +now.getDayOfMonth()+ now.getMinute() + now.getSecond() +".xlsx";

                    try {
                        writeByteArrayToExcelFile(excel, outputFilePath);
                        System.out.println("Excel file created successfully!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


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
