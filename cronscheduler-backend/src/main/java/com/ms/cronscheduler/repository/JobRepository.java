package com.ms.cronscheduler.repository;

import com.ms.cronscheduler.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    @Query("SELECT j FROM Job j WHERE j.status IN ('SCHEDULED', 'IN PROGRESS') AND j.isDeleted = false")
    List<Job> findAllByStatusAndIsDeletedFalse();

    @Modifying
    @Query("UPDATE Job j SET j.isDeleted = true where j.id = :id")
    void deleteByIdImple(@Param("id") Long id);

    @Query("Select j from Job j WHERE j.isDeleted = false")
    List<Job> findAllImpl();
}
