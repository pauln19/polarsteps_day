package org.example.polarsteps.image;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@WebMvcTest(ImageController.class)
class ImageControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private ImageService imageService;

    @Test
    void returnsImageById() {
        given(this.imageService.getImageById(42))
                .willReturn(new ImageDtos.ImageResponse("/images/42.jpg"));

        assertThat(this.mvc.get().uri("/api/image/{id}", 42))
                .hasStatus(HttpStatus.OK)
                .hasContentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .bodyJson()
                .extractingPath("$.path")
                .isEqualTo("/images/42.jpg");
    }
}
