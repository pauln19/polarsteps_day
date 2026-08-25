package org.example.polarsteps.userscore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "trips")
public class Trip {
    @Column(name = "future_timeline_last_modified")
    private Instant futureTimelineLastModified;
    @Column(name = "planned_steps_visible")
    private Boolean plannedStepsVisible;
    @Column(name = "featured_priority_for_new_users")
    private Boolean featuredPriorityForNewUsers;
    @Size(max = 80)
    @Column(name = "summary", length = 80)
    private String summary;
    @Size(max = 255)
    @ColumnDefault("'GMT'")
    @Column(name = "timezone_id")
    private String timezoneId;
    @NotNull
    @ColumnDefault("now()")
    @Column(name = "creation_time", nullable = false)
    private Instant creationTime;
    @Column(name = "feature_date")
    private LocalDate featureDate;
    @Size(max = 2)
    @Column(name = "language", length = 2)
    private String language;
    @Size(max = 500)
    @Column(name = "feature_text", length = 500)
    private String featureText;
    @Column(name = "featured")
    private Boolean featured;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mashup_user_id")
    private User mashupUser;
    @Size(max = 500)
    @Column(name = "cover_photo_thumb_path", length = 500)
    private String coverPhotoThumbPath;
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private String uuid;
    @Size(max = 500)
    @Column(name = "cover_photo_path", length = 500)
    private String coverPhotoPath;
    @Column(name = "quality_score")
    private Double qualityScore;
    @ColumnDefault("0")
    @Column(name = "views")
    private Integer views;
    @ColumnDefault("0")
    @Column(name = "likes")
    private Integer likes;
    @Column(name = "total_km")
    private Double totalKm;
    @Size(max = 100)
    @Column(name = "open_graph_id", length = 100)
    private String openGraphId;
    @Column(name = "fb_publish_status")
    private Short fbPublishStatus;
    @Size(max = 255)
    @Column(name = "slug")
    private String slug;
    @Column(name = "synchronized")
    private Boolean synchronizedField;
    @Column(name = "last_modified")
    private Instant lastModified;
    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    @Column(name = "end_date")
    private Instant endDate;
    @Column(name = "start_date")
    private Instant startDate;
    @ColumnDefault("0")
    @Column(name = "visibility")
    private Short visibility;
    @Column(name = "type")
    private Short type;
    @Size(max = 50)
    @Column(name = "name", length = 50)
    private String name;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
}
