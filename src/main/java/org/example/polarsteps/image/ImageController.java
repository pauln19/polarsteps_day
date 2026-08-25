package org.example.polarsteps.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/closest")
    public ResponseEntity<List<ImageDtos.ImageResponse>> getImageById(@RequestBody CoordinateDto coordinates) {
        return ResponseEntity.ok(imageService.getClosestImagesToCoordinate(coordinates));
    }
}
