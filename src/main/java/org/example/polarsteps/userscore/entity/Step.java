package org.example.polarsteps.userscore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;
import java.time.OffsetDateTime;

@Getter
@Setter
@Entity
@Table(name = "steps")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Size(max = 100000)
    @Column(name = "description", length = 100000)
    private String description;

    @Column(name = "type")
    private Short type;

    @Column(name = "start_time")
    private Instant startTime;

    @Column(name = "end_time")
    private Instant endTime;

    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "synchronized")
    private Boolean synchronizedField;

    @Size(max = 500)
    @Column(name = "main_media_item_path", length = 500)
    private String mainMediaItemPath;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "creation_time")
    private OffsetDateTime creationTime;

    @Column(name = "zelda_step_id")
    private Integer zeldaStepId;

    @Size(max = 255)
    @Column(name = "timezone_id")
    private String timezoneId;

    @Size(max = 255)
    @Column(name = "slug")
    private String slug;

    @Column(name = "fb_publish_status")
    private Short fbPublishStatus;

    @Size(max = 100)
    @Column(name = "open_graph_id", length = 100)
    private String openGraphId;

    @ColumnDefault("0")
    @Column(name = "likes")
    private Integer likes;

    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views;

    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private String uuid;

    @NotNull
    @ColumnDefault("now()")
    @Column(name = "db_insertion_time", nullable = false)
    private Instant dbInsertionTime;

    @Size(max = 500)
    @Column(name = "facebook_image_url", length = 500)
    private String facebookImageUrl;

    @Size(max = 50)
    @Column(name = "weather_condition", length = 50)
    private String weatherCondition;

    @Column(name = "weather_temperature")
    private Double weatherTemperature;

    @Column(name = "comment_count")
    private Integer commentCount;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "spot_count")
    private Integer spotCount;


}