package com.ms.cronscheduler.dto;

public class DatabaseSettingsDTO {
    private Long id;
    private String DatabaseSettingName;

    public DatabaseSettingsDTO(Long id, String databaseSettingName) {
        this.id = id;
        DatabaseSettingName = databaseSettingName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatabaseSettingName() {
        return DatabaseSettingName;
    }

    public void setDatabaseSettingName(String databaseSettingName) {
        DatabaseSettingName = databaseSettingName;
    }
}
