package org.example.polarsteps.image;

import org.example.polarsteps.common.error.ResourceNotFoundException;
import org.example.polarsteps.common.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ImageService {

    private final ImageRepository imageRepository;


    ImageService(
            ImageRepository imageRepository
    ) {
        this.imageRepository = imageRepository;
    }

    @Transactional
    public ImageDtos.ImageResponse getImageById(Integer imageId) {
        Optional<Image> image = imageRepository.findById(imageId);
        if (image.isEmpty()) {
            throw ResourceNotFoundException.of("Image", imageId);
        }

        return new ImageDtos.ImageResponse(image.get().getPath());
    }

}
