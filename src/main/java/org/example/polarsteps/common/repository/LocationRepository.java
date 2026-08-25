package org.example.polarsteps.common.repository;

import org.example.polarsteps.userscore.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Integer> {
}
