package org.example.polarsteps.userscore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @Column(name = "name")
    private String name;

    @Column(name = "lat")
    private Double lat;

    @Column(name = "lon")
    private Double lon;

    @Size(max = 100)
    @Column(name = "detail", length = 100)
    private String detail;

    @ColumnDefault("false")
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "last_modified")
    private Instant lastModified;

    @Column(name = "synchronized")
    private Boolean synchronizedField;

    @Size(max = 200)
    @Column(name = "full_detail", length = 200)
    private String fullDetail;

    @Size(max = 255)
    @Column(name = "venue")
    private String venue;

    @ColumnDefault("uuid_generate_v4()")
    @Column(name = "uuid", length = Integer.MAX_VALUE)
    private String uuid;

    @Size(max = 3)
    @Column(name = "country_code", length = 3)
    private String countryCode;

    @Column(name = "accuracy")
    private Double accuracy;

    @Column(name = "\"precision\"")
    private Double precision;

    @Column(name = "elevation")
    private Double elevation;

    @Column(name = "point", columnDefinition = "geography")
    private Object point;


}