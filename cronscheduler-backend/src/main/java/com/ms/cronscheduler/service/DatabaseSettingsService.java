package com.ms.cronscheduler.service;


import com.ms.cronscheduler.dto.DatabaseSettingsDTO;
import com.ms.cronscheduler.errorHandling.ResourceNotFoundException;
import com.ms.cronscheduler.model.DatabaseSettings;
import com.ms.cronscheduler.repository.DatabaseSettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseSettingsService {

    @Autowired
    private DatabaseSettingsRepository databaseSettingsRepository;

    public List<DatabaseSettings> getAllDatabaseSettings() {
        return databaseSettingsRepository.findAllImpl();
    }

    public DatabaseSettings postDatabaseSettings(DatabaseSettings databaseSettings) {
        return databaseSettingsRepository.save(databaseSettings);
    }

    public DatabaseSettings updateDatabaseSettings(Long id, DatabaseSettings databaseSettings) {

        if(databaseSettingsRepository.findById(id).isPresent()) {
            DatabaseSettings oldDatabaseSettings = databaseSettingsRepository.findById(id).get();

            oldDatabaseSettings.setDatabaseName(databaseSettings.getDatabaseName());
            oldDatabaseSettings.setDatabasePassword(databaseSettings.getDatabasePassword());
            oldDatabaseSettings.setDatabaseUrl(databaseSettings.getDatabaseUrl());
            oldDatabaseSettings.setDatabaseUsername(databaseSettings.getDatabaseUsername());
            oldDatabaseSettings.setIsDeleted(databaseSettings.getIsDeleted());

            return databaseSettingsRepository.save(oldDatabaseSettings);
        } else {
            throw new ResourceNotFoundException("Database settings not found");
        }


    }

    public void deleteDatabaseSettings(Long id) {
        DatabaseSettings databaseSettings = this.getDatabaseSettingsById(id);

        if(true ) {
            throw new IllegalStateException("Cannot Delete database settings with Active Job");
        }
        databaseSettingsRepository.deleteById(id);

    }

    public DatabaseSettings getDatabaseSettingsById(Long id) {
        return databaseSettingsRepository.findById(id).get();
    }

    public List<DatabaseSettingsDTO> getAllDatabaseSettingsDTO() {
        List<DatabaseSettings> databaseSettings = databaseSettingsRepository.findAllImpl();
        return databaseSettings.stream().map(databaseSetting -> new DatabaseSettingsDTO(databaseSetting.getId(), databaseSetting.getDatabaseSettingName())).toList();

    }
}
