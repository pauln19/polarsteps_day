package org.example.polarsteps.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/image")
@Tag(name = "Images", description = "Images API")
public class ImageController {

    private final ImageService imageService;

    ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ImageDtos.ImageResponse> getImageById(@NonNull @PathVariable(value = "id") Integer imageId) {
        return ResponseEntity.ok(imageService.getImageById(imageId));
    }

}
