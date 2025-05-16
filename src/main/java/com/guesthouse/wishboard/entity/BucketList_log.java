package com.guesthouse.wishboard.entity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "bucketList_log")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BucketList_log {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id", nullable = false)
    private Long logId;

    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "bucket_id", insertable = false, updatable = false)
    private BucketList bucketList;
}
