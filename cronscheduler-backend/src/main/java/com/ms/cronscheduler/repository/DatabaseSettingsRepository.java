package com.ms.cronscheduler.repository;

import com.ms.cronscheduler.model.DatabaseSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseSettingsRepository extends JpaRepository<DatabaseSettings, Long> {
}
