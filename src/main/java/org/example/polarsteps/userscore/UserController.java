package org.example.polarsteps.userscore;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.example.polarsteps.userscore.UserDtos.UserScoreResponse;

@RestController
@RequestMapping("/api/user")
@Tag(name = "Users", description = "User API")
public class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/userscore/{id}")
    public ResponseEntity<UserScoreResponse> getQualityScore(@NonNull @PathVariable(value = "id") Integer userId) {
        return ResponseEntity.ok(userService.getUserScore(userId));
    }

}
