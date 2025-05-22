package com.guesthouse.wishboard.entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "community")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @Column(name = "type", nullable = false)
    private String type;

    @Column(name = "community_type", nullable = false)
    private String communityType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "created_at", nullable = false,  updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "community_diversity", nullable = false)
    private String communityDiversity;
    
    @Column(name = "bucket_id", nullable = false)
    private Long bucketId;

    @ManyToOne
    @JoinColumn(name = "bucket_id", insertable = false, updatable = false)
    private BucketList bucketList;
}
