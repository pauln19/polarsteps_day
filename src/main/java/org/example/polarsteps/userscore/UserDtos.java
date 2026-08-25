package org.example.polarsteps.userscore;

public final class UserDtos {

    private UserDtos() {
    }

    public record UserScoreResponse(Integer userId, Integer score) {
    }

}
