package com.guesthouse.wishboard.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
@Entity
@Table(name = "bucketList")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bucket_id", nullable = false)
    private Long bucketId;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "targetDate", nullable = true)
    private Date targetDate;

    @Column(name = "PinToTop", nullable = true)
    private boolean PinToTop;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "resolution", nullable = false)
    private String resolution;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "achieved_at", nullable = true)
    private Date achievedAt;

    @Column(name = "trophy", nullable = true)
    private String trophy;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;
    
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}

