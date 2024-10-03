package com.ms.cronscheduler.model;


import jakarta.persistence.*;

@Entity
@Table(name = "settings_database_setting")
public class DatabaseSettings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "database_setting_name", nullable = false)
    private String databaseSettingName;

    @Column(name = "database_name", nullable = false)
    private String databaseName;

    @Column(name = "database_password", nullable = false)
    private String databasePassword;

    @Column(name = "database_url", nullable = false)
    private String databaseUrl;

    @Column(name = "database_username", nullable = false)
    private String databaseUsername;

    public DatabaseSettings() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseSettingName() {
        return databaseSettingName;
    }

    public void setDatabaseSettingName(String databaseSettingName) {
        this.databaseSettingName = databaseSettingName;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public void setDatabaseUrl(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    @Override
    public String toString() {
        return "DatabaseSettings{" +
                "id=" + id +
                ", databaseSettingName='" + databaseSettingName + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", databasePassword='" + databasePassword + '\'' +
                ", databaseUrl='" + databaseUrl + '\'' +
                ", databaseUsername='" + databaseUsername + '\'' +
                '}';
    }
}
