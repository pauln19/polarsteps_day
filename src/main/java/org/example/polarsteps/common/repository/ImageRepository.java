package org.example.polarsteps.common.repository;

import org.example.polarsteps.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Integer> {
}
