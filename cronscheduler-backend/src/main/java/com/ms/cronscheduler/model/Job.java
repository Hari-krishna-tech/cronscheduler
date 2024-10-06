package com.ms.cronscheduler.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.SneakyThrows;
import org.hibernate.annotations.ColumnDefault;

import java.io.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;


@Entity
@Table(name = "scheduler_job_list")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "job_name", nullable = false)
    private String jobName;

    @Lob
    @Column(name = "sql_query", columnDefinition = "BLOB")
    private byte[] sqlQuery;

    @ManyToOne
    @JoinColumn(name = "database_setting_id", nullable = false)
    private DatabaseSettings databaseSettings;

    @Lob
    @Column(name = "key_user_email", columnDefinition = "BLOB")
    private byte[] keyUserEmail;
    @Lob
    @Column(name = "cc", columnDefinition = "BLOB")
    private byte[] cc;
    @Column(name = "email_body" , nullable = false)
    private String emailBody;
    @Column(name = "email_subject", nullable = false)
    private String emailSubject;



    @Column(name = "cron_frequency", nullable = false)
    private String cronFrequency;
//    @Temporal(TemporalType.DATE)

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;
//    @Temporal(TemporalType.DATE)

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "status", nullable = false)
    private String status = "NOT STARTED";

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;







    // Convert String[] to byte[]
    public void setSqlQuery(String[] sqlQuery) throws IOException {
        this.sqlQuery = serialize(sqlQuery);
    }

    // Convert byte[] back to String[]
    public String[] getSqlQuery() throws IOException, ClassNotFoundException {
        return deserialize(this.sqlQuery);
    }

    private byte[] serialize(String[] data) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(data);
            return bos.toByteArray();
        }
    }

    private String[] deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (String[]) ois.readObject();
        }
    }

    public Job() {
    }

    public  Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotNull String getJobName() {
        return jobName;
    }

    public void setJobName(@NotNull String jobName) {
        this.jobName = jobName;
    }

    public DatabaseSettings getDatabaseSettings() {
        return databaseSettings;
    }

    public void setDatabaseSettings(DatabaseSettings databaseSettings) {
        this.databaseSettings = databaseSettings;
    }

    public String[] getKeyUserEmail() throws IOException, ClassNotFoundException {
        return deserialize(keyUserEmail);
    }

    public void setKeyUserEmail(String[] keyUserEmail) throws IOException {
        this.keyUserEmail = serialize(keyUserEmail);
    }

    public String getEmailBody() {
        return emailBody;
    }

    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public @NotNull String getCronFrequency() {
        return cronFrequency;
    }

    public void setCronFrequency(@NotNull String cronFrequency) {
        this.cronFrequency = cronFrequency;
    }

    public @NotNull LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(@NotNull LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public @NotNull LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(@NotNull LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getCc() throws IOException, ClassNotFoundException {
        return deserialize(cc);
    }

    public void setCc(String[] cc) throws IOException {
        this.cc = serialize(cc);
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        try {
            return "Job{" +
                    "id=" + id +
                    ", jobName='" + jobName + '\'' +
                    ", sqlQuery=" + Arrays.toString(deserialize(sqlQuery)) +
                    ", keyUserEmail=" + Arrays.toString(keyUserEmail) +
                    ", cc=" + Arrays.toString(cc) +
                    ", emailBody='" + emailBody + '\'' +
                    ", emailSubject='" + emailSubject + '\'' +
                    ", cronFrequency='" + cronFrequency + '\'' +
                    ", startDateTime=" + startDateTime +
                    ", endDateTime=" + endDateTime +
                    ", status='" + status + '\'' +
                    '}';
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
