package org.example.polarsteps.image;

import org.example.polarsteps.common.error.ResourceNotFoundException;
import org.example.polarsteps.common.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageService imageService;

    @Test
    void returnsImageResponseWhenImageExists() {
        Image image = new Image();
        image.setPath("/images/42.jpg");
        when(imageRepository.findById(42)).thenReturn(Optional.of(image));

        ImageDtos.ImageResponse response = imageService.getImageById(42);

        assertThat(response.path()).isEqualTo("/images/42.jpg");
    }

    @Test
    void throwsWhenImageDoesNotExist() {
        when(imageRepository.findById(42)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> imageService.getImageById(42))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
