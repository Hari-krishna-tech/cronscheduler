package com.ms.cronscheduler.controller;

import com.ms.cronscheduler.dto.DatabaseSettingsDTO;
import com.ms.cronscheduler.model.DatabaseSettings;
import com.ms.cronscheduler.service.DatabaseSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/database-settings")
public class DatabaseSettingController {

    @Autowired
    DatabaseSettingsService databaseSettingsService;

    @GetMapping("/list")
    public List<DatabaseSettings> getDatabaseSettingsList() {
        return databaseSettingsService.getAllDatabaseSettings();

    }

    @GetMapping("/list/id")
    public List<DatabaseSettingsDTO> getDatabaseSettingsListDTO() {
        return databaseSettingsService.getAllDatabaseSettingsDTO();
    }

    @GetMapping("/{id}")
    public DatabaseSettings getDatabaseSettings(@PathVariable Long id) {
        return databaseSettingsService.getDatabaseSettingsById(id);
    }

    @PostMapping("/")
    public DatabaseSettings addToDatabaseSettings(@RequestBody DatabaseSettings databaseSettings) {
        return databaseSettingsService.postDatabaseSettings(databaseSettings);
    }

    @PutMapping("/{id}")
    public DatabaseSettings updateDatabaseSettings(@PathVariable Long id, @RequestBody DatabaseSettings databaseSettings) {
        return databaseSettingsService.updateDatabaseSettings(id, databaseSettings);
    }

    @DeleteMapping("/{id}")
    public void deleteDatabaseSettings(@PathVariable Long id) {
        databaseSettingsService.deleteDatabaseSettings(id);
    }
}
