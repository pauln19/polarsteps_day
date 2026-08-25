package org.example.polarsteps.common.repository;

import org.example.polarsteps.userscore.entity.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip, Integer> {

    @Query("""
            select t.id as tripId, t.startDate as startDate, t.endDate as endDate
            from Trip t
            where t.user.id = :userId and t.isDeleted = false
            """)
    List<TripScoreData> findScoreDataByUserId(@Param("userId") Integer userId);

    interface TripScoreData {
        Integer getTripId();

        Instant getStartDate();

        Instant getEndDate();
    }
}
