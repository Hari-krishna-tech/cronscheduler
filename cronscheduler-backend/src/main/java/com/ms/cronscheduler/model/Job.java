package com.ms.cronscheduler.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
public class Job {
    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NotNull
    @Column(unique = true)
    private String jobName;

    private String[] sqlQuery;
    private String databaseUrl;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    private String[] keyUserEmail;
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

}
