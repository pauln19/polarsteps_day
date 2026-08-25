package org.example.polarsteps.userscore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
    @Id
    private Integer id;

    private String username;
    @Size(max = 255)
    @Column(name = "profile_image_path")
    private String profileImagePath;
    @ColumnDefault("1")
    @Column(name = "visibility")
    private Short visibility;
    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;
    @Size(max = 100)
    @Column(name = "traveller_type", length = 100)
    private String travellerType;
    @NotNull
    @ColumnDefault("false")
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;
    @Column(name = "last_modified")
    private Instant lastModified;
    @Column(name = "synchronized")
    private Boolean synchronizedField;
    @Column(name = "creation_date")
    private Instant creationDate;
    @Size(max = 255)
    @Column(name = "living_location_name")
    private String livingLocationName;
    @NotNull
    @ColumnDefault("true")
    @Column(name = "unit_is_km", nullable = false)
    private Boolean unitIsKm;
    @Column(name = "quality_score")
    private Double qualityScore;
    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private String uuid;
    @Size(max = 255)
    @Column(name = "profile_image_thumb_path")
    private String profileImageThumbPath;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mashup_user_id")
    private User mashupUser;
    @NotNull
    @ColumnDefault("true")
    @Column(name = "temperature_is_celsius", nullable = false)
    private Boolean temperatureIsCelsius;
    @Size(max = 32)
    @Column(name = "locale", length = 32)
    private String locale;
    @Column(name = "has_multiple_devices")
    private Boolean hasMultipleDevices;
}
