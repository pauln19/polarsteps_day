package org.example.polarsteps.common.repository;

import org.example.polarsteps.userscore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByIdAndIsDeletedFalse(Integer id);
}
