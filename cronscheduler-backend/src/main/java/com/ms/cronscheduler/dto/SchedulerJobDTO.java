package com.ms.cronscheduler.dto;

import java.io.*;
import java.time.LocalDateTime;

// Example DTO for creating a new job
public class SchedulerJobDTO {

    private Long databaseSettingsId; // Only need the ID, not the whole settings object
    private String jobName;
    private byte[] sqlQuery;
    private byte[] keyUserEmail;
    private byte[] cc;
    private String emailBody;
    private String emailSubject;
    private String cronFrequency;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private LocalDateTime createdAt;
    private Boolean isDeleted;
    private String Status = "SCHEDULED";
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    // getters and setters




    public Long getDatabaseSettingsId() {
        return databaseSettingsId;
    }

    public void setDatabaseSettingsId(Long databaseSettingsId) {
        this.databaseSettingsId = databaseSettingsId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String[] getSqlQuery() throws IOException, ClassNotFoundException {
        return deserialize(sqlQuery);
    }

    public void setSqlQuery(String[] sqlQuery) throws IOException {
        this.sqlQuery = serialize(sqlQuery);
    }

    public String[] getKeyUserEmail() throws IOException, ClassNotFoundException {
        return deserialize(keyUserEmail);
    }

    public void setKeyUserEmail(String[] keyUserEmail) throws IOException {
        this.keyUserEmail = serialize(keyUserEmail);
    }

    public String[] getCc() throws IOException, ClassNotFoundException {
        return deserialize(this.cc);
    }

    public void setCc(String[] cc) throws IOException {
        this.cc = serialize(cc);
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

    public String getCronFrequency() {
        return cronFrequency;
    }

    public void setCronFrequency(String cronFrequency) {
        this.cronFrequency = cronFrequency;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
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
}