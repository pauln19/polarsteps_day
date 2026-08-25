package org.example.polarsteps.userscore;

import org.example.polarsteps.common.error.ResourceNotFoundException;
import org.example.polarsteps.common.repository.StepRepository;
import org.example.polarsteps.common.repository.TripRepository;
import org.example.polarsteps.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.example.polarsteps.common.repository.StepRepository.StepScoreData;
import static org.example.polarsteps.common.repository.TripRepository.TripScoreData;

@Service
public class UserService {
    private static final int KM_PER_DISTANCE_POINT = 100;
    private static final int DAYS_PER_DURATION_POINT = 7;
    private static final int LOCATIONS_PER_DIVERSITY_POINT = 5;

    private final UserRepository userRepository;
    private final TripRepository tripRepository;
    private final StepRepository stepRepository;

    UserService(
            UserRepository userRepository,
            TripRepository tripRepository,
            StepRepository stepRepository
    ) {
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
        this.stepRepository = stepRepository;
    }

    @Transactional(readOnly = true)
    public UserDtos.UserScoreResponse getUserScore(Integer userId) {
        if (!userRepository.existsByIdAndIsDeletedFalse(userId)) {
            throw ResourceNotFoundException.of("User", userId);
        }

        Map<Integer, TripScore> tripScores = new HashMap<>();
        long score = 0;
        for (TripScoreData trip : tripRepository.findScoreDataByUserId(userId)) {
            tripScores.put(trip.getTripId(), new TripScore());

            // Trip duration
            score += durationPoints(trip.getStartDate(), trip.getEndDate());
        }

        Set<LocationIdentity> locations = new HashSet<>();
        for (StepScoreData step : stepRepository.findScoreDataByUserId(userId)) {
            TripScore tripScore = tripScores.get(step.getTripId());
            if (tripScore == null) {
                throw new IllegalStateException("Step linked to invalid trip: " + step.getTripId());
            }

            // Step with or without photo
            score += Boolean.TRUE.equals(step.getHasPhoto()) ? 2 : 1;

            // Step with text
            if (hasText(step.getDescription())) {
                score++;
            }

            // Distnace between consecutive steps
            tripScore.addStep(step.getLatitude(), step.getLongitude());
            // Add to then verify location diversity score
            addLocation(locations, step.getLocationName(), step.getLocationDetail());
        }

        for (TripScore tripScore : tripScores.values()) {
            score += tripScore.score;
        }

        // Location diversity
        score += locations.size() / LOCATIONS_PER_DIVERSITY_POINT;

        return new UserDtos.UserScoreResponse(userId, Math.toIntExact(score));
    }

    private long durationPoints(Instant startDate, Instant endDate) {
        if (startDate == null || endDate == null || endDate.isBefore(startDate)) {
            return 0;
        }
        return Duration.between(startDate, endDate).toDays() / DAYS_PER_DURATION_POINT; // Rounded down
    }

    private static boolean hasText(String description) {
        return description != null && !description.isBlank();
    }

    private static void addLocation(Set<LocationIdentity> locations, String name, String detail) {
        if (hasText(name) || hasText(detail)) {
            locations.add(new LocationIdentity(name, detail));
        }
    }

    private static boolean validCoordinates(Double latitude, Double longitude) {
        return latitude != null && longitude != null
                && Double.isFinite(latitude) && Double.isFinite(longitude)
                && latitude >= -90 && latitude <= 90
                && longitude >= -180 && longitude <= 180;
    }

    private static final class TripScore {
        private Coordinates previousCoords;
        private long score = 0L;

        private void addStep(Double latitude, Double longitude) {
            Coordinates currentCoords = validCoordinates(latitude, longitude)
                    ? new Coordinates(latitude, longitude)
                    : null;
            if (previousCoords != null && currentCoords != null) {
                double distanceBetweenSteps = DistanceCalculator.calculateDistanceInKm(previousCoords.latitude, previousCoords.longitude,
                        currentCoords.latitude, currentCoords.longitude);
                score += (long) Math.floor(distanceBetweenSteps / KM_PER_DISTANCE_POINT);
            }
            previousCoords = currentCoords;
        }
    }

    private record Coordinates(double latitude, double longitude) {
    }

    private record LocationIdentity(String name, String detail) {
    }
}
