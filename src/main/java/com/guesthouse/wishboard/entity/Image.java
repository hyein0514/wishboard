package com.guesthouse.wishboard.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long imageId;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

//    @Column(name = "community_id", nullable = false)
//    private Long communityId;
    
    @ManyToOne
    @JoinColumn(name = "community_id", nullable = false)
    private Community community;
}
