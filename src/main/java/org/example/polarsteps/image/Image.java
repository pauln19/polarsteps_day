package org.example.polarsteps.image;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private java.lang.Integer id;

    @Size(max = 500)
    @Column(name = "path", length = 500)
    private java.lang.String path;

    @Column(name = "step_id")
    private java.lang.Integer stepId;

    @Size(max = 500)
    @Column(name = "large_thumbnail_path", length = 500)
    private java.lang.String largeThumbnailPath;

    @Size(max = 500)
    @Column(name = "small_thumbnail_path", length = 500)
    private java.lang.String smallThumbnailPath;

    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private java.lang.String uuid;

    @Column(name = "\"order\"")
    private java.lang.Short order;

    @Column(name = "lat")
    private java.lang.Double lat;

    @Column(name = "lon")
    private java.lang.Double lon;

}