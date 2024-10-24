package com.ms.cronscheduler.controller;

import com.ms.cronscheduler.dto.DatabaseSettingsDTO;
import com.ms.cronscheduler.model.DatabaseSettings;
import com.ms.cronscheduler.service.DatabaseSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 */

@CrossOrigin()
@RestController
@RequestMapping("/database-settings")
public class DatabaseSettingController {

    @Autowired
    DatabaseSettingsService databaseSettingsService;

    /**
     *
     *
     * @return List of DatabaseSettings Objects
     */

    @PreAuthorize("hasRole('ROLE_DATABASE_SETTING')")
    @GetMapping("/list")
    public List<DatabaseSettings> getDatabaseSettingsList() {
        return databaseSettingsService.getAllDatabaseSettings();

    }

    @PreAuthorize("hasRole('ROLE_REPORT_SCHEDULER')")
    @GetMapping("/list/id")
    public List<DatabaseSettingsDTO> getDatabaseSettingsListDTO() {
        return databaseSettingsService.getAllDatabaseSettingsDTO();
    }

    @PreAuthorize("hasRole('ROLE_DATABASE_SETTING')")
    @GetMapping("/{id}")
    public DatabaseSettings getDatabaseSettings(@PathVariable Long id) {
        return databaseSettingsService.getDatabaseSettingsById(id);
    }

    /**
     *
     * @param databaseSettings get's databaseSettings object from the client
     * @return databasesettings that is stored in the database
     */

    @PreAuthorize("hasRole('ROLE_DATABASE_SETTING')")
    @PostMapping("/")
    public DatabaseSettings addToDatabaseSettings(@RequestBody DatabaseSettings databaseSettings) {
        return databaseSettingsService.postDatabaseSettings(databaseSettings);
    }
    // don't allow update and delete

    @PreAuthorize("hasRole('ROLE_DATABASE_SETTING')")
    @PutMapping("/{id}")
    public DatabaseSettings updateDatabaseSettings(@PathVariable Long id, @RequestBody DatabaseSettings databaseSettings) {

        return databaseSettingsService.updateDatabaseSettings(id, databaseSettings);
    }


/*
    // should not allow update and delete
    @PreAuthorize("hasRole('ROLE_DATABASE_SETTING')")
    @DeleteMapping("/{id}")
    public void deleteDatabaseSettings(@PathVariable Long id) {
        databaseSettingsService.deleteDatabaseSettings(id);
    }
*/

}
