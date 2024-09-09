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
    @NotNull
    private String cronFrequency;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date startDate;
    @Temporal(TemporalType.DATE)
    @NotNull
    private Date endDate;

    @ColumnDefault("'NOT STARTED'")
    private String status;

}
