package org.example.polarsteps.userscore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "media")
public class Media {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "type")
    private Short type;

    @Size(max = 500)
    @Column(name = "path", length = 500)
    private String path;

    @Column(name = "step_id")
    private Integer stepId;

    @Size(max = 2000)
    @Column(name = "description", length = 2000)
    private String description;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "synchronized")
    private Boolean synchronizedField;

    @Size(max = 500)
    @Column(name = "large_thumbnail_path", length = 500)
    private String largeThumbnailPath;

    @Size(max = 500)
    @Column(name = "small_thumbnail_path", length = 500)
    private String smallThumbnailPath;

    @Size(max = 255)
    @Column(name = "md5")
    private String md5;

    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private String uuid;

    @Column(name = "\"order\"")
    private Short order;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "full_res_unavailable", nullable = false)
    private Boolean fullResUnavailable;

    @Column(name = "full_res_width")
    private Double fullResWidth;

    @Column(name = "full_res_height")
    private Double fullResHeight;

    @Column(name = "aspect_ratio")
    private Double aspectRatio;

    @Column(name = "backup_uploaded")
    private Boolean backupUploaded;

    @Column(name = "duration")
    private Long duration;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Column(name = "point", columnDefinition = "geography")
    private Object point;

    @Column(name = "geolocation_from_step")
    private Boolean geolocationFromStep;


}