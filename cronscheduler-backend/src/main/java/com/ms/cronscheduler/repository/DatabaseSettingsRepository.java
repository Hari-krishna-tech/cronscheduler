package com.ms.cronscheduler.repository;

import com.ms.cronscheduler.model.DatabaseSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseSettingsRepository extends JpaRepository<DatabaseSettings, Long> {
    @Query("SELECT ds from DatabaseSettings ds where ds.isDeleted = false")
    List<DatabaseSettings> findAllImpl();


}
