package com.ms.cronscheduler.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.ColumnDefault;

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

    private String sqlQuery;
    private String databaseUrl;
    private String databaseName;
    private String databaseUsername;
    private String databasePassword;
    private String keyUserEmail;
    private String emailBody;
    private String emailSubject;


    @NotNull
    private String cronFrequency;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;

    @Column(name = "status", columnDefinition = "VARCHAR(255) DEFAULT 'NOT STARTED'")
    private String status;


    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = "NOT STARTED";
        }
    }

}
