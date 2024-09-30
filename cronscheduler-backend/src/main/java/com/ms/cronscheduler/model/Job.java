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
public class Job {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String jobName;

    @Lob
    @Column(name = "sql_query", columnDefinition = "BLOB")
    private byte[] sqlQuery;
    private String databaseUrl;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    @Lob
    @Column(name = "key_user_email", columnDefinition = "BLOB")
    private byte[] keyUserEmail;
    @Lob
    @Column(name = "cc", columnDefinition = "BLOB")
    private byte[] cc;
    private String emailBody;
    private String emailSubject;


    @NotNull
    private String cronFrequency;
//    @Temporal(TemporalType.DATE)
    @NotNull
    private LocalDateTime startDateTime;
//    @Temporal(TemporalType.DATE)
    @NotNull
    private LocalDateTime endDateTime;

    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'NOT STARTED'")
    private String status;


    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "NOT STARTED";
        }
    }


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

    public @NotNull Integer getId() {
        return id;
    }

    public void setId(@NotNull Integer id) {
        this.id = id;
    }

    public @NotNull String getJobName() {
        return jobName;
    }

    public void setJobName(@NotNull String jobName) {
        this.jobName = jobName;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
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


    @Override
    public String toString() {
        try {
            return "Job{" +
                    "id=" + id +
                    ", jobName='" + jobName + '\'' +
                    ", sqlQuery=" + Arrays.toString(deserialize(sqlQuery)) +
                    ", databaseUrl='" + databaseUrl + '\'' +
                    ", databaseName='" + databaseName + '\'' +
                    ", databaseUsername='" + databaseUsername + '\'' +
                    ", databasePassword='" + databasePassword + '\'' +
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
