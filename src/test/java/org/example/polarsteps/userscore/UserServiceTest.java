package org.example.polarsteps.userscore;

import org.example.polarsteps.common.error.ResourceNotFoundException;
import org.example.polarsteps.common.repository.StepRepository;
import org.example.polarsteps.common.repository.TripRepository;
import org.example.polarsteps.common.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TripRepository tripRepository;
    @Mock
    private StepRepository stepRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void throwsWhenUserDoesntExist() {
        when(userRepository.existsByIdAndIsDeletedFalse(5)).thenReturn(Boolean.FALSE);

        assertThatThrownBy(() -> userService.getUserScore(5))
                .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    void returnsCorrectDurationScore() {

    }

    @Test
    void returnsCorrectPhotoScore() {

    }

    @Test
    void returnsCorrectTextScore() {

    }

    @Test
    void returnsCorrectConsecutiveDistanceScore() {

    }

    @Test
    void returnsCorrectDiversityScore() {

    }

}
