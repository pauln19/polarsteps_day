package org.example.polarsteps.image;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.example.polarsteps.userscore.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.example.polarsteps.userscore.UserDtos.UserScoreResponse;

@RestController
@RequestMapping("/api/image")
@Tag(name = "Images", description = "Images API")
public class ImageController {

    private final UserService userService;

    ImageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserScoreResponse> getQualityScore(@NonNull @PathVariable(value = "id") Integer userId) {
        return ResponseEntity.ok(userService.getUserScore(userId));
    }

    @GetMapping
    public ResponseEntity<> getImagesByVicinity(List<Coordinate> coordinates) {
        return null;
    }

}
