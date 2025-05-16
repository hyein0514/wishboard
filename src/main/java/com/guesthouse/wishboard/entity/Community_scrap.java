package com.guesthouse.wishboard.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "community_scrap")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Community_scrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_scrap_id", nullable = false)
    private Long communityScrapId;

    @Column(name = "community_name", nullable = false)
    private String communityName;

    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;
}
