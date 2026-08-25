package org.example.polarsteps.common.repository;

import org.example.polarsteps.userscore.entity.Step;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StepRepository extends JpaRepository<Step, Integer> {

    @Query(value = """
            select
                s.trip_id as tripId,
                s.id as stepId,
                s.description as description,
                l.lat as latitude,
                l.lon as longitude,
                l.name as locationName,
                l.detail as locationDetail,
                exists (
                    select 1
                    from media m
                    where m.step_id = s.id
                      and m.is_deleted = false
                      and m.path is not null
                      and m.type = 0
                ) as hasPhoto
            from steps s
            join trips t on t.id = s.trip_id
            left join locations l on l.id = s.location_id and l.is_deleted = false
            where t.user_id = :userId
              and t.is_deleted = false
              and s.is_deleted = false
            order by s.trip_id, s.start_time nulls last, s.id
            """, nativeQuery = true)
    List<StepScoreData> findScoreDataByUserId(@Param("userId") Integer userId);

    interface StepScoreData {
        Integer getTripId();

        Integer getStepId();

        String getDescription();

        Double getLatitude();

        Double getLongitude();

        String getLocationName();

        String getLocationDetail();

        Boolean getHasPhoto();
    }
}
